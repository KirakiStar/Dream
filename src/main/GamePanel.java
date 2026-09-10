package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;
import static main.Game.GAME_WIDTH;
import static main.Game.GAME_HEIGHT;

public class GamePanel extends JPanel{
	private MouseInputs mouseInputs;
	private Game game;
	
	public GamePanel(Game game) {
		mouseInputs = new MouseInputs();
		this.game = game;
		
		setPanelSize();
		addKeyListener(new KeyboardInputs(this));
		
		/*
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
		*/
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
		game.render(g2);
	}
	
	public Game getGame() { return game; }
}
