package gamestates;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import main.Game;
import display.TitleUI;
import helpers.Settings;
import helpers.ResourceLoader;

public class TitleScreen extends State implements StateMethods{
	private TitleUI title;
	private final String titlePng = ResourceLoader.TITLESCREEN;
	BufferedImage background;
	
	public TitleScreen(Game game) {
		super(game);
		background = ResourceLoader.ImagesLoader(titlePng);
		title = new TitleUI(this);
	}
	
	@Override
	public void update() {
		title.update();
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.drawImage(background, 0, 0, Settings.getGameWidth(), Settings.getGameHeight(), null);
		title.draw(g2);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (title.isStarting()) return;
		
		switch (e.getKeyCode()) {
			case KeyEvent.VK_Z, KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> {
				title.triggerStart();
			}
			case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> {
				Settings.setNewScale(1);
				game.getGameWindow().resizeWindow(game.getGamePanel());
				title.setPressingRight(true);
			}
			case KeyEvent.VK_LEFT, KeyEvent.VK_A -> {
				Settings.setNewScale(-1);
				game.getGameWindow().resizeWindow(game.getGamePanel());
				title.setPressingLeft(true);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> title.setPressingRight(false);
			case KeyEvent.VK_LEFT, KeyEvent.VK_A -> title.setPressingLeft(false);
		}
	}
}
