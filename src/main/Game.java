package main;

import java.awt.Graphics2D;
import java.util.List;

import entities.Player;
import levels.LevelManager;

public class Game{
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private GameLoop gameLoop;
	
	private Player player;
	private static LevelManager levelManager;
	private List<Integer> collisionData;
	private int levelWidth;
	private int levelHeight;
	
	public final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 2.0f;
	public final static int TILES_IN_WIDTH = 12;
	public final static int TILES_IN_HEIGHT = 9;
	public final static int TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
	
	
	public Game() {
		initializer();
		
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();
		
		startGameLoop();
	}
	
	private void initializer() {
		levelManager = new LevelManager(this);
		player = new Player(this, 100 * SCALE, 100 * SCALE, (int)(9*SCALE), (int)(24*SCALE), 11 * SCALE, 8 * SCALE);
		loadLevelData(levelManager);
	}
	
	private void startGameLoop() {
		gameLoop = new GameLoop(this);
		gameLoop.start();
	}
	
	public void update() {
		player.update();
		levelManager.update();
	}
	
	public void render(Graphics2D g2) {
		levelManager.draw(g2);
		player.draw(g2);
	}
	
	public void loadLevelData(LevelManager levelManager) {
		this.collisionData = levelManager.getCurrentLevel().getCollisionData();
		this.levelWidth = levelManager.getCurrentLevel().getLevelWidth();
		this.levelHeight = levelManager.getCurrentLevel().getLevelHeight();
	}
	
	public GamePanel getGamePanel() { return gamePanel; }
	public Player getPlayer() { return player; }
	public LevelManager getLevelManager() { return levelManager; }
	public List<Integer> getCollisionData() { return collisionData; }
	public int getLevelWidth() { return levelWidth; }
	public int getLevelHeight() { return levelHeight; }
}
