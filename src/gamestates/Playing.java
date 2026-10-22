package gamestates;

import java.util.List;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

import main.Game;
import display.Camera;
import display.PlayingUI;
import entities.EntityManager;
import entities.player.Player;
import entities.bosses.Boss;
import levels.Level;
import levels.LevelData;
import levels.LevelManager;
import helpers.Settings;

public class Playing extends State implements StateMethods {
	private Player player;
	private Camera camera;
	private PlayingUI playingUI;
	
	private LevelManager levelManager;
	private List<Integer> collisionData;
	private int levelWidth;
	private int levelHeight;
	private float arenaMinX;
	private float arenaMaxX;
	
	private static EntityManager entityManager;
	private List<Integer> entityData;
	
	private final java.util.Set<Integer> pressedKeys = new java.util.HashSet<>();
	
	public Playing(Game game) {
		super(game);
		init();
	}
	
	private void init() {
		playingUI = new PlayingUI(this);
		levelManager = new LevelManager(this);
		Level current = levelManager.getCurrentLevel();
		player = new Player(this, current.getSpawnX() * Settings.getCurrentScale(), current.getSpawnY() * Settings.getCurrentScale());
		entityManager = new EntityManager(this);
		camera = new Camera(this);
		loadLevelData(levelManager, current.getSpawnX(), current.getSpawnY());
	}

	@Override
	public void update() {
		player.update();
		levelManager.update();
		entityManager.update();
		checkNextLevelTriggers();
		camera.update();
		checkBossTrigger();
	}

	@Override
	public void draw(Graphics2D g2) {
		AffineTransform originalTransform = g2.getTransform();
		
		g2.translate(-camera.getX(), -camera.getY());
		levelManager.draw(g2);
		entityManager.draw(g2);
		player.draw(g2);
		
		g2.setTransform(originalTransform);
		playingUI.draw(g2);
	}
	
	public void loadLevelData(LevelManager levelManager, float spawnX, float spawnY) {
		Level currentLevel = levelManager.getCurrentLevel();
		this.collisionData = currentLevel.getCollisionData();
		this.levelWidth = currentLevel.getLevelWidth();
		this.levelHeight = currentLevel.getLevelHeight();
		this.arenaMinX = currentLevel.getArenaMinX();
		this.arenaMaxX = currentLevel.getArenaMaxX();
		this.entityData = currentLevel.getEntityData();

		if (player != null) {
			player.setPosition(spawnX * Settings.getCurrentScale(), spawnY * Settings.getCurrentScale());
		}
		
		if (camera != null) {
			camera.setLevelBounds();
			camera.snapToPlayer();
		}
		
		if (entityManager != null) {
			entityManager.loadEntities();
		}
	}
	
	public void respawnPlayer() {
		Level currentLevel = levelManager.getCurrentLevel();
		float spawnX = currentLevel.getSpawnX() * Settings.getCurrentScale();
		float spawnY = currentLevel.getSpawnY() * Settings.getCurrentScale();

		loadLevelData(levelManager, spawnX, spawnY);
		player.reset(spawnX, spawnY);
	}
	
	private void checkNextLevelTriggers() {
		List<LevelData.Trigger> triggers = levelManager.getCurrentLevel().getTriggers();
		if (triggers == null) return;

		for (LevelData.Trigger trigger : triggers) {
			float tx = trigger.getX() * Settings.getCurrentScale();
			float ty = trigger.getY() * Settings.getCurrentScale();
			float tw = (trigger.getWidth() == -1)?
						levelWidth * Settings.getTileSize() * Settings.getCurrentScale() 
						: trigger.getWidth() * Settings.getCurrentScale();

			float th = (trigger.getHeight() == -1)?
						levelHeight * Settings.getTileSize() * Settings.getCurrentScale() 
						: trigger.getHeight() * Settings.getCurrentScale();

			if (player.getHitbox().getBounds().intersects(tx, ty, tw, th)) {
				boolean isAuto = "AUTO".equalsIgnoreCase(trigger.getActivation());
				boolean isInteract = "INTERACT".equalsIgnoreCase(trigger.getActivation()) && player.isUp();

				if (isAuto || isInteract) {
					player.setUp(false);
					levelManager.loadLevel(trigger.getTargetLevel(), trigger.getSpawnX(), trigger.getSpawnY());
					camera.setLevelBounds();
					break;
				}
			}
		}
	}
	
	public void checkBossTrigger() {
		if (arenaMinX < 0 || arenaMaxX < 0) return;
		
		Boss activeBoss = getActiveBoss();

		if (activeBoss != null && activeBoss.isAlive()) {
			if (!camera.isLocked() && player.getX() >= arenaMinX * Settings.getCurrentScale()) {
				float minX = arenaMinX * Settings.getCurrentScale();
				float maxX = arenaMaxX * Settings.getCurrentScale();
				camera.lockToArea(minX, maxX);
			}
		} else if (camera.isLocked()) {
			camera.unlock();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!pressedKeys.add(e.getKeyCode()))  return;
		
		switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> player.setRight(true);
			case KeyEvent.VK_LEFT, KeyEvent.VK_A -> player.setLeft(true);
			case KeyEvent.VK_UP, KeyEvent.VK_W -> player.setUp(true);
			case KeyEvent.VK_DOWN, KeyEvent.VK_S -> player.setDown(true);
			case KeyEvent.VK_SPACE, KeyEvent.VK_Z -> player.setJump(true);
			case KeyEvent.VK_X, KeyEvent.VK_SHIFT -> player.attack();
			case KeyEvent.VK_K -> player.takeDamage(1);
			case KeyEvent.VK_L -> player.takeDamage(player.getCurrentHealth());
			case KeyEvent.VK_R -> {
				game.resetPlaying();
				Gamestate.state = Gamestate.TITLESCREEN;
			}
			case KeyEvent.VK_7 -> player.activateCheat();
			default -> {}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		pressedKeys.remove(e.getKeyCode());
		
		switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> player.setRight(false);
			case KeyEvent.VK_LEFT, KeyEvent.VK_A -> player.setLeft(false);
			case KeyEvent.VK_UP, KeyEvent.VK_W -> player.setUp(false);
			case KeyEvent.VK_DOWN, KeyEvent.VK_S -> player.setDown(false);
			case KeyEvent.VK_SPACE, KeyEvent.VK_Z -> player.setJump(false);
			default -> {}
		}
	}
	
	public Player getPlayer() { return player; }
	public Camera getCamera() { return camera; }
	public LevelManager getLevelManager() { return levelManager; }
	public List<Integer> getCollisionData() { return collisionData; }
	public int getLevelWidth() { return levelWidth; }
	public int getLevelHeight() { return levelHeight; }
	public EntityManager getEntityManager() { return entityManager; }
	public List<Integer> getEntityData() { return entityData; }
	public Boss getActiveBoss() { return entityManager.getActiveBoss(); }
}
