package entities;

import java.awt.Graphics2D;
import java.util.List;

import main.Game;
import helper.ResourceLoader;
import gamestates.Playing;
import helper.Constants.PlayerState;
import collision.CollisionChecker;
import static main.Game.SCALE;
import static helper.Constants.PlayerState.*;

public class Player extends MovingEntity {
	private Playing playing;
	private final String playerPng = ResourceLoader.PLAYER_SPRITES;
	private final int pngRow = 11;
	private final int pngCol = 4;
	private PlayerState playerAction;
	
	private static final int WIDTH = (int)(16*Game.SCALE);
	private static final int HEIGHT = (int)(36*Game.SCALE);
	private static final float OFFSET_X = 24 * Game.SCALE;
	private static final float OFFSET_Y = 27 * Game.SCALE;
	
	private final float playerSpeed = 1.5f * SCALE;
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	private boolean jumping;
	private boolean climbing;
	
	private int jumpCount = 0;
	private final int maxJump = 2;
	private boolean climbable = false;
	private final float jumpSpeed = -2.3f * SCALE;

	public Player(Playing playing, float x, float y) {
		super(x, y, WIDTH, HEIGHT, OFFSET_X, OFFSET_Y);
		this.playing = playing;
		this.playerAction = IDLE;
		entitySpeed = playerSpeed;
		loadAnimations(playerPng, pngRow, pngCol, 64, 64);
	}
	
	protected void updateAnimationTick() {
		super.updateAnimationTick(playerAction.getAnimationAmount(), playerAction.isLooping());
	}
	
	private void setAnimation(PlayerState newAction) {
		if (this.playerAction.getId() != newAction.getId()) {
			this.playerAction = newAction;
			this.aniTick = 0;
			this.aniIndex = 0;
		}
	}
	
	private void updatePlayerAction() {
		if (inAir) {
			if (airSpeed < 0) {
				if (jumpCount < 2) {
					setAnimation(JUMPING);
				} else {
					setAnimation(JUMPING2);
				}
			} else {
				setAnimation(FALLING);
			}
		} else if (moving) {
			setAnimation(RUNNING);
		} else {
			setAnimation(IDLE);
		}
	}

	@Override
	public void update() {
		super.update();
		updatePosition();
		updateAnimationTick();
		updatePlayerAction();
	}

	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2, playerAction.getId());
	}

	private void updatePosition() {
		moving = false;

		List<Integer> cd = playing.getCollisionData();
		int lw = playing.getLevelWidth();
		int lh = playing.getLevelHeight();

		if (CollisionChecker.isWater(hitbox, cd, lw, lh)) {
			die();
			return;
		}

		climbable = CollisionChecker.isLadder(hitbox, cd, lw, lh);
		if (climbable && (up || down)) {
			climbing = true;
			inAir = false;
			airSpeed = 0;
		} else if (!climbable) {
			climbing = false;
		}

		if (climbing) {
			float xSpeed = 0;
			float ySpeed = 0;

			if (up) ySpeed -= playerSpeed;
			if (down) ySpeed += playerSpeed;
			if (left) { xSpeed -= playerSpeed; facingLeft = true; }
			if (right) { xSpeed += playerSpeed; facingLeft = false; }

			if (ySpeed != 0 && !CollisionChecker.isSolid(hitbox, x, y + ySpeed, cd, lw, lh)) {
				y += ySpeed;
				moving = true;
			}
			if (xSpeed != 0) {
				updateXPos(xSpeed, false, cd, lw, lh);
			}

			if (!CollisionChecker.isLadder(hitbox, cd, lw, lh)) {
				climbing = false;
				inAir = false;
				airSpeed = 0;
				jumpCount = 0;
			}
			return;
		}

		float xSpeed = 0;

		if (jumping) jump();
		if (left) { xSpeed -= playerSpeed; facingLeft = true; }
		if (right) { xSpeed += playerSpeed; facingLeft = false; }

		float slopeFloorY = CollisionChecker.getSlopeY(hitbox, x + xSpeed, y, cd, lw, lh);
		
		if (xSpeed != 0) {
			updateXPos(xSpeed, slopeFloorY != -1, cd, lw, lh);
		}

		if (slopeFloorY != -1 && airSpeed >= 0) {
			y = slopeFloorY - hitbox.getOffsetY() - hitbox.getHeight();
			inAir = false;
			airSpeed = 0;
			jumpCount = 0;
		} else if (!inAir) {
			boolean solidGround = CollisionChecker.isSolid(hitbox, x, y + 1.0f, cd, lw, lh);
			boolean platformGround = CollisionChecker.isPlatform(hitbox, y, y + 1.0f, 1.0f, cd, lw, lh);

			if (down && platformGround && !solidGround) {
				inAir = true;
				y += 3.0f;
				airSpeed = fallSpeed;
			} else if (!solidGround && !platformGround) {
				inAir = true;
				jumpCount = 1;
			}
		}

		if (inAir) {
			updateYPos(airSpeed, cd, lw, lh);
		}
	}
	
	private void jump() {
		if (jumpCount < maxJump) {
			jumpCount++;
			inAir = true;
			airSpeed = jumpSpeed;
		}
		jumping = false;
	}
	
	@Override
	protected void resetInAir() {
		super.resetInAir();
		jumpCount = 0;
	}

	public void resetDirection() {
		left = false;
		right = false;
		up = false;
		down = false;
	}
	
	private void die() {
		System.out.println("Dead");
	}

	public void setLeft(boolean left) { this.left = left; }
	public boolean isLeft() { return left; }

	public void setRight(boolean right) {  this.right = right; }
	public boolean isRight() {  return right; }

	public void setUp(boolean up) { this.up = up; }
	public boolean isUp() { return up; }

	public void setDown(boolean down) { this.down = down; }
	public boolean isDown() { return down; }

	public void setJump(boolean jump) { this.jumping = jump; }
	public boolean isJump() { return jumping; }
}
