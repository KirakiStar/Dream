package display;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import entities.bosses.Boss;
import helpers.Settings;
import helpers.ResourceLoader;
import helpers.BossesKillStatus;
import static helpers.ScalarValues.*;

public class PlayingUI implements GUI_Methods {
	private final Playing playing;
	
	private final BufferedImage[][] fontCharacters;
	
	private final int screenWidth = Settings.getGameWidth();
	private final int screenHeight = Settings.getGameHeight();

	public PlayingUI(Playing playing) {
		this.playing = playing;
		this.fontCharacters = ResourceLoader.fontCharacters;
	}

	@Override
	public void update() { }

	@Override
	public void draw(Graphics2D g2) {
		drawPlayerStats(g2);
		drawPlayerScore(g2);
		drawBossHealth(g2);
	}
	
	private void drawPlayerStats(Graphics2D g2) {
		String hp = Integer.toString(playing.getPlayer().getCurrentHealth());
		boolean isHit = playing.getPlayer().isHit();
		
		// silver icon
		g2.drawImage(fontCharacters[5][3], getX10(), getX20(), getX32(), getX32(), null);

		BufferedImage heartIcon = isHit ? fontCharacters[2][2] : fontCharacters[2][1];
		BufferedImage[] hpAmount = isHit ? fontCharacters[0] : fontCharacters[1];

		g2.drawImage(heartIcon, getX32(), getX10(), getX32(), getX32(), null);

		for (int i = 0; i < hp.length(); i++) {
			int numIndex = hp.charAt(i) - '0';
			g2.drawImage(hpAmount[numIndex], getX64() + (i * getX20()), getX10(), getX32(), getX32(), null);
		}
		
		int attackDamage = playing.getPlayer().getDamage();
		for (int i = 0; i < attackDamage; i++) {
			// attack icon
			g2.drawImage(fontCharacters[2][6], getX32() + (i * getX10()), getX32(), getX16(), getX16(), null);
		}
	}
	
	private void drawPlayerScore(Graphics2D g2) {
		for (int i = 0; i < 4; i++) {
			if (BossesKillStatus.getBossAtId(i).isKilled())
				g2.drawImage(fontCharacters[5][4+i], getX10() + i * getX32(), screenHeight - getX32(), getX32(), getX32(), null);
		}
	}

	private void drawBossHealth(Graphics2D g2) {
		Boss boss = playing.getActiveBoss();
		if (boss == null || !playing.getCamera().isLocked()) return;
		
		String hp = Integer.toString(playing.getActiveBoss().getCurrentHealth());
		boolean isHit = playing.getActiveBoss().isHit();
		
		int startXPos = screenWidth - getX128() + getX20();
		int yPos = screenHeight - getX32() - getX10();
		
		// boss icon
		g2.drawImage(fontCharacters[5][boss.getBossId().getIconCol()], startXPos + getX10(), yPos, getX32(), getX32(), null);
		
		BufferedImage heartIcon = isHit ? fontCharacters[2][2] : fontCharacters[2][1];
		BufferedImage[] hpAmount = isHit ? fontCharacters[0] : fontCharacters[1];
		
		g2.drawImage(heartIcon, startXPos + getX32(), screenHeight - getX32(), getX32(), getX32(), null);

		startXPos += getX64() - getX10();
		for (int i = 0; i < hp.length(); i++) {
			int numIndex = hp.charAt(i) - '0';
			g2.drawImage(hpAmount[numIndex], startXPos + (i * getX20()), yPos, getX32(), getX32(), null);
		}
	}
}
