package entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import collision.Hitbox;
import helper.ResourceLoader;
import helper.ResourceLoader.SpriteSheet;

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
	
	protected int maxHealth;
	protected int currentHealth;
	protected boolean alive = true;
	protected int invincibilityTick = 0;
	protected int invincibilityDuration = 30;

	public Entity(float x, float y, int width, int height, float offsetX, float offsetY, int maxHealth) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.hitbox = new Hitbox(this, offsetX, offsetY, width, height);
		initHealth(maxHealth);
	}
	
	protected void initHealth(int maxHealth) {
		this.maxHealth = maxHealth;
		this.currentHealth = maxHealth;
	}

	protected void loadAnimations(SpriteSheet spriteSheet) {
		String fileName = spriteSheet.getFileName();
		int rows = spriteSheet.getRow();
		int columns = spriteSheet.getColumn();
		int spriteWidth = spriteSheet.getWidth();
		int spriteHeight = spriteSheet.getHeight();
		BufferedImage img = ResourceLoader.ImagesLoader(fileName);
		sprites = new BufferedImage[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				sprites[i][j] = img.getSubimage(j * spriteWidth, i * spriteHeight, spriteWidth, spriteHeight);
			}
		}
	}
	
	public void takeDamage(int amount) {
		if (!alive || hitbox.isInvincible()) return;
		
		hitbox.setInvincible(true);
		invincibilityTick = 0;

		currentHealth -= amount;
		if (currentHealth <= 0) {
			currentHealth = 0;
			die();
		}
	}
	
	private void updateInvincibility() {
		if (hitbox.isInvincible()) {
			invincibilityTick++;
			if (invincibilityTick >= invincibilityDuration) {
				hitbox.setInvincible(false);
				invincibilityTick = 0;
			}
		}
	}
	
	protected void heal(int amount) {
		if (!alive) return;
		currentHealth = Math.min(currentHealth + amount, maxHealth);
	}
	
	protected void die() {
		if (!alive) return;
		alive = false;
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

	public void draw(Graphics2D g2, int actionId) {
		if (hitbox.isInvincible()) {
			int blinkInterval = 7;
			if ((invincibilityTick / blinkInterval) % 2 != 0) {
				return;
			}
		}
		
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
		updateInvincibility();
	}
	
	public abstract void draw(Graphics2D g2);
	protected final void drawHitbox(Graphics2D g2) { hitbox.drawDebug(g2); }
	protected final void updateHitbox() { hitbox.update(x, y); }

	public final Hitbox getHitbox() { return hitbox; }
	public final float getX() { return x; }
	public final float getY() { return y; }
	public final int getWidth() { return width; }
	public final int getHeight() { return height; }
	public boolean isAlive() { return alive; }
}