package entities;

import java.util.List;
import java.awt.Graphics2D;

import inputsAndResourceLoader.ResourceLoader;
import collision.CollisionChecker;
import main.Game;

public abstract class Enemy extends MovingEntity {
	protected boolean hit = false;
	protected int hitDuration = 15;
	protected int hitTick = 0;
	protected float knockbackSpeed = 0.5f * Game.SCALE;
	protected float knockbackHeight = -0.5f * Game.SCALE;
	protected int knockbackDir = 1; // 1: right, -1: left
	protected float xSpeed = 0;
	
	public Enemy(float x, float y, int width, int height, float offsetX, float offsetY, int maxHealth, ResourceLoader.SpriteSheet enemySprites) {
		super(x, y, width, height, offsetX, offsetY, maxHealth);
		facingLeft = true;
		loadAnimations(enemySprites);
	}

	public void update(List<Integer> collisionData, int levelWidth, int levelHeight, Player player) {
		super.update();
		
		if (CollisionChecker.isWater(hitbox, collisionData, levelWidth, levelHeight)) {
			die();
			return;
		}

		xSpeed = 0;
		if (hit) {
			hitTick++;
			xSpeed = knockbackDir * knockbackSpeed;
			if (hitTick >= hitDuration) {
				hit = false;
				hitTick = 0;
			}
		} else {
			updateBehavior(collisionData, levelWidth, levelHeight, player);
		}

		updateGroundPosition(xSpeed, collisionData, levelWidth, levelHeight, false);
	}
	
	@Override
	public void draw(Graphics2D g2, int entityState) {
		super.draw(g2, entityState);
		hitbox.drawDebug(g2);
	}
	
	@Override
    protected void updateXPos(float xSpeed, boolean candidateIsSlope, List<Integer> cd, int lw, int lh) {
        float nextX = x + xSpeed;
        float mapRightX = (lw * Game.TILES_SIZE) - width;

        if (nextX < 0 || nextX > mapRightX) {
            moving = false;
            return;
        }

        super.updateXPos(xSpeed, candidateIsSlope, cd, lw, lh);
    }
	
	public void takeDamage(int amount, float playerX) {
		if (!alive || hitbox.isInvincible()) return;
		
		this.knockbackDir = (playerX < this.x) ? 1 : -1;
		
		super.takeDamage(amount);
		hit = true;
		hitTick = 0;
		hitbox.setInvincible(true);
		
		if (!inAir) {
			inAir = true;
			airSpeed = knockbackHeight;
		}
//		System.out.println("Enemy hit");
	}

	protected abstract void updateBehavior(List<Integer> collisionData, int levelWidth, int levelHeight, Player player);
}
