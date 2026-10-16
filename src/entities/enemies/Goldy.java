package entities.enemies;

import entities.player.Player;
import java.util.List;
import java.awt.Graphics2D;

import display.Camera;
import collision.Hitbox;
import collision.CollisionChecker;
import entities.Attackable;
import helpers.Settings;
import helpers.ResourceLoader;
import helpers.EntityConstants.GoldyState;
import static helpers.EntityConstants.GoldyState.*;

public class Goldy extends Enemy implements Attackable {
	private GoldyState goldyState;
	
	private static final int WIDTH = 16;
	private static final int HEIGHT = 32;
	private static final float OFFSET_X = 24f;
	private static final float OFFSET_Y = 31f;
	
	private int tick = 0;
	
	private final float startX;
	private float maxWalkDistance = 50 * Settings.SCALE;
	private final float walkSpeed = 0.5f * Settings.SCALE;
	
	private Hitbox attackBox;
	private boolean attacking = false;
	private final int attackDamage = 1;
	private final float attackRange = 64f * Settings.SCALE;
	public static final int attackInterval = 180;

	public Goldy(float x, float y, int UniqueTickOffset) {
		super(ResourceLoader.GOLDY_SPRITES, x, y, WIDTH, HEIGHT, OFFSET_X, OFFSET_Y, 5); //maxHealth = 5
		this.startX = x;
		this.knockbackSpeed = 0.2f * Settings.SCALE;
		tick = UniqueTickOffset;
		this.goldyState = IDLE;
		setAttackBox();
	}
	
	private void setAttackBox() {
		float offsetX = 24f;
		float offsetY = 50f;
		int attackWidth = 14;
		int attackHeight = 14;
		this.attackBox = new Hitbox(this, offsetX, offsetY, attackWidth, attackHeight);
	}
	
	@Override
	public void update(List<Integer> collisionData, int levelWidth, int levelHeight, Player player, Camera camera) {
		super.update(collisionData, levelWidth, levelHeight, player, camera);
		updateAttackBox();
		updateAnimationTick();
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
			attacking = false;
			hit = false;
//			if (aniIndex >= goldyState.getAnimationAmount() - 1) {
//			
//			}
		}
	}

	@Override
	protected void updateBehavior(List<Integer> collisionData, int levelWidth, int levelHeight, Player player) {
		if (!attacking && !hit) {
			if (nearPlayer(player.getX(), player.getY())) {
				tick++;
				facePlayer(player.getX());
				if (tick >= attackInterval) {
					attack();
					tick = 0;
				}
			} else {
				patrol(collisionData, levelWidth, levelHeight);
			}
		}
	}
	
	private void patrol(List<Integer> cd, int lw, int lh) {
		float trueXSpeed = facingLeft ? -walkSpeed : walkSpeed;
		float nextX = x + trueXSpeed;

		if (Math.abs(nextX - startX) >= maxWalkDistance) {
			turnAround();
			return;
		}

		if (CollisionChecker.isSolid(hitbox, nextX, y, cd, lw, lh)) {
			turnAround();
			return;
		}

		float checkX = facingLeft ? nextX - width : nextX + width;
		float checkY = y + 1.0f;
		boolean solidGround = CollisionChecker.isSolid(hitbox, checkX, checkY, cd, lw, lh);
		boolean platformGround = CollisionChecker.isPlatform(hitbox, checkX, y, checkY, 1.0f, cd, lw, lh);

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

		this.xSpeed = trueXSpeed;
//		this.goldyState = WALKING;
	}
	
	private void turnAround() {
		facingLeft = !facingLeft;
		this.xSpeed = 0;
		this.goldyState = IDLE;
	}
	
	private boolean nearPlayer(float playerX, float playerY) {
		if (Math.abs(playerY - this.y) > attackRange) return false;
		return Math.abs(playerX - this.x) <= attackRange;
	}
	
	@Override
	public void attack() {
		attacking = true;
		facingLeft = !facingLeft;
	}
	
	private void resetAttack() {
		attacking = false;
		facingLeft = !facingLeft;
		aniIndex = 0;
	}
	
	private void updateAttackBox() {
		if (!facingLeft) {
			attackBox.update(x - (attackBox.getWidth()), y);
		} else {
			attackBox.update(x + this.width, y);
		}
	}

	@Override public Hitbox getAttackBox() { return attackBox; }
	@Override public int getDamage() { return attackDamage; }
	@Override public boolean isAttacking() { return attacking; }
}
