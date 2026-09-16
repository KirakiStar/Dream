package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import inputs.KeyboardInputs;
import static main.Game.GAME_WIDTH;
import static main.Game.GAME_HEIGHT;

public class GamePanel extends JPanel{
	private Game game;
	
	public GamePanel(Game game) {
		this.game = game;
		setPanelSize();
		addKeyListener(new KeyboardInputs(this));
	}
	
	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setPreferredSize(size);
		System.out.println("Game size: " + GAME_WIDTH + " * " + GAME_HEIGHT);
	}
	
	public void updateGame() {
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
//		g2.translate(-game.getPlaying().getCamera().getX(), -game.getPlaying().getCamera().getY());
		game.render(g2);
//		g2.translate(game.getCamera().getX(), game.getCamera().getY());
	}
	
	public Game getGame() { return game; }
}
