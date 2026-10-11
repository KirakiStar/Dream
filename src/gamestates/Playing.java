package gamestates;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.List;
import java.awt.geom.AffineTransform;

import main.Game;
import display.Camera;
import display.PlayingUI;
import helpers.EntityManager;
import entities.Player;
import entities.Boss;
import levels.Level;
import levels.LevelData;
import levels.LevelManager;

public class Playing extends State implements StateMethods {
	private Player player;
	private Camera camera;
	private PlayingUI playingUI;
	private static LevelManager levelManager;
	private List<Integer> collisionData;
	private int levelWidth;
	private int levelHeight;
	private float arenaMinX;
	private float arenaMaxX;
	private static EntityManager entityManager;
	private List<Integer> entityData;
	
	public Playing(Game game) {
		super(game);
		init();
	}
	
	private void init() {
		playingUI = new PlayingUI(this);
		levelManager = new LevelManager(this);
		entityManager = new EntityManager(this);
		Level current = levelManager.getCurrentLevel();

		player = new Player(this, current.getSpawnX() * Game.SCALE, current.getSpawnY() * Game.SCALE);
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
			player.setPosition(spawnX * Game.SCALE, spawnY * Game.SCALE);
		}
		
		if (entityManager != null) {
			entityManager.loadEntities();
		}
	}
	
	public void respawnPlayer() {
		Level currentLevel = levelManager.getCurrentLevel();
		float spawnX = currentLevel.getSpawnX() * Game.SCALE;
		float spawnY = currentLevel.getSpawnY() * Game.SCALE;

		player.reset(spawnX, spawnY);
		camera.setLevelBounds();
	}
	
	private void checkNextLevelTriggers() {
		List<LevelData.TriggerData> triggers = levelManager.getCurrentLevel().getTriggers();
		if (triggers == null) return;

		for (LevelData.TriggerData trigger : triggers) {
			float tx = trigger.getX() * Game.SCALE;
			float ty = trigger.getY() * Game.SCALE;
			float tw = (trigger.getWidth() == -1)?
						levelWidth * Game.TILES_SIZE * Game.SCALE 
						: trigger.getWidth() * Game.SCALE;

			float th = (trigger.getHeight() == -1)?
						levelHeight * Game.TILES_SIZE * Game.SCALE 
						: trigger.getHeight() * Game.SCALE;

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
		
		Boss activeBoss = entityManager.getActiveBoss();

		if (activeBoss != null && activeBoss.isAlive()) {
			if (!camera.isLocked() && player.getX() >= arenaMinX * Game.SCALE) {
				float minX = arenaMinX * Game.SCALE;
				float maxX = arenaMaxX * Game.SCALE;
				camera.lockToArea(minX, maxX);
			}
		} else if (camera.isLocked()) {
			camera.unlock();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				player.setRight(true);
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				player.setLeft(true);
				break;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				player.setUp(true);
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				player.setDown(true);
				break;
			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_Z:
				player.setJump(true);
				break;
			case KeyEvent.VK_X:
			case KeyEvent.VK_SHIFT:
				player.attack();
				break;
			case KeyEvent.VK_K:
				player.takeDamage(1);
				break;
			case KeyEvent.VK_R:
				Gamestate.state = Gamestate.TITLESCREEN;
//				resetAll();
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				player.setRight(false);
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				player.setLeft(false);
				break;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				player.setUp(false);
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				player.setDown(false);
				break;
			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_Z:
				player.setJump(false);
				break;
		}
	}
	
//	private void resetAll() {
//		player.reset();
//		levelManager.reset();
//		camera.reset();
//	}
	
	public Player getPlayer() { return player; }
	public Camera getCamera() { return camera; }
	public LevelManager getLevelManager() { return levelManager; }
	public List<Integer> getCollisionData() { return collisionData; }
	public int getLevelWidth() { return levelWidth; }
	public int getLevelHeight() { return levelHeight; }
	public EntityManager getEntityManager() { return entityManager; }
	public List<Integer> getEntityData() { return entityData; }
}
