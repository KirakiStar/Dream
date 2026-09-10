package entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.Game;
import helper.ResourceLoader;
import static main.Game.SCALE;
import static collision.CollisionChecker.isSolid;

public class Player extends Entity {
	private Game game;
	private BufferedImage[][] sprites;
	private String playerPng = ResourceLoader.PLAYER_SPRITES;
	private boolean moving = false;
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	private boolean jump;
	private float playerSpeed = 1.5f * SCALE;
	
	private boolean inAir = false;
	private int jumpCount = 0;
	private int maxJump = 2;
	private boolean climbable = false;
	private float gravity = 0.05f * SCALE;
	private float airSpeed = 0f;
	private float jumpSpeed = -2.3f * SCALE;
	private float fallSpeed = 0.5f * SCALE;

	public Player(Game game, float x, float y, int width, int height, float offsetX, float offsetY) {
		super(x, y, width, height, offsetX, offsetY);
		this.game = game;
		loadAnimations();
	}

	public void update() {
		updatePosition();
		updateHitbox();
	}

	public void draw(Graphics2D g2) {
		g2.drawImage(sprites[0][0], (int)x, (int)y, (int)(32*SCALE), (int)(32*SCALE), null);
		drawHitbox(g2);
	}

	private void updatePosition() {
		moving = false;

		float xSpeed = 0;
		
		if (jump) jump();
		if (left) xSpeed -= playerSpeed;
		if (right) xSpeed += playerSpeed;
		
		if (!inAir) {
            if (!isSolid(hitbox, x, y + 1, game.getCollisionData(), game.getLevelWidth(), game.getLevelHeight())) {
                inAir = true;
				jumpCount = 1;
            }
        }

		if (inAir) {
			if(!isSolid(hitbox, x, y + airSpeed, game.getCollisionData(), game.getLevelWidth(), game.getLevelHeight())) {
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
		if (!isSolid(hitbox, x + xSpeed, y, game.getCollisionData(), game.getLevelWidth(), game.getLevelHeight())) {
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

	private void loadAnimations() {
		BufferedImage img = ResourceLoader.ImagesLoader(playerPng);

		sprites = new BufferedImage[4][4];
		for(int i = 0; i < sprites.length; i++) {
			for(int j = 0; j < sprites[i].length; j++) {
				sprites[i][j] = img.getSubimage(j*32, i*32, 32, 32);
			}
		}
	}

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
