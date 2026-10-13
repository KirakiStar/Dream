package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gamestates.Gamestate;

public class Inputs implements KeyListener{
	private GamePanel gamePanel;
	
	public Inputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (Gamestate.state) {
			case TITLESCREEN:
				gamePanel.getGame().getTitleScreen().keyPressed(e);
				break;
			case PLAYING:
				gamePanel.getGame().getPlaying().keyPressed(e);
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (Gamestate.state) {
			case TITLESCREEN:
				gamePanel.getGame().getTitleScreen().keyReleased(e);
				break;
			case PLAYING:
				gamePanel.getGame().getPlaying().keyReleased(e);
				break;
		}
	}
	
	@Override public void keyTyped(KeyEvent e) { }
}
