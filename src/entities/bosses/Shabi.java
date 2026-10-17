package entities.bosses;

import java.util.List;
import java.awt.Graphics2D;

import display.Camera;
import entities.Item;
import entities.player.Player;
import entities.enemies.Fireball;
import collision.Hitbox;
import collision.CollisionChecker;
import helpers.Settings;
import helpers.ResourceLoader;
import helpers.BossesKillStatus;
import helpers.EntityConstants.ShabiState;
import static helpers.EntityConstants.ShabiState.*;
import static helpers.EntityConstants.ItemType.MAGICIAN;

public class Shabi extends Boss {
	private ShabiState shabiState;
	
	private static final int WIDTH = 32;
	private static final int HEIGHT = 56;
	private static final float OFFSET_X = 32f;
	private static final float OFFSET_Y = 40f;
	
	private boolean attacking = false;
	private final int attackDamage = 1;
	
	private int tick = 0;
	private int action = 0; // 0:idle, 1:patrol, 2: jump + move 3:jump + spawn fire, 3:spawn fire
	private int[] actionInterval = {100,200,10,150,100};

	public Shabi(float x, float y) {
		super(BossesKillStatus.SHABI, ResourceLoader.SHABI_SPRITES, 
				x, y, WIDTH, HEIGHT, OFFSET_X, OFFSET_Y, 15); //maxhealth = 15
		this.walkSpeed = 0.5f * Settings.getCurrentScale();
		this.jumpSpeed = -2f * Settings.getCurrentScale();
		this.gravity = 0.03f * Settings.getCurrentScale();
		facingLeft = true;
		this.shabiState = IDLE;
	}
	
	@Override
	public void update(List<Integer> collisionData, int levelWidth, int levelHeight, Player player, Camera camera) {
		super.update(collisionData, levelWidth, levelHeight, player, camera);
		updateAnimation();
		updateAnimationTick();
	}

	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2, shabiState.getId());
	}
	
	protected void updateAnimationTick() {
		super.updateAnimationTick(shabiState.getAnimationAmount(), shabiState.isLooping());
		
		if (attacking) {
			if (aniIndex >= shabiState.getAnimationAmount() - 1) {
				spawnFire();
			}
		}
	}
	
	@Override
	protected void updateAnimation() {
		if (!alive) return;
		if (attacking) {
			setAnimation(ATTACK);
		} else if (inAir && airSpeed < 0) {
			setAnimation(JUMPING);
		} else if (moving && !inAir) {
			setAnimation(WALKING);
		} else {
			setAnimation(IDLE);
		}
	}
	
	private void setAnimation(ShabiState newAction) {
		if (this.shabiState.getId() != newAction.getId()) {
			this.shabiState = newAction;
			this.aniTick = 0;
			this.aniIndex = 0;
		}
	}
	
	private void setNewAction() {
		moving = false;
		attacking = false;
		action = random.nextInt(5);
		tick = 0;
	}

	@Override
	protected void updateBossBehavior(List<Integer> collisionData, int levelWidth, int levelHeight, Player player, Camera camera) {
		if (!(hit || attacking)) tick++;
		
		if (!inAir) {
			if (tick >= actionInterval[action] || attacking) setNewAction();

			switch (action) {
				case 1 -> patrol(collisionData, levelWidth, levelHeight, camera);
				case 2 -> {
					jump();
					patrol(collisionData, levelWidth, levelHeight, camera);
				}
				case 3 -> {
					jump();
					facePlayer(player.getX());
					attack();
				}
				case 4 -> {
					facePlayer(player.getX());
					attack();
				}
				default -> facePlayer(player.getX());
			}
		} else {
			if (action == 2)
				patrol(collisionData, levelWidth, levelHeight, camera);
		}
	}
	
	private void patrol(List<Integer> cd, int lw, int lh, Camera camera) {
		float trueXSpeed = facingLeft ? -walkSpeed : walkSpeed;
		float nextX = x + trueXSpeed;

		if (CollisionChecker.isSolid(hitbox, nextX, y, cd, lw, lh, camera)) {
			turnAround();
			return;
		}

		this.xSpeed = trueXSpeed;
		moving = true;
	}
	
	private void turnAround() {
		facingLeft = !facingLeft;
		this.moving = false;
		this.xSpeed = 0;
		this.shabiState = IDLE;
	}
	
	private void jump() {
		if (!inAir) {
			inAir = true;
			airSpeed = jumpSpeed;
		}
	}
	
	@Override
	public void die() {
		onDeath();
		super.die();
	}

	@Override
	protected void onDeath() {
		playing.getEntityManager().removeWantedEntity(Fireball.class);
		playing.getPlayer().takeDamage(0);
		playing.getEntityManager().addEntity(new Item(x, y, MAGICIAN));
	}

	@Override
	public void attack() {
		if (!attacking) {
			attacking = true;
		}
	}
	
	public void spawnFire() {
		float scale = Settings.getCurrentScale();
		int fireAmount = random.nextInt(1,4);
			for (int i = 0; i < fireAmount; i++) {
				float offsetX = random.nextFloat(-128f, 128f) * scale;
				float offsetY = random.nextFloat(32f, 64f) * scale;
				playing.getEntityManager().addEntity(new Fireball(x + offsetX, y - offsetY));
			}
		attacking = false;
	}

	@Override public Hitbox getAttackBox() { return null; }
	@Override public int getDamage() { return attackDamage; }
	@Override public boolean isAttacking() { return attacking; }
}
