package entities;

import helpers.EntityConstants;
import java.awt.Graphics2D;
import java.util.List;

import display.Camera;
import collision.Hitbox;
import helpers.ResourceLoader;
import static helpers.EntityConstants.ShabiState.IDLE;

public class Shabi extends Boss {
	private EntityConstants.ShabiState shabiState;
	
	private static final int WIDTH = 32;
	private static final int HEIGHT = 56;
	private static final float OFFSET_X = 32f;
	private static final float OFFSET_Y = 40f;
	
	private Hitbox attackBox;
	private boolean attacking = false;
	private final int attackDamage = 1;
	
	private int tick = 0;
	private int actionInterval = 150;

	public Shabi(float x, float y) {
		super(ResourceLoader.SHABI_SPRITES, x, y, WIDTH, HEIGHT, OFFSET_X, OFFSET_Y, 15); //maxhealth = 15
		facingLeft = true;
		this.shabiState = IDLE;
		setAttackBox();
	}
	
	@Override
	public void update(List<Integer> collisionData, int levelWidth, int levelHeight, Player player, Camera camera) {
		super.update(collisionData, levelWidth, levelHeight, player, camera);
		updateAttackBox();
		updateAnimationTick();
	}
	
	private void setAttackBox() {
		float offsetX = 32f;
		float offsetY = 50f;
		int attackWidth = 16;
		int attackHeight = 32;
		this.attackBox = new Hitbox(this, offsetX, offsetY, attackWidth, attackHeight);
	}
	
	private void updateAttackBox() {
		if (facingLeft) {
			attackBox.update(x - (attackBox.getWidth()), y);
		} else {
			attackBox.update(x + this.width, y);
		}
	}

	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2, shabiState.getId());
		attackBox.drawDebug(g2);
	}
	
	protected void updateAnimationTick() {
		super.updateAnimationTick(shabiState.getAnimationAmount(), shabiState.isLooping());
	}

	@Override
	protected void updateBossBehavior(List<Integer> collisionData, int levelWidth, int levelHeight, Player player) {
		
	}

	@Override
	protected void onDeath() {
		
	}

	@Override
	public void attack() {
		
	}

	@Override public Hitbox getAttackBox() { return this.attackBox; }
	@Override public int getDamage() { return attackDamage; }
	@Override public boolean isAttacking() { return attacking; }
}
