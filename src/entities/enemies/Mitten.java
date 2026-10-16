package entities.enemies;

import java.util.List;
import java.util.Random;
import java.awt.Graphics2D;

import display.Camera;
import entities.player.Player;
import collision.CollisionChecker;
import helpers.Settings;
import helpers.ResourceLoader;
import helpers.EntityConstants.MittenState;
import static helpers.EntityConstants.MittenState.*;

public class Mitten extends Enemy {
	private MittenState mittenState;
	
	private static final int WIDTH = 16;
	private static final int HEIGHT = 32;
	private static final float OFFSET_X = 24f;
	private static final float OFFSET_Y = 31f;

	private int tick = 0;
	private final int randomAction;// 1:idle, 2:patrol, other:jump
	
	private final float startX;
	private float maxWalkDistance = 50 * Settings.SCALE;
	private final float walkSpeed = 0.3f * Settings.SCALE;
	
	private float jumpSpeed = -2f * Settings.SCALE;
	public static final int jumpInterval = 300;
	
	public Mitten(float x, float y, int UniqueTickOffset) {
		super(ResourceLoader.MITTEN_SPRITES, x, y, WIDTH, HEIGHT, OFFSET_X, OFFSET_Y, 3); //maxHealth = 2
		this.startX = x;
		mittenState = IDLE;
		tick = UniqueTickOffset;
		Random random = new Random();
		randomAction = random.nextInt(4);
	}
	
	@Override
	public void update(List<Integer> collisionData, int levelWidth, int levelHeight, Player player, Camera camera) {
		super.update(collisionData, levelWidth, levelHeight, player, camera);
		updateAnimationTick(mittenState.getAnimationAmount(), mittenState.isLooping());
	}
	
	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2, mittenState.getId());
	}

	@Override
	protected void updateBehavior(List<Integer> collisionData, int levelWidth, int levelHeight, Player player) {
		switch (randomAction) {
			case 1:
				facePlayer(player.getX());
				break;
			case 2:
				patrol(collisionData, levelWidth, levelHeight);
				break;
			default:
				tick++;
				facePlayer(player.getX());
				if (tick >= jumpInterval) {
					jump();
					tick = 0;
				} break;
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

		float checkX = facingLeft ? nextX - this.width : nextX + this.width;
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
//		this.mittenState = WALKING;
	}
	
	private void turnAround() {
		facingLeft = !facingLeft;
		this.xSpeed = 0;
		this.mittenState = IDLE;
	}
	
	private void jump() {
		if (!inAir) {
			inAir = true;
			airSpeed = jumpSpeed;
		}
	}
}
