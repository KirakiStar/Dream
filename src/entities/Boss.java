package entities;

import java.util.List;
import java.awt.Graphics2D;

import display.Camera;
import collision.CollisionChecker;
import helpers.ResourceLoader;

public abstract class Boss extends MovingEntity implements Attackable {
	protected boolean hit = false;
	protected float xSpeed = 0;

	public Boss(ResourceLoader.SpriteSheet entitySprites, float x, float y, int width, int height, float offsetX, float offsetY, int maxHealth) {
		super(entitySprites, x, y, width, height, offsetX, offsetY, maxHealth);
	}
	
	public void update(List<Integer> collisionData, int levelWidth, int levelHeight, Player player, Camera camera) {
		super.update();
		
		if (CollisionChecker.isWater(hitbox, collisionData, levelWidth, levelHeight)) {
			die();
			return;
		}

		xSpeed = 0;
		updateBossBehavior(collisionData, levelWidth, levelHeight, player);

		updateGroundPosition(xSpeed, collisionData, levelWidth, levelHeight, false, camera);
	}
	
	@Override
	public void draw(Graphics2D g2, int entityState) {
		super.draw(g2, entityState);
		hitbox.drawDebug(g2);
	}
	
	protected abstract void updateBossBehavior(List<Integer> collisionData, int levelWidth, int levelHeight, Player player);
	protected abstract void onDeath();
}
