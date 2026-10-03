package entities;

import java.util.List;
import java.awt.Graphics2D;

import main.Game;
import collision.CollisionChecker;
import inputsAndResourceLoader.ResourceLoader;
import entities.EntityConstants.MittenState;
import static entities.EntityConstants.MittenState.*;

public class Mitten extends Enemy {
	private static final ResourceLoader.SpriteSheet mittenSprites = ResourceLoader.MITTEN_SPRITES;
	private MittenState mittenState;
	
	private static final int WIDTH = (int)(16*Game.SCALE);
	private static final int HEIGHT = (int)(32*Game.SCALE);
	private static final float OFFSET_X = 24 * Game.SCALE;
	private static final float OFFSET_Y = 31 * Game.SCALE;

	private int tick = 0;
	private final int randomAction;// 1:idle, 2:patrol, other:jump
	
	private final float startX;
	private float maxWalkDistance = 50 * Game.SCALE;
	private final float walkSpeed = 0.3f * Game.SCALE;
	
	private float jumpSpeed = -2f * Game.SCALE;
	public static final int jumpInterval = 300;
	
	public Mitten(float x, float y, int UniqueTickOffset) {
		super(x, y, WIDTH, HEIGHT, OFFSET_X, OFFSET_Y, 3, mittenSprites); //maxHealth = 2
		this.startX = x;
		mittenState = IDLE;
		tick = UniqueTickOffset;
		randomAction = (int)(Math.random() * 4);
	}
	
	@Override
	public void update(List<Integer> collisionData, int levelWidth, int levelHeight, Player player) {
		super.update(collisionData, levelWidth, levelHeight, player);
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
				return;
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
//		this.mittenState = WALKING;
	}
	
	private void turnAround() {
		facingLeft = !facingLeft;
		this.xSpeed = 0;
		this.mittenState = IDLE;
	}
	
	private void facePlayer(float playerX) {
		facingLeft = (playerX < this.x);
	}
	
	private void jump() {
		if (!inAir) {
			inAir = true;
			airSpeed = jumpSpeed;
		}
	}
}
