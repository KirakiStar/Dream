package entities;

import java.util.List;
import java.awt.Graphics2D;

import helper.ResourceLoader;

public abstract class Enemy extends MovingEntity {
	public Enemy(float x, float y, int width, int height, float offsetX, float offsetY, int maxHealth, ResourceLoader.SpriteSheet enemySprites) {
		super(x, y, width, height, offsetX, offsetY, maxHealth);
		facingLeft = true;
		loadAnimations(enemySprites);
	}

	public void update(List<Integer> collisionData, int levelWidth, int levelHeight) {
		super.update();
		
		if (!inAir) {
			boolean solidGround = collision.CollisionChecker.isSolid(hitbox, x, y + 1.0f, collisionData, levelWidth, levelHeight);
			boolean platformGround = collision.CollisionChecker.isPlatform(hitbox, y, y + 1.0f, 1.0f, collisionData, levelWidth, levelHeight);
			if (!solidGround && !platformGround) {
				inAir = true;
			}
		}

		if (inAir) {
			updateYPos(airSpeed, collisionData, levelWidth, levelHeight);
		}

		updateBehavior(collisionData, levelWidth, levelHeight);
	}
	
	@Override
	public void draw(Graphics2D g2, int entityState) {
		super.draw(g2, entityState);
		hitbox.drawDebug(g2);
	}
	
	@Override
	public void takeDamage(int amount) {
		super.takeDamage(amount);
		System.out.println("Enemy hit");
	}

	protected abstract void updateBehavior(List<Integer> collisionData, int levelWidth, int levelHeight);
}
