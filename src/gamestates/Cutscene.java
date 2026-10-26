package gamestates;

import java.util.List;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import main.Game;
import helpers.Settings;
import helpers.ResourceLoader;

public class Cutscene extends State implements StateMethods {
	private final List<BufferedImage> cutsceneImages = new ArrayList<>();
	private int currentIndex = 0;
	private Runnable onCutsceneFinish;
	private boolean escapeOnly = false;
	
	public Cutscene(Game game) {
		super(game);
	}
	
	public void startCutscene(String[] filePaths, Runnable onFinish, boolean escapeOnly) {
		cutsceneImages.clear();
		for (String path : filePaths) {
			cutsceneImages.add(ResourceLoader.ImagesLoader(path));
		}
		currentIndex = 0;
		this.onCutsceneFinish = onFinish;
		this.escapeOnly = escapeOnly;
	}
	
	public void startCutscene(String[] filePaths, Runnable onFinish) {
		startCutscene(filePaths, onFinish, false);
	}

	@Override
	public void update() { }

	@Override
	public void draw(Graphics2D g2) {
		if (!cutsceneImages.isEmpty() && currentIndex < cutsceneImages.size()) {
			BufferedImage currentImg = cutsceneImages.get(currentIndex);
			g2.drawImage(currentImg, 0, 0, Settings.getGameWidth(), Settings.getGameHeight(), null);
		}
	}
	
	private void advanceCutscene() {
		game.advanceCutsceneWithFade(() -> {
			currentIndex++;
			if (currentIndex >= cutsceneImages.size()) {
				if (onCutsceneFinish != null) {
					onCutsceneFinish.run();
				}
			}
		});
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (escapeOnly) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				game.advanceCutsceneWithFade(() -> {
					if (onCutsceneFinish != null) {
						onCutsceneFinish.run();
					}
				});
			}
		} else {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_SPACE, KeyEvent.VK_ENTER, KeyEvent.VK_Z -> advanceCutscene();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) { }
	
}
