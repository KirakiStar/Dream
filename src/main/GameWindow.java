package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
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
		jframe.setLocationRelativeTo(null);
		jframe.setResizable(false);
		jframe.setVisible(true);
		/*
		jframe.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
			}
			
		});
		*/
	}
}
