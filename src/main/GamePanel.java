package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import static helpers.Settings.GAME_HEIGHT;
import static helpers.Settings.GAME_WIDTH;

public class GamePanel extends JPanel{
	private Game game;
	
	public GamePanel(Game game) {
		this.game = game;
		setPanelSize();
		setFocusable(true);
		addKeyListener(new Inputs(this));
	}
	
	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setPreferredSize(size);
//		System.out.println("Game size: " + GAME_WIDTH + " * " + GAME_HEIGHT);
	}
	
	public void updateGame() {
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		game.render(g2);
	}
	
	public Game getGame() { return game; }
}
