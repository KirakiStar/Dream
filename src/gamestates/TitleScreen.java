package gamestates;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import helper.ResourceLoader;
import main.Game;

public class TitleScreen extends State implements StateMethods{
	private String titlePng = ResourceLoader.TITLESCREEN;
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
		g2.drawImage(background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_Z:
			case KeyEvent.VK_ENTER:
			case KeyEvent.VK_SPACE:
				Gamestate.state = Gamestate.PLAYING;
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
}
