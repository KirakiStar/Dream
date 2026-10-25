package main;

import java.awt.Graphics2D;

import gamestates.Gamestate;
import gamestates.TitleScreen;
import gamestates.Playing;
import gamestates.Cutscene;
import display.ScreenTransition;
import helpers.ResourceLoader;

public class Game{
	private final GameWindow gameWindow;
	private final GamePanel gamePanel;
	private GameLoop gameLoop;
	
	private TitleScreen titleScreen;
	private Playing playing;
	private Cutscene cutscene;
	
	private ScreenTransition transition;
	
	public Game() {
		init();
		
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();
		
		startGameLoop();
	}
	
	private void init() {
		ResourceLoader.fontCharacters = ResourceLoader.loadSpriteSheet(ResourceLoader.GUI_ITEMS);
		titleScreen = new TitleScreen(this);
		playing = new Playing(this);
		cutscene = new Cutscene(this);
		transition = new ScreenTransition(ResourceLoader.BLACKSCREEN);
	
		cutscene.startCutscene(ResourceLoader.INTRO_CUTSCENE, () -> {
			changeStateWithFade(Gamestate.TITLESCREEN, 0.05f);
		});
	}
	
	private void startGameLoop() {
		gameLoop = new GameLoop(this);
		gameLoop.start();
	}
	
	public void update() {
		transition.update();

		if (!transition.isTransitioning()) {
			switch (Gamestate.state) {
				case TITLESCREEN -> titleScreen.update();
				case PLAYING -> playing.update();
				case CUTSCENE -> cutscene.update();
			}
		}
	}
	
	public void render(Graphics2D g2) {
		switch (Gamestate.state) {
			case TITLESCREEN -> titleScreen.draw(g2);
			case PLAYING -> playing.draw(g2);
			case CUTSCENE -> cutscene.draw(g2);
		}
		transition.draw(g2);
	}
	
	public void changeStateWithFade(Gamestate newState, float fadeSpeed) {
		transition.startTransition(() -> {
			Gamestate.state = newState;
			if (newState == Gamestate.PLAYING) {
				resetPlaying();
			}
		}, fadeSpeed);
	}
	
	public void loadLevelWithFade(int levelIndex, float spawnX, float spawnY) {
		transition.startTransition(() -> {
			if (playing != null && playing.getLevelManager() != null) {
				playing.getLevelManager().loadLevel(levelIndex, spawnX, spawnY);
			}
		});
	}
	
	public void advanceCutsceneWithFade(Runnable onComplete) {
		transition.startTransition(onComplete);
	}
	
	public GamePanel getGamePanel() { return gamePanel; }
	public GameWindow getGameWindow() { return gameWindow; }
	
	public TitleScreen getTitleScreen() {
		if (titleScreen == null) {
			titleScreen = new TitleScreen(this);
		}
		return titleScreen;
	}
	
	public Playing getPlaying() {
		if (playing == null) {
			playing = new Playing(this);
		}
		return playing;
	}
	
	public Cutscene getCutscene() { 
		if (cutscene == null) {
			cutscene = new Cutscene(this);
		}
		return cutscene; 
	}
	
	public void resetPlaying() {
		playing = new Playing(this);
		helpers.BossesKillStatus.resetBossKillStatus();
	}
}
