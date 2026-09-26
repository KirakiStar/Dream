package entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import main.Game;
import helper.ResourceLoader;
import gamestates.Playing;
import helper.Constants.PlayerState;
import collision.CollisionChecker;
import static main.Game.SCALE;
import static helper.Constants.PlayerState.*;

public class Player extends Entity {
	private Playing playing;
	private BufferedImage[][] sprites;
	private final String playerPng = ResourceLoader.PLAYER_SPRITES;
	private final int pngRow = 11;
	private final int pngCol = 4;
	private PlayerState playerAction;
	private int aniTick;
	private int aniIndex;
	private final int aniSpeed = 15;
	private boolean facingLeft = false;
	
	private static final int WIDTH = (int)(16*Game.SCALE);
	private static final int HEIGHT = (int)(36*Game.SCALE);
	private static final float OFFSET_X = 24 * Game.SCALE;
	private static final float OFFSET_Y = 27 * Game.SCALE;
	
	private boolean moving = false;
	private final float playerSpeed = 1.5f * SCALE;
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	private boolean jumping;
	private boolean climbing;
	
	private boolean inAir = false;
	private int jumpCount = 0;
	private final int maxJump = 2;
	private boolean climbable = false;
	private final float gravity = 0.05f * SCALE;
	private float airSpeed = 0f;
	private final float jumpSpeed = -2.3f * SCALE;
	private final float fallSpeed = 0.5f * SCALE;

	public Player(Playing playing, float x, float y) {
		super(x, y, WIDTH, HEIGHT, OFFSET_X, OFFSET_Y);
		this.playing = playing;
		this.playerAction = IDLE;
		loadAnimations();
	}

	@Override
	protected void loadAnimations() {
		BufferedImage img = ResourceLoader.ImagesLoader(playerPng);
		
		sprites = new BufferedImage[pngRow][pngCol];
		for(int i = 0; i < pngRow; i++) {
			for(int j = 0; j < pngCol; j++) {
				sprites[i][j] = img.getSubimage(j*64, i*64, 64, 64);
			}
		}
	}
	
	@Override
	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;

			if (playerAction.isLooping()) {
				aniIndex++;
				if (aniIndex >= playerAction.getAnimationAmount()) {
					aniIndex = 0;
				}
			} else {
				if (aniIndex < playerAction.getAnimationAmount() - 1) {
					aniIndex++;
				}
			}
		}
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
		updatePosition();
		updateHitbox();
		updateAnimationTick();
		updatePlayerAction();
	}

	@Override
	public void draw(Graphics2D g2) {
		int drawX = (int) x;
		int drawY = (int) y;
		int drawWidth = (int) (64 * SCALE);
		int drawHeight = (int) (64 * SCALE);
		if (facingLeft) {
			g2.drawImage(sprites[playerAction.getId()][aniIndex], drawX+drawWidth, drawY,
					-drawWidth, drawHeight, null);
		} else {
			g2.drawImage(sprites[playerAction.getId()][aniIndex], drawX, drawY,
					drawWidth, drawHeight, null);
		}
//		drawHitbox(g2);
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

			// Dismount when climbing past the top of the ladder
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

	private void updateXPos(float xSpeed, boolean candidateIsSlope, List<Integer> cd, int lw, int lh) {
		if (!CollisionChecker.isSolid(hitbox, x + xSpeed, y, cd, lw, lh)) {
			this.x += xSpeed;
			moving = true;
		} 
		
		else if (candidateIsSlope && !CollisionChecker.isSolid(hitbox, x + xSpeed, y - (4.0f * Game.SCALE), cd, lw, lh)) {
			this.x += xSpeed;
			moving = true;
		}
	}

	private void updateYPos(float ySpeed, List<Integer> cd, int lw, int lh) {
		boolean solidHit = CollisionChecker.isSolid(hitbox, x, y + ySpeed, cd, lw, lh);
		boolean platformHit = CollisionChecker.isPlatform(hitbox, y, y + ySpeed, ySpeed, cd, lw, lh);

		if (!solidHit && !platformHit) {
			this.y += ySpeed;
			if (inAir) {
				airSpeed += gravity;
			}
			moving = true;
		} else {
			this.y = CollisionChecker.GetYFromBlocks(hitbox, y + ySpeed, airSpeed);

			if (airSpeed > 0) {
				resetInAir();
			} else {
				airSpeed = fallSpeed;
			}
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
	
	private void resetInAir() {
		inAir = false;
		jumpCount = 0;
		airSpeed = 0;
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
