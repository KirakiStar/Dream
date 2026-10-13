package main;

import java.awt.Graphics2D;

import gamestates.Gamestate;
import gamestates.Playing;
import gamestates.TitleScreen;
import helpers.ResourceLoader;

public class Game{
	private final GameWindow gameWindow;
	private final GamePanel gamePanel;
	private GameLoop gameLoop;
	
	private TitleScreen titleScreen;
	private Playing playing;
	
	public Game() {
		init();
		
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();
		
		startGameLoop();
	}
	
	private void init() {
		ResourceLoader.loadFonts();
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
