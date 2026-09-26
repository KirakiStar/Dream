package inputs;

import gamestates.Gamestate;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;


public class KeyboardInputs implements KeyListener{
	private GamePanel gamePanel;
	
	public KeyboardInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (Gamestate.state) {
			case TITLESCREEN:
				gamePanel.getGame().getTitleScreen().keyPressed(e);
//				System.out.println("Start");
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
	
}
