package gamestates;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import main.Game;
import helpers.Settings;
import helpers.ResourceLoader;

public class TitleScreen extends State implements StateMethods{
	private final String titlePng = ResourceLoader.TITLESCREEN;
	BufferedImage background;
	
	public TitleScreen(Game game) {
		super(game);
		loadTitleScreen();
	}
	
	private void loadTitleScreen() {
		background = ResourceLoader.ImagesLoader(titlePng);
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.drawImage(background, 0, 0, Settings.getGameWidth(), Settings.getGameHeight(), null);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_Z, KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> {
				game.resetPlaying();
				Gamestate.state = Gamestate.PLAYING;
			}
			case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> {
				Settings.setNewScale(1);
				game.getGameWindow().resizeWindow(game.getGamePanel());
			}
			case KeyEvent.VK_LEFT, KeyEvent.VK_A -> {
				Settings.setNewScale(-1);
				game.getGameWindow().resizeWindow(game.getGamePanel());
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) { }
	
}
