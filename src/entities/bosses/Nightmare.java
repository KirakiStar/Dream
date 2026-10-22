package entities.bosses;

import java.util.List;
import java.awt.Graphics2D;

import display.Camera;
import collision.Hitbox;
import entities.Item;
import entities.enemies.Sheep;
import entities.player.Player;
import helpers.Settings;
import helpers.ResourceLoader;
import helpers.BossesKillStatus;
import helpers.EntityConstants.NightmareState;
import static helpers.EntityConstants.NightmareState.*;
import static helpers.EntityConstants.ItemType.HYPNOTIST;

public class Nightmare extends Boss {
	private NightmareState nightmare;
	
	private static final int WIDTH = 32;
	private static final int HEIGHT = 56;
	private static final float OFFSET_X = 32f;
	private static final float OFFSET_Y = 40f;
	
	private boolean attacking = false;
	private final int attackDamage = 1;
	
	private int tick = 0;
	private int spawnInterval = 500;

	public Nightmare(float x, float y) {
		super(BossesKillStatus.NIGHTMARE, ResourceLoader.NIGHTMARE_SPRITES, 
				x, y, WIDTH, HEIGHT, OFFSET_X, OFFSET_Y, 45); // maxHealth = 45
		this.nightmare = IDLE;
	}
	
	@Override
	public void update(List<Integer> collisionData, int levelWidth, int levelHeight, Player player, Camera camera) {
		super.update(collisionData, levelWidth, levelHeight, player, camera);
//		updateAnimation();
		updateAnimationTick();
	}

	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2, nightmare.getId());
	}
	
	protected void updateAnimationTick() {
		super.updateAnimationTick(nightmare.getAnimationAmount(), nightmare.isLooping());
		
		if (attacking) {
			if (aniIndex >= nightmare.getAnimationAmount() - 1) {
				spawnSheep();
			}
		}
	}
	
	@Override
	protected void updateAnimation() {
		
	}

	@Override
	protected void updateBossBehavior(List<Integer> collisionData, int levelWidth, int levelHeight, Player player, Camera camera) {
		if (!(hit || attacking)) tick++;
		if (nightmare == IDLE) facePlayer(player.getX());
		
		if (tick >= spawnInterval) {
			attack();
			updateInterval();
			tick = 0;
		}
	}
	
	private void updateInterval() {
		spawnInterval = random.nextInt(150,500);
	}
	
	private void spawnSheep() {
		float scale = Settings.getCurrentScale();
		float offsetX = random.nextFloat(-128f, 128f) * scale;
		playing.getEntityManager().addEntity(new Sheep(x + offsetX, y -  320 * scale));
		attacking = false;
	}
	
	@Override
	public void die() {
		onDeath();
		super.die();
	}

	@Override
	protected void onDeath() {
		playing.getEntityManager().removeWantedEntity(Sheep.class);
		playing.getPlayer().takeDamage(0);
		playing.getEntityManager().addEntity(new Item(x, y - Settings.getTileSize(), HYPNOTIST));
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
