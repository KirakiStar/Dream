package entities;

import java.awt.Graphics2D;

import collision.Hitbox;

public abstract class Entity {
	protected float x, y;
	protected Hitbox hitbox;
	
	public Entity(float x, float y, int width, int height, float offsetX, float offsetY) {
		this.x = x;
		this.y = y;
		this.hitbox = new Hitbox(this, offsetX, offsetY, width, height);
	}
	
	protected void drawHitbox(Graphics2D g2) {
		hitbox.drawDebug(g2);
	}
	
	protected void updateHitbox() {
		hitbox.update(x, y);
	}
	
	public Hitbox getHitbox() { return hitbox; }
	public float getX() { return x; }
	public float getY() { return y; }
}
