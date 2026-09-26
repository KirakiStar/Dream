package entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import helper.ResourceLoader;
import collision.Hitbox;

public abstract class Entity {
	protected float x;
	protected float y;
	protected int width;
	protected int height;
	protected Hitbox hitbox;

	protected BufferedImage[][] sprites;
	protected int aniTick;
	protected int aniIndex;
	protected int aniSpeed = 15;
	protected boolean facingLeft = false;

	public Entity(float x, float y, int width, int height, float offsetX, float offsetY) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.hitbox = new Hitbox(this, offsetX, offsetY, width, height);
	}

	protected void updateAnimationTick(int maxAnimationAmount, boolean isLooping) {
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			if (isLooping) {
				aniIndex++;
				if (aniIndex >= maxAnimationAmount) {
					aniIndex = 0;
				}
			} else {
				if (aniIndex < maxAnimationAmount - 1) {
					aniIndex++;
				}
			}
		}
	}

	protected void loadAnimations(String resourcePath, int rows, int cols, int spriteWidth, int spriteHeight) {
		BufferedImage img = ResourceLoader.ImagesLoader(resourcePath);
		sprites = new BufferedImage[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				sprites[i][j] = img.getSubimage(j * spriteWidth, i * spriteHeight, spriteWidth, spriteHeight);
			}
		}
	}

	public void draw(Graphics2D g2, int actionId) {
		int drawX = (int) x;
		int drawY = (int) y;
		int drawWidth = (int) (64 * main.Game.SCALE);
		int drawHeight = (int) (64 * main.Game.SCALE);

		if (sprites != null && actionId < sprites.length && sprites[actionId] != null) {
			if (facingLeft) {
				g2.drawImage(sprites[actionId][aniIndex], drawX + drawWidth, drawY, -drawWidth, drawHeight, null);
			} else {
				g2.drawImage(sprites[actionId][aniIndex], drawX, drawY, drawWidth, drawHeight, null);
			}
		}
//		drawHitbox(g2);
	}

	public void update() {
		updateHitbox();
	}
	
	public abstract void draw(Graphics2D g2);
	protected final void drawHitbox(Graphics2D g2) { hitbox.drawDebug(g2); }
	protected final void updateHitbox() { hitbox.update(x, y); }

	public final Hitbox getHitbox() { return hitbox; }
	public final float getX() { return x; }
	public final float getY() { return y; }
	public final int getWidth() { return width; }
	public final int getHeight() { return height; }
}