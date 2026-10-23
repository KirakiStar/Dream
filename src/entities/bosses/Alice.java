package entities.bosses;

import java.util.List;
import java.awt.Graphics2D;

import display.Camera;
import collision.Hitbox;
import collision.CollisionChecker;
import entities.Item;
import entities.player.Player;
import helpers.Settings;
import helpers.ResourceLoader;
import helpers.BossesKillStatus;
import helpers.EntityConstants.AliceState;
import static helpers.EntityConstants.AliceState.*;
import static helpers.EntityConstants.ItemType.PUPPETEER;

public class Alice extends Boss {
	private AliceState aliceState;
	
	private static final int WIDTH = 32;
	private static final int HEIGHT = 56;
	private static final float OFFSET_X = 32f;
	private static final float OFFSET_Y = 40f;
	
	private final float walkSpeed = 0.3f * Settings.getCurrentScale();
	private final float dashSpeed = 3f * Settings.getCurrentScale();
	
	private Hitbox attackBox;
	private boolean attacking = false;
	private final float fleeRange = 64f * Settings.getCurrentScale();
	private boolean dashing = false;
	private boolean spin = false;
	private int warning = 0; // for slash and dash
	private final int attackDamage = 1;
	
	private int tick = 0;
	private int action = 0; // 1:slash, 2:dash, 3:patrol, other: idle
	private int[] actionInterval = {150,4,100,300,200};

	public Alice(float x, float y) {
		super(BossesKillStatus.ALICE, ResourceLoader.ALICE_SPRITES, 
				x, y, WIDTH, HEIGHT, OFFSET_X, OFFSET_Y, 40); //maxhealth = 40
		this.currentSpeed = walkSpeed;
		this.jumpSpeed = -1f * Settings.getCurrentScale();
		this.aliceState = ATTACK1;
		setAttackBox();
	}
	
	@Override
	public void update(List<Integer> collisionData, int levelWidth, int levelHeight, Player player, Camera camera) {
		super.update(collisionData, levelWidth, levelHeight, player, camera);
		updateAttackBox();
		updateAnimationTick();
		updateAnimation();
	}
	
	private void setAttackBox() {
		float offsetX = 32f;
		float offsetY = 55f;
		int attackWidth = 10;
		int attackHeight = 32;
		this.attackBox = new Hitbox(this, offsetX, offsetY, attackWidth, attackHeight);
	}
	
	private void updateAttackBox() {
		if (attacking || dashing) {
			if (facingLeft) attackBox.update(x - attackBox.getWidth() - OFFSET_X, y);
			else attackBox.update(x + this.width + attackBox.getWidth(), y);
		} else {
			if (facingLeft) attackBox.update(x - (attackBox.getWidth()), y);
			else attackBox.update(x + this.width, y);
		}
	}

	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2, aliceState.getId());
