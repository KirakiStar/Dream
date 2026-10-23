package entities.player;

import java.awt.Graphics2D;
import java.util.List;

import helpers.Settings;
import gamestates.Playing;
import collision.Hitbox;
import collision.CollisionChecker;
import entities.Attackable;
import entities.MovingEntity;
import helpers.ResourceLoader;
import helpers.EntityConstants.PlayerState;
import static helpers.EntityConstants.PlayerState.*;

public class Player extends MovingEntity implements Attackable {
	private Playing playing;
	private PlayerState playerAction;
	private PlayerStats stats;
	
	private float jumpSpeed;
	private int maxJump;
	private int attackDamage;
	
	private static final int WIDTH = 16;
	private static final int HEIGHT = 36;
	private static final float OFFSET_X = 24f;
	private static final float OFFSET_Y = 27f;
	
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	private boolean jump;
	private boolean climbing;
	
	private int jumpCount = 0;
	private boolean climbable = false;
	private final float drownSpeed = 0.5f * Settings.getCurrentScale();
	private final float HEIGHT_DROP_LADDER = HEIGHT / 2 * Settings.getCurrentScale();
	
	private Hitbox attackBox;
	private boolean attacking = false;
	private boolean hit = false;
	private int knockbackDir = 1;

	public Player(Playing playing, float x, float y) {
		super(ResourceLoader.PLAYER_SPRITES, x, y, WIDTH, HEIGHT, OFFSET_X, OFFSET_Y, 5); //default maxHealth = 5
		this.stats = new PlayerStats(maxHealth, 1); //default attack damage = 1
		this.playing = playing;
		this.playerAction = IDLE;
		this.facingLeft = true;
		setStats();
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
		float offsetX = 24f;
		float offsetY = 32f;
		int attackWidth = 27;
		int attackHeight = 32;
		this.attackBox = new Hitbox(this, offsetX, offsetY, attackWidth, attackHeight);
	}

	@Override
	public void update() {
		super.update();
		updatePosition();
		updateAttackBox();
		updatePlayerAnimation();
		updateAnimationTick();
	}

	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2, playerAction.getId());
//		if (attacking) attackBox.drawDebug(g2);
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
	
	private void updatePlayerAnimation() {
		if (!alive) {
			setAnimation(DEATH);
		} else if (hit) {
			setAnimation(ATTACKED);
		} else if (attacking) {
			setAnimation(ATTACK);
		} else if (inAir) {
			if (airSpeed < 0) {
				if (jumpCount < 2) setAnimation(JUMPING);
				else setAnimation(JUMPING2);
			} else setAnimation(FALLING);
		} else if (climbing) {
			if (moving)setAnimation(CLIMBING);
			else setAnimation(ON_LADDER);
		} else if (moving) {
			setAnimation(RUNNING);
		} else {
			setAnimation(IDLE);
		}
	}
	
	private void setStats() {
		this.maxHealth = stats.getMaxHealth();
		this.entitySpeed = stats.getPlayerSpeed();
		this.jumpSpeed = stats.getJumpSpeed();
		this.maxJump = stats.getMaxJump();
		this.attackDamage = stats.getAttackDamage();
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

			if (up) ySpeed -= entitySpeed;
			if (down) ySpeed += entitySpeed;
			if (left) { xSpeed -= entitySpeed; facingLeft = true; }
			if (right) { xSpeed += entitySpeed; facingLeft = false; }

			if (ySpeed != 0 && !CollisionChecker.isSolid(hitbox, x, y + ySpeed, cd, lw, lh)) {
				y += ySpeed;
				moving = true;
			}
			if (xSpeed != 0) {
				updateXPos(xSpeed, false, cd, lw, lh, playing.getCamera());
			}

			if (!CollisionChecker.isLadder(hitbox, cd, lw, lh)) {
				climbing = false;
				resetInAir();
			}
			return;
		}

		float xSpeed = 0;
		if (jump) jump();
		if (left) { xSpeed -= entitySpeed; facingLeft = true; }
		if (right) { xSpeed += entitySpeed; facingLeft = false; }

		if (hit) {
			resetDirection();
			xSpeed = knockbackDir * entitySpeed;
		}

		boolean dropping = down;
		updateGroundPosition(xSpeed, cd, lw, lh, dropping, playing.getCamera());
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
	}

	@Override
	public void attack() {
		attacking = true;
	}
	
	private void updateAttackBox() {
		if (facingLeft) {
			attackBox.update(x - (attackBox.getWidth()), y);
		} else {
			attackBox.update(x + this.width, y);
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
	}
	
	@Override
	public void heal(int amount) {
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
	@Override public boolean isAttacking() { return attacking; }
	public boolean isHit() { return hit; }

	public void attackDamageUp(int amount) {
		stats.attackDamageUp(amount);
		setStats();
	}
	public void maxHealthUp(int amount) {
		stats.MaxHealthUp(amount);
		setStats();
	}
	
	public void activateCheat() {
		stats.TheSevenDeadlyCheats();
		setStats();
		heal(maxHealth);
	}
}
