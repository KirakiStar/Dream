package entities;

import java.awt.Graphics2D;

import collision.Hitbox;

public abstract class Entity {
	protected float x;
	protected float y;
	protected int width;
	protected int height;
	protected Hitbox hitbox;
	
	public Entity(float x, float y, int width, int height, float offsetX, float offsetY) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.hitbox = new Hitbox(this, offsetX, offsetY, width, height);
	}
	
	protected final void drawHitbox(Graphics2D g2) { hitbox.drawDebug(g2); }
	protected final void updateHitbox() { hitbox.update(x, y); }
	
	public final Hitbox getHitbox() { return hitbox; }
	public final float getX() { return x; }
	public final float getY() { return y; }
	public final int getWidth() { return width; }
	public final int getHeight() { return height; }
	
	public abstract void update();
	public abstract void draw(Graphics2D g2);
	protected abstract void loadAnimations();
	protected abstract void updateAnimationTick();
}
