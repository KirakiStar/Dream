package entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.Game;
import helper.ResourceLoader;
import gamestates.Playing;
import helper.Constants.PlayerState;
import static main.Game.SCALE;
import static collision.CollisionChecker.isSolid;
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
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	private boolean jump;
	private final float playerSpeed = 1.5f * SCALE;
	
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

	private void loadAnimations() {
		BufferedImage img = ResourceLoader.ImagesLoader(playerPng);

		sprites = new BufferedImage[pngRow][pngCol];
		for(int i = 0; i < pngRow; i++) {
			for(int j = 0; j < pngCol; j++) {
				sprites[i][j] = img.getSubimage(j*64, i*64, 64, 64);
			}
		}
	}
	
	private void updateAnimationTick() {
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

	public void update() {
		updatePosition();
		updateHitbox();
		updateAnimationTick();
		updatePlayerAction();
	}

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

		float xSpeed = 0;
		
		if (jump) jump();
		if (left) {
			xSpeed -= playerSpeed;
			facingLeft = true;
		}
		if (right) {
			xSpeed += playerSpeed;
			facingLeft = false;
		}
		
		if (!inAir) {
            if (!isSolid(hitbox, x, y + 1, playing.getCollisionData(), playing.getLevelWidth(), playing.getLevelHeight())) {
                inAir = true;
				jumpCount = 1;
            }
        }

		if (inAir) {
			if(!isSolid(hitbox, x, y + airSpeed, playing.getCollisionData(), playing.getLevelWidth(), playing.getLevelHeight())) {
				airSpeed += gravity;
				y += airSpeed;
			}
			else {
				if (airSpeed > 0)
					resetInAir();
				else
					airSpeed = fallSpeed;
			}
		}
		
		if (xSpeed == 0)
			return;
		updateXPos(xSpeed);
		moving = true;
		
//		updateXPos(xSpeed);
//		updateYPos(ySpeed);
		
//		if (!isSolid(hitbox, x + xSpeed, y, game.getCollisionData(), game.getLevelWidth(), game.getLevelHeight())) {
//			this.x += xSpeed;
//			moving = true;
//		}
		
//		if (!isSolid(hitbox, x, y + ySpeed, game.getCollisionData(), game.getLevelWidth(), game.getLevelHeight())) {
//			this.y += ySpeed;
//			moving = true;
//		}
	}
	
	private void updateXPos(float xSpeed) {
		if (!isSolid(hitbox, x + xSpeed, y, playing.getCollisionData(), playing.getLevelWidth(), playing.getLevelHeight())) {
			this.x += xSpeed;
			moving = true;
		}
	}
	
	private void jump() {
		if (jumpCount < maxJump) {
			jumpCount++;
			inAir = true;
			airSpeed = jumpSpeed;
		}
		jump = false;
	}
	
	private void resetInAir() {
		inAir = false;
		jumpCount = 0;
		airSpeed = 0;
	}
	
//	private void updateYPos(float ySpeed) {
//		if (!isSolid(hitbox, x, y + ySpeed, game.getCollisionData(), game.getLevelWidth(), game.getLevelHeight())) {
//			this.y += ySpeed;
//			moving = true;
//		}
//	}

	public void resetDirection() {
		left = false;
		right = false;
		up = false;
		down = false;
	}

	public void setLeft(boolean left) { this.left = left; }
	public boolean isLeft() { return left; }

	public void setRight(boolean right) {  this.right = right; }
	public boolean isRight() {  return right; }

	public void setUp(boolean up) { this.up = up; }
	public boolean isUp() { return up; }

	public void setDown(boolean down) { this.down = down; }
	public boolean isDown() { return down; }

	public void setJump(boolean jump) { this.jump = jump; }
	public boolean isJump() { return jump; }
}
