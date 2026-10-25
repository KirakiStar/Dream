package display;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import gamestates.TitleScreen;
import helpers.Settings;
import helpers.ResourceLoader;
import helpers.ResourceLoader.SpriteSheet;
import static helpers.ScalarValues.*;

public class TitleUI implements GUI_Methods {
	private final TitleScreen title;
	private final SpriteSheet spriteSheet = ResourceLoader.TS_BUTTONS;
	
	private final BufferedImage[][] fontCharacters;
	
	private final BufferedImage[][] buttons;
	private final int buttonWidth = spriteSheet.getWidth();
	private final int buttonHeight = spriteSheet.getHeight();
	
	private final int screenWidth = Settings.getGameWidth();
	private final int screenHeight = Settings.getGameHeight();
	
	private int blinkTick = 0;
	private final int blinkSpeed = 5;
	private final int tickBeforeStart = 60;
	private boolean starting = false;
	
	private boolean pressingLeft = false;
	private boolean pressingRight = false;
	
	public TitleUI(TitleScreen title) {
		this.title = title;
		this.fontCharacters = ResourceLoader.fontCharacters;
		this.buttons = ResourceLoader.loadSpriteSheet(spriteSheet);
	}

	@Override
	public void update() {
		if (starting) {
			blinkTick++;
			if (blinkTick >= tickBeforeStart) {
				title.getGame().resetPlaying();
				title.getGame().changeStateWithFade(gamestates.Gamestate.PLAYING, 0.05f);
				starting = false;
				blinkTick = 0;
			}
		}
	}

	@Override
	public void draw(Graphics2D g2) {
		drawStart(g2);
		drawSettings(g2);
	}
	
	private void drawStart(Graphics2D g2) {
		int drawWidth = (int)(buttonWidth  * 1.5f * Settings.getCurrentScale());
		int drawHeight = (int)(buttonHeight  * 1.5f * Settings.getCurrentScale());
		g2.drawImage(buttons[0][0], getX10(), getX32(), drawWidth, drawHeight, null);
		
		if ((blinkTick / blinkSpeed) % 2 == 1) {
			g2.drawImage(buttons[1][0], getX10(), getX32(), drawWidth, drawHeight, null);
		}
	}
	
	private void drawSettings(Graphics2D g2) {
		int drawWidth = (int)(buttonWidth * Settings.getCurrentScale());
		int drawHeight = (int)(buttonHeight * Settings.getCurrentScale());
		g2.drawImage(buttons[2][0], getX32(), getX128() + getX32(), drawWidth, drawHeight, null);
		
		int scaleIndex = Settings.getScaleIndex() + 1;
		g2.drawImage(fontCharacters[1][scaleIndex], getX20() * 4, getX128() + getX32(), getX32(), getX32(), null);
		
		if (pressingLeft)
			g2.drawImage(buttons[3][0], getX32(), getX128() + getX32(), drawWidth, drawHeight, null);
		if (pressingRight)
			g2.drawImage(buttons[4][0], getX32(), getX128() + getX32(), drawWidth, drawHeight, null);
	}
	
	public void triggerStart() {
		if (!starting) {
			starting = true;
			blinkTick = 0;
		}
	}
	
	public void setPressingLeft(boolean pressing) { this.pressingLeft = pressing; }
	public void setPressingRight(boolean pressing) { this.pressingRight = pressing; }
	public boolean isStarting() { return starting; }
}
