package entities;

import java.util.List;
import java.awt.Graphics2D;

import main.Game;
import collision.Hitbox;
import collision.CollisionChecker;
import helper.ResourceLoader;
import helper.Constants.GoldyState;
import static helper.Constants.GoldyState.*;

public class Goldy extends Enemy implements Attackable {
	private static final ResourceLoader.SpriteSheet goldySprites = ResourceLoader.GOLDY_SPRITES;
	private GoldyState goldyState;
	
	private static final int WIDTH = (int)(16*Game.SCALE);
	private static final int HEIGHT = (int)(32*Game.SCALE);
	private static final float OFFSET_X = 24 * Game.SCALE;
	private static final float OFFSET_Y = 31 * Game.SCALE;
	
	private int tick = 0;
	
	private final float startX;
	private float maxWalkDistance = 50 * Game.SCALE;
	private final float walkSpeed = 0.5f * Game.SCALE;
	
	private Hitbox attackBox;
	private boolean attacking = false;
	private final int attackDamage = 1;
	private int attackInterval = 180;

	public Goldy(float x, float y) {
		super(x, y, WIDTH, HEIGHT, OFFSET_X, OFFSET_Y, 5, goldySprites); //maxHealth = 5
		this.startX = x;
		this.knockbackSpeed = 0.2f * Game.SCALE;
		this.goldyState = IDLE;
		setAttackBox();
	}
	
	private void setAttackBox() {
		float offsetX = 24f * Game.SCALE;
		float offsetY = 50f * Game.SCALE;
		int attackWidth = (int)(14 * Game.SCALE);
		int attackHeight = (int)(14 * Game.SCALE);
		this.attackBox = new Hitbox(this, offsetX, offsetY, attackWidth, attackHeight);
	}
	
	@Override
	public void update(List<Integer> collisionData, int levelWidth, int levelHeight) {
		super.update(collisionData, levelWidth, levelHeight);
		updateAnimationTick();
		updateAttackBox();
	}
	
	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2, goldyState.getId());
		attackBox.drawDebug(g2);
	}
	
	protected void updateAnimationTick() {
		super.updateAnimationTick(goldyState.getAnimationAmount(), goldyState.isLooping());
		
		if (attacking) {
			if (aniIndex >= goldyState.getAnimationAmount() - 1) {
				resetAttack();
			}
		}
		if (hit) {
			if (aniIndex >= goldyState.getAnimationAmount() - 1) {
				hit = false;
			}
		}
	}

	@Override
	protected void updateBehavior(List<Integer> collisionData, int levelWidth, int levelHeight) {
		tick++;
//		patrol(collisionData, levelWidth, levelHeight);
		if (tick >= attackInterval) {
			attack();
			tick = 0;
		}
	}
	
	private void patrol(List<Integer> cd, int lw, int lh) {
		float proposedXSpeed = facingLeft ? -walkSpeed : walkSpeed;
		float nextX = x + proposedXSpeed;

		if (Math.abs(nextX - startX) >= maxWalkDistance) {
			turnAround();
			return;
		}

		if (CollisionChecker.isSolid(hitbox, nextX, y, cd, lw, lh)) {
			turnAround();
			return;
		}

		float checkX = facingLeft ? x - 2.0f : x + width + 2.0f;
		float checkY = y + 1.0f;
		boolean solidGround = CollisionChecker.isSolid(hitbox, checkX, checkY, cd, lw, lh);
		boolean platformGround = CollisionChecker.isPlatform(hitbox, checkY, checkY, 1.0f, cd, lw, lh);

		if (!solidGround && !platformGround) {
			turnAround();
			return;
		}

		float slopeY = CollisionChecker.getSlopeY(hitbox, nextX, y, cd, lw, lh);
		if (slopeY != -1) {
			float targetY = slopeY - hitbox.getOffsetY() - hitbox.getHeight();
			if (Math.abs(targetY - y) > 1.0f) {
				turnAround();
				return;
			}
		}

		this.xSpeed = proposedXSpeed;
//		this.goldyState = WALKING;
	}
	
	private void turnAround() {
		facingLeft = !facingLeft;
		this.xSpeed = 0;
		this.goldyState = IDLE;
	}
	
	public void attack() {
		attacking = true;
		facingLeft = !facingLeft;
	}
	
	private void resetAttack() {
		attacking = false;
		facingLeft = !facingLeft;
	}
	
	private void updateAttackBox() {
		if (!facingLeft) {
			attackBox.update(x - (attackBox.getWidth()), y);
		} else {
			attackBox.update(x + (WIDTH), y);
		}
	}

	@Override public Hitbox getAttackBox() { return attackBox; }
	@Override public int getDamage() { return attackDamage; }
	@Override public boolean isAttacking() { return attacking; }
}
