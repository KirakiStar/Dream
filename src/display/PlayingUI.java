package display;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import helpers.ResourceLoader;
import main.Game;

public class PlayingUI implements GUI_Methods {
	private Playing playing;
	private BufferedImage[][] fontCharacters;

	public PlayingUI(Playing playing) {
		this.playing = playing;
		this.fontCharacters = ResourceLoader.fontCharacters;
	}

	@Override
	public void update() {
		
	}

	@Override
	public void draw(Graphics2D g2) {
		drawPlayerHealth(g2);
		drawCollectedDolls(g2);
		drawBossBar(g2);
	}
	
	private void drawPlayerHealth(Graphics2D g2) {
		int scale = (int) Game.SCALE;
		int x10 = 10 * scale;
		int x20 = x10 * 2;
		int x32 = 32 * scale;
		int x64 = x32 * 2;

		String hp = Integer.toString(playing.getPlayer().getCurrentHealth());
		boolean isHit = playing.getPlayer().isHit();
		
		// silver icon
		g2.drawImage(fontCharacters[5][2], x10, x20, x32, x32, null);

		BufferedImage heartIcon = isHit ? fontCharacters[2][1] : fontCharacters[2][0];
		BufferedImage[] hpAmount = isHit ? fontCharacters[1] : fontCharacters[0];

		g2.drawImage(heartIcon, x32, x10, x32, x32, null);

		for (int i = 0; i < hp.length(); i++) {
			int numIndex = hp.charAt(i) - '0';
			g2.drawImage(hpAmount[numIndex], x64 + (i * x20), x10, x32, x32, null);
		}
	}
	
	private void drawCollectedDolls(Graphics2D g2) {
		
	}

	private void drawBossBar(Graphics2D g2) {
		
	}
}
