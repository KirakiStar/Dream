package entities;

import java.awt.Graphics2D;
import java.util.List;

import main.Game;
import gamestates.Playing;
import collision.Hitbox;
import collision.CollisionChecker;
import inputsAndResourceLoader.ResourceLoader;
import entities.EntityConstants.PlayerState;
import static entities.EntityConstants.PlayerState.*;

public class Player extends MovingEntity implements Attackable {
	private Playing playing;
	private static final ResourceLoader.SpriteSheet playerSprites = ResourceLoader.PLAYER_SPRITES;
	private PlayerState playerAction;
	
	private static final int WIDTH = (int)(16*Game.SCALE);
	private static final int HEIGHT = (int)(36*Game.SCALE);
	private static final float OFFSET_X = 24 * Game.SCALE;
	private static final float OFFSET_Y = 27 * Game.SCALE;
	private static final float HEIGHT_DROP_LADDER = HEIGHT / 2;
	
	private final float playerSpeed = 1.5f * Game.SCALE;
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	private boolean jump;
	private boolean climbing;
	
	private int jumpCount = 0;
	private final int maxJump = 2;
	private boolean climbable = false;
	private final float jumpSpeed = -2.3f * Game.SCALE;
	private final float drownSpeed = 0.5f * Game.SCALE;
	
	private Hitbox attackBox;
	private boolean attacking = false;
	private final int attackDamage = 1;
	private boolean hit = false;
	private int knockbackDir = 1;

	public Player(Playing playing, float x, float y) {
		super(x, y, WIDTH, HEIGHT, OFFSET_X, OFFSET_Y, 7); //maxHealth = 7
		this.playing = playing;
		this.playerAction = IDLE;
		entitySpeed = playerSpeed;
		loadAnimations(playerSprites);
		setAttackBox();
		this.invincibilityDuration = 120;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		updateHitbox();
		resetDirection();
	}
	
	private void setAttackBox() {
		float offsetX = 24f * Game.SCALE;
		float offsetY = 32f * Game.SCALE;
		int attackWidth = (int)(27 * Game.SCALE);
		int attackHeight = (int)(32 * Game.SCALE);
		this.attackBox = new Hitbox(this, offsetX, offsetY, attackWidth, attackHeight);
	}
	
	protected void updateAnimationTick() {
		super.updateAnimationTick(playerAction.getAnimationAmount(), playerAction.isLooping());
		
		if (!alive) {
			if (aniIndex >= playerAction.getAnimationAmount() - 1) {
				playing.respawnPlayer();
			}
			return;
		}
		
		if (attacking) {
			if (aniIndex >= playerAction.getAnimationAmount() - 1) {
				attacking = false;
			}
		}
		if (hit) {
			if (aniIndex >= playerAction.getAnimationAmount() - 1) {
				hit = false;
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
		if (!alive) {
			setAnimation(DEATH);
			return;
		}
		
		if (hit) {
			setAnimation(ATTACKED);
			return;
		}
		
		if (attacking) {
			setAnimation(ATTACK);
			return;
		}
		
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
		} else if (climbing) {
			setAnimation(CLIMBING);
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
		updateAttackBox();
		updatePlayerAction();
		updateAnimationTick();
	}

	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2, playerAction.getId());
		if (attacking) attackBox.drawDebug(g2);
	}

	private void updatePosition() {
		moving = false;

		List<Integer> cd = playing.getCollisionData();
		int lw = playing.getLevelWidth();
		int lh = playing.getLevelHeight();

		if (CollisionChecker.isWater(hitbox, cd, lw, lh)) {
			die();
			y += drownSpeed;
			return;
		}

		climbable = CollisionChecker.isLadder(hitbox, cd, lw, lh);
		if (jump) {
			climbing = false;
		} else if (climbable && (up || down)) {
			if (down && CollisionChecker.isSolid(hitbox, x, y + HEIGHT_DROP_LADDER, cd, lw, lh)) {
				climbing = false;
				jumpCount = 1;
			} else {
				climbing = true;
				inAir = false;
				airSpeed = 0;
			}
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
				resetInAir();
			}
			return;
		}

		float xSpeed = 0;
		if (jump) jump();
		if (left) { xSpeed -= playerSpeed; facingLeft = true; }
		if (right) { xSpeed += playerSpeed; facingLeft = false; }

		if (hit) {
			resetDirection();
			xSpeed = knockbackDir * playerSpeed;
		}

		boolean dropping = down;
		updateGroundPosition(xSpeed, cd, lw, lh, dropping);
	}
	
	private void jump() {
		if (!inAir) {
			inAir = true;
			airSpeed = jumpSpeed;
			jumpCount = 1;
		} else if (jumpCount < maxJump) {
			jumpCount++;
			airSpeed = jumpSpeed;
		}
		jump = false;
	}
	
	@Override
	protected void dropFromEdgeOrPlatform() {
		jumpCount = 1;
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
	
	@Override
	protected void die() {
		if (!alive) return;
		super.die();
//		System.out.println("Dead");
	}

	@Override
	public void attack() {
		attacking = true;
	}
	
	private void updateAttackBox() {
		if (facingLeft) {
			attackBox.update(x - (attackBox.getWidth()), y);
		} else {
			attackBox.update(x + (WIDTH), y);
		}
	}
	
	@Override
	public void takeDamage(int amount) {
		takeDamage(amount, -1);
	}
	
	public void takeDamage(int amount, float enemyX) {
		if (!alive || hitbox.isInvincible()) return;
		
		if (enemyX >= 0) this.knockbackDir = (enemyX < this.x) ? 1 : -1;
		else this.knockbackDir = (facingLeft) ? 1 : -1;
		
		super.takeDamage(amount);
		hit = true;
		attacking = false;
		hitbox.setInvincible(true);
		
		if (inAir || climbing) {
			climbing = false;
			inAir = true;
			airSpeed = fallSpeed;
		}
		
//		if (alive) System.out.println("hit");
	}
	
	protected void heal(int amount) {
		if (!alive) return;
		currentHealth = Math.min(currentHealth + amount, maxHealth);
	}
	
	public void reset(float spawnX, float spawnY) {
		this.currentHealth = maxHealth;
		this.alive = true;

		resetDirection();
		this.attacking = false;
		this.hit = false;
		this.inAir = false;
		this.climbing = false;
		this.airSpeed = 0;
		this.jumpCount = 0;

		this.hitbox.setInvincible(false);
		this.invincibilityTick = 0;
		this.playerAction = PlayerState.IDLE;
		this.aniTick = 0;
		this.aniIndex = 0;

		setPosition(spawnX, spawnY);
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
	
	@Override public Hitbox getAttackBox() { return attackBox; }
	@Override public int getDamage() { return attackDamage; }
	public boolean isAttacking() { return attacking; }
}
