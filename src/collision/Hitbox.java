package collision;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import entities.Entity;

public class Hitbox {
    private Entity owner;
    private float offsetX, offsetY;
    private int width, height;
    private Rectangle bounds;
    private boolean invincible = false;
    private boolean isTrigger = false;

    public Hitbox(Entity owner, float offsetX, float offsetY, int width, int height) {
        this.owner = owner;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.bounds = new Rectangle(
            (int) (owner.getX() + offsetX),
            (int) (owner.getY() + offsetY),
            width, height
        );
    }

    public void update(float entityX, float entityY) {
        bounds.x = (int) (entityX + offsetX);
        bounds.y = (int) (entityY + offsetY);
    }

    public boolean intersects(Hitbox other) {
        if (this.invincible || other.invincible)
			return false;
        return this.bounds.intersects(other.getBounds());
    }

    public void drawDebug(Graphics2D g2) {
        if (invincible) return;
        g2.setColor(isTrigger? Color.YELLOW : Color.MAGENTA);
        g2.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public Rectangle getBounds() { return bounds; }
    public Entity getOwner() { return owner; }
    public boolean isInvincible() { return invincible; }
    public void setInvincible(boolean invincible) { this.invincible = invincible; }
    public boolean isTrigger() { return isTrigger; }
    public void setTrigger(boolean isTrigger) { this.isTrigger = isTrigger; }

	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public float getOffsetX() { return offsetX; }
	public float getOffsetY() { return offsetY; }
}