package gamestates;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.List;
import java.awt.geom.AffineTransform;

import main.Game;
import display.Camera;
import entities.Player;
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
		initializer();
	}
	
	private void initializer() {
		levelManager = new LevelManager(this);
		player = new Player(this, 100 * Game.SCALE, 100 * Game.SCALE);
		loadLevelData(levelManager);
		camera = new Camera(this);
	}
	
	public void loadLevelData(LevelManager levelManager) {
		this.collisionData = levelManager.getCurrentLevel().getCollisionData();
		this.levelWidth = levelManager.getCurrentLevel().getLevelWidth();
		this.levelHeight = levelManager.getCurrentLevel().getLevelHeight();
	}

	@Override
	public void update() {
		player.update();
		levelManager.update();
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
