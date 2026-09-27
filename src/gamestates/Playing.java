package gamestates;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.List;
import java.awt.geom.AffineTransform;

import main.Game;
import display.Camera;
import entities.Player;
import levels.Level;
import levels.LevelData;
import levels.LevelManager;

public class Playing extends State implements StateMethods {
	private Player player;
	private static LevelManager levelManager;
	private List<Integer> collisionData;
	private int levelWidth;
	private int levelHeight;
	private Camera camera;
	
	public Playing(Game game) {
		super(game);
		init();
	}
	
	private void init() {
		levelManager = new LevelManager(this);
		Level current = levelManager.getCurrentLevel();

		player = new Player(this, current.getSpawnX() * Game.SCALE, current.getSpawnY() * Game.SCALE);
		camera = new Camera(this);

		loadLevelData(levelManager, current.getSpawnX(), current.getSpawnY());
	}
	
	public void loadLevelData(LevelManager levelManager, float spawnX, float spawnY) {
		Level currentLevel = levelManager.getCurrentLevel();
		this.collisionData = currentLevel.getCollisionData();
		this.levelWidth = currentLevel.getLevelWidth();
		this.levelHeight = currentLevel.getLevelHeight();

		if (player != null) {
			player.setPosition(spawnX * Game.SCALE, spawnY * Game.SCALE);
		}
	}
	
	private void checkTriggers() {
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

	@Override
	public void update() {
		player.update();
		levelManager.update();
		checkTriggers();
		camera.update();
	}

	@Override
	public void draw(Graphics2D g2) {
		AffineTransform originalTransform = g2.getTransform();
		
		g2.translate(-camera.getX(), -camera.getY());
		levelManager.draw(g2);
		player.draw(g2);
		
		g2.setTransform(originalTransform);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
				player.setRight(true);
				break;
			case KeyEvent.VK_LEFT:
				player.setLeft(true);
				break;
			case KeyEvent.VK_UP:
				player.setUp(true);
				break;
			case KeyEvent.VK_DOWN:
				player.setDown(true);
				break;
			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_Z:
				player.setJump(true);
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
				player.setRight(false);
				break;
			case KeyEvent.VK_LEFT:
				player.setLeft(false);
				break;
			case KeyEvent.VK_UP:
				player.setUp(false);
				break;
			case KeyEvent.VK_DOWN:
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
}
