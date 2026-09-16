package gamestates;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public interface StateMethods {
	public void update();
	public void draw(Graphics2D g2);
	public void keyPressed(KeyEvent e);
	public void keyReleased(KeyEvent e);
}
