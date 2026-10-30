package entities.bosses;

import java.util.List;
import java.awt.Graphics2D;

import display.Camera;
import collision.Hitbox;
import collision.CollisionChecker;
import entities.Item;
import entities.enemies.SheepSpawn;
import entities.player.Player;
import helpers.Settings;
import helpers.ResourceLoader;
import helpers.BossesKillStatus;
import helpers.EntityConstants.NightmareState;
import static helpers.EntityConstants.NightmareState.*;
import static helpers.EntityConstants.ItemType.HYPNOTIST;

public class Nightmare extends Boss {
	private NightmareState nightmare;
	
	private static final int WIDTH = 80;
	private static final int HEIGHT = 90;
	private static final float OFFSET_X = 10f;
	private static final float OFFSET_Y = 32f;
	
	private final float offsetXLeft = 25f * Settings.getCurrentScale();
	
	private boolean attacking = true;
	private final int attackDamage = 1;
	
	private int tick = 0;
	private int spawnInterval = 500;
	
	private boolean idling = false;

	public Nightmare(float x, float y) {
		super(BossesKillStatus.NIGHTMARE, ResourceLoader.NIGHTMARE_SPRITES, 
				x, y, WIDTH, HEIGHT, OFFSET_X, OFFSET_Y, 70); // maxHealth = 70
		this.nightmare = IDLE;
		this.currentSpeed = 0.2f * Settings.getCurrentScale();
	}
	
	@Override
	public void update(List<Integer> collisionData, int levelWidth, int levelHeight, Player player, Camera camera) {
		super.update(collisionData, levelWidth, levelHeight, player, camera);
		updateAnimation();
		updateAnimationTick(camera);
	}
	
	@Override
	protected void updateHitbox() {
		if (facingLeft) {
			hitbox.update(x + offsetXLeft, y);
		} else {
			hitbox.update(x, y);
		}
	}

	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2, nightmare.getId());
	}
	
	protected void updateAnimationTick(Camera camera) {
		super.updateAnimationTick(nightmare.getAnimationAmount(), nightmare.isLooping());
		
		if (dying) {
			if (aniIndex >= nightmare.getAnimationAmount() - 1) {
				playing.getEntityManager().addEntity(new Item(x, y - Settings.getTileSize(), HYPNOTIST));
				super.die();
			}
				return;
		}
		
		if (attacking) {
			if (aniIndex >= nightmare.getAnimationAmount() - 1) {
				spawnSheep(camera);
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
		
		if (moving) setAnimation(WALKING);
		else setAnimation(IDLE);
	}
	
	private void setAnimation(NightmareState newAction) {
		if (this.nightmare.getId() != newAction.getId()) {
			this.nightmare = newAction;
			this.aniTick = 0;
			this.aniIndex = 0;
		}
	}

	@Override
	protected void updateBossBehavior(List<Integer> collisionData, int levelWidth, int levelHeight, Player player, Camera camera) {
		if (!(hit || attacking)) tick++;
		if (random.nextInt(128) == 1) idling = !idling;
		
		if (tick >= spawnInterval) {
			facePlayer(player.getX());
			attack();
			updateInterval();
			tick = 0;
		} else if (!idling) {
			patrol(collisionData, levelWidth, levelHeight, camera);
		} else {
			facePlayer(player.getX());
			moving = false;
		}
	}
	
	private void patrol(List<Integer> cd, int lw, int lh, Camera camera) {
		float trueXSpeed = facingLeft ? -currentSpeed : currentSpeed;
		float nextX = x + trueXSpeed;

		if (CollisionChecker.isSolid(hitbox, nextX, y, cd, lw, lh, camera)) {
			turnAround();
			return;
		}
		
		if (random.nextInt(128) == 0) turnAround();

		this.xSpeed = trueXSpeed;
		moving = true;
	}
	
	private void updateInterval() {
		spawnInterval = random.nextInt(150,500);
	}
	
	private void spawnSheep(Camera camera) {
		float scale = Settings.getCurrentScale();
		float offsetX = random.nextFloat(-128f, 128f) * scale;
		playing.getEntityManager().addEntity(new SheepSpawn(x + offsetX, y -  320 * scale, camera));
		attacking = false;
	}
	
	private void turnAround() {
		facingLeft = !facingLeft;
		this.moving = false;
		this.xSpeed = 0;
		this.nightmare = IDLE;
		if (facingLeft) x -= offsetXLeft;
		else x += offsetXLeft;
	}
	
	@Override
	public void die() {
		onDeath();
	}

	@Override
	protected void onDeath() {
		playing.getEntityManager().removeWantedEntity(SheepSpawn.class);
		playing.getPlayer().takeDamage(0);
		dying = true;
	}

	@Override
	public void attack() {
		if (!attacking) {
			attacking = true;
		}
	}

	@Override public Hitbox getAttackBox() { return null; }
	@Override public int getDamage() { return attackDamage; }
	@Override public boolean isAttacking() { return attacking; }
}
