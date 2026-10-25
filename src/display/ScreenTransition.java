package display;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;

import helpers.ResourceLoader;
import helpers.Settings;

public class ScreenTransition {
	private final BufferedImage blackImg;
	private float alpha = 0f;
	private final float fadeSpeed = 0.2f;
	private boolean fadingOut = false;
	private boolean fadingIn = false;
	private Runnable onFadeComplete;

	public ScreenTransition(String fileName) {
		this.blackImg = ResourceLoader.ImagesLoader(fileName);
	}

	public void startTransition(Runnable onComplete) {
		this.fadingOut = true;
		this.fadingIn = false;
		this.onFadeComplete = onComplete;
	}

	public void update() {
		if (fadingOut) {
			alpha += fadeSpeed;
			if (alpha >= 1f) {
				alpha = 1f;
				fadingOut = false;
				if (onFadeComplete != null) {
					onFadeComplete.run();
				}
				fadingIn = true;
			}
		} else if (fadingIn) {
			alpha -= fadeSpeed;
			if (alpha <= 0f) {
				alpha = 0f;
				fadingIn = false;
			}
		}
	}

	public void draw(Graphics2D g2) {
		if (alpha <= 0f) return;

		java.awt.Composite oldComposite = g2.getComposite();
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

		g2.drawImage(blackImg, 0, 0, Settings.getGameWidth(), Settings.getGameHeight(), null);

		g2.setComposite(oldComposite);
	}

	public boolean isTransitioning() { return fadingOut || fadingIn; }
}