package main;

import java.awt.Graphics2D;

import gamestates.Gamestate;
import gamestates.Playing;
import gamestates.TitleScreen;

public class Game{
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private GameLoop gameLoop;
	
	private TitleScreen titleScreen;
	private Playing playing;
	
	public final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 2.0f;
	public final static int TILES_IN_WIDTH = 12;
	public final static int TILES_IN_HEIGHT = 9;
	public final static int TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
	public final static float CAMERA_Y_RATIO = 0.3f;
	
	
	public Game() {
		initializer();
		
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();
		
		startGameLoop();
	}
	
	private void initializer() {
		titleScreen = new TitleScreen(this);
		playing = new Playing(this);
	}
	
	private void startGameLoop() {
		gameLoop = new GameLoop(this);
		gameLoop.start();
	}
	
	public void update() {
		switch (Gamestate.state) {
			case TITLESCREEN:
				titleScreen.update();
				break;
			case PLAYING:
				playing.update();
				break;
		}
	}
	
	public void render(Graphics2D g2) {
		switch (Gamestate.state) {
			case TITLESCREEN:
				titleScreen.draw(g2);
				break;
			case PLAYING:
				playing.draw(g2);
				break;
		}
	}
	
	public GamePanel getGamePanel() { return gamePanel; }
	public Playing getPlaying() { return playing; }
	public TitleScreen getTitleScreen() { return titleScreen; }
}
