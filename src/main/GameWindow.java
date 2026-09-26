package main;

import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JFrame;

import helper.ResourceLoader;

public class GameWindow {
	private JFrame jframe;

	public GameWindow(GamePanel gamePanel) {
		jframe = new JFrame();
		jframe.setTitle("Dream");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Image icon = ResourceLoader.ImagesLoader(ResourceLoader.ICON);
		jframe.setIconImage(icon);

		jframe.add(gamePanel);
		jframe.pack();
		jframe.setResizable(false);
		jframe.setLocationRelativeTo(null);

		jframe.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				gamePanel.requestFocusInWindow();
			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				gamePanel.getGame().getPlaying().getPlayer().resetDirection();
			}
		});

		jframe.setVisible(true);

		gamePanel.requestFocusInWindow(); 
	}
}