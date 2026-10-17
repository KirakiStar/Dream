package display;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import helpers.Settings;
import helpers.ResourceLoader;
import helpers.BossesKillStatus;

public class PlayingUI implements GUI_Methods {
	private Playing playing;
	
	private BufferedImage[][] fontCharacters;
//	private final int imgRow = 9;
//	private final int imgCol = 10;
	
	private final int screenWidth = Settings.GAME_WIDTH;
	private final int screenHeight = Settings.GAME_HEIGHT;
	
	private final int scale = (int) Settings.SCALE;
	private final int x10 = 10 * scale;
	private final int x20 = x10 * 2;
	private final int x16 = 16 * scale;
	private final int x32 = x16 * 2;
	private final int x64 = x32 * 2;

	public PlayingUI(Playing playing) {
		this.playing = playing;
		this.fontCharacters = ResourceLoader.fontCharacters;
	}

	@Override
	public void update() {
		
	}

	@Override
	public void draw(Graphics2D g2) {
		drawPlayerStats(g2);
		drawPlayerScore(g2);
		drawCollectedDolls(g2);
		drawBossBar(g2);
	}
	
	private void drawPlayerStats(Graphics2D g2) {
		String hp = Integer.toString(playing.getPlayer().getCurrentHealth());
		boolean isHit = playing.getPlayer().isHit();
		
		// silver icon
		g2.drawImage(fontCharacters[5][3], x10, x20, x32, x32, null);

		BufferedImage heartIcon = isHit ? fontCharacters[2][2] : fontCharacters[2][1];
		BufferedImage[] hpAmount = isHit ? fontCharacters[0] : fontCharacters[1];

		g2.drawImage(heartIcon, x32, x10, x32, x32, null);

		for (int i = 0; i < hp.length(); i++) {
			int numIndex = hp.charAt(i) - '0';
			g2.drawImage(hpAmount[numIndex], x64 + (i * x20), x10, x32, x32, null);
		}
		
		int attackDamage = playing.getPlayer().getDamage();
		for (int i = 0; i < attackDamage; i++) {
			// attack icon
			g2.drawImage(fontCharacters[2][6], x32 + (i * x10), x32, x16, x16, null);
		}
	}
	
	private void drawPlayerScore(Graphics2D g2) {
		for (int i = 0; i < 4; i++) {
			if (BossesKillStatus.getBossAtId(i).isKilled())
				g2.drawImage(fontCharacters[5][4+i], x10 + i * x32, screenHeight - x32, x32, x32, null);
		}
//		g2.drawImage(fontCharacters[5][4], x10, screenHeight - x32, x32, x32, null);
//		g2.drawImage(fontCharacters[5][5], x10 + x32, screenHeight - x32, x32, x32, null);
//		g2.drawImage(fontCharacters[5][6], x10 + x64, screenHeight - x32, x32, x32, null);
//		g2.drawImage(fontCharacters[5][7], x10 + x64 + x32, screenHeight - x32, x32, x32, null);
	}
	
	private void drawCollectedDolls(Graphics2D g2) {
		
	}

	private void drawBossBar(Graphics2D g2) {
		
	}
}