//		attackBox.drawDebug(g2);
	}
	
	protected void updateAnimationTick() {
		super.updateAnimationTick(aliceState.getAnimationAmount(), aliceState.isLooping());
		
		if (dying) {
			if (aniIndex >= aliceState.getAnimationAmount() - 1) {
				playing.getEntityManager().addEntity(new Item(x, y - Settings.getTileSize(), PUPPETEER));
				super.die();
			}
				return;
		}
		if (warning > 0) {
			if (aniIndex >= aliceState.getAnimationAmount() - 1) {
				if (warning == 1) attacking = true;
				else dashing = true;
				warning = 0;
			}
		} else if (dashing) {
			if (aniIndex >= aliceState.getAnimationAmount() - 1)
				resetAttack();
		} else if (attacking) {
			if (aniIndex >= aliceState.getAnimationAmount()/2 && spin == false) {
				turnAround();
				spin = true;
			} else if (aniIndex >= aliceState.getAnimationAmount() - 1) {
				turnAround();
				resetAttack();
			}
		}
	}
	
	@Override
	protected void updateAnimation() {
		if (!alive) return;
		if (dying) {
			setAnimation(DEATH);
			return;
		}
		if (warning > 0) {
			setAnimation(WARNING);
		} else if (attacking) {
			setAnimation(ATTACK2);
		} else if (dashing) {
			setAnimation(ATTACK1);
		} else if (inAir) {
			if (airSpeed < 0) setAnimation(JUMPING);
			else setAnimation(FALLING);
		} else if (moving) {
			setAnimation(WALKING);
		} else {
			setAnimation(IDLE);
		}
	}
	
	private void setAnimation(AliceState newAction) {
		if (this.aliceState.getId() != newAction.getId()) {
			this.aliceState = newAction;
			this.aniTick = 0;
			this.aniIndex = 0;
		}
	}
	
	private void setNewAction() {
		moving = false;
		resetAttack();
		action = random.nextInt(5);
		tick = 0;
	}

	@Override
	protected void updateBossBehavior(List<Integer> collisionData, int levelWidth, int levelHeight, Player player, Camera camera) {
		tick++;
		
		if (dashing) currentSpeed = dashSpeed;
		
		boolean InAttackPhase = (warning > 0) || attacking || dashing;
		
		if (nearPlayer(player.getX(), player.getY()) && !InAttackPhase) {
			if (random.nextInt(128) == 0) {
				jump();
				tick = 300;
			}
		}
		
		if (!inAir && !InAttackPhase) {
			if (tick >= actionInterval[action]) {
				setNewAction();
			}

			switch (action) {
				case 1 -> {
					warning = action;
				}
				case 2 -> {
					warning = action;
					patrol(collisionData, levelWidth, levelHeight, camera);
				}
				case 3 -> {
					patrol(collisionData, levelWidth, levelHeight, camera);
				}
				default -> facePlayer(player.getX());
			}
		} else if (!attacking && warning == 0) {
			if (inAir) jumpBack(collisionData, levelWidth, levelHeight, camera);
			else patrol(collisionData, levelWidth, levelHeight, camera);
		}
	}
	
	private void patrol(List<Integer> cd, int lw, int lh, Camera camera) {
		float trueXSpeed = facingLeft ? -currentSpeed : currentSpeed;
		float nextX = x + trueXSpeed;

		if (CollisionChecker.isSolid(hitbox, nextX, y, cd, lw, lh, camera)) {
			turnAround();
			return;
		}

		this.xSpeed = trueXSpeed;
		moving = true;
	}
	
	private void jumpBack(List<Integer> cd, int lw, int lh, Camera camera) {
		float trueXSpeed = facingLeft ? dashSpeed : -dashSpeed;
		float tileSize = facingLeft ? Settings.getTileSize() : -Settings.getTileSize();
		float nextX = x + trueXSpeed + tileSize;

		if (CollisionChecker.isSolid(hitbox, nextX, y, cd, lw, lh, camera)) return;
		
		this.xSpeed = trueXSpeed;
		moving = true;
	}
	
	private void jump() {
		if (!inAir) {
			inAir = true;
			airSpeed = jumpSpeed;
		}
	}
	
	private void turnAround() {
		facingLeft = !facingLeft;
		this.moving = false;
		this.xSpeed = 0;
	}
	
	private boolean nearPlayer(float playerX, float playerY) {
		if (Math.abs(playerY - this.y) > fleeRange) return false;
		return Math.abs(playerX - this.x) <= fleeRange;
	}
	
	@Override
	public void die() {
		onDeath();
	}
	
	@Override
	protected void onDeath() {
		resetInAir();
		playing.getPlayer().takeDamage(0);
		dying = true;
	}

	@Override
	public void attack() {
		
	}
	
	private void resetAttack() {
		attacking = false;
		dashing = false;
		spin = false;
		currentSpeed = walkSpeed;
	}

	@Override public Hitbox getAttackBox() { return this.attackBox; }
	@Override public int getDamage() { return attackDamage; }
	@Override public boolean isAttacking() { return attacking; }
}
