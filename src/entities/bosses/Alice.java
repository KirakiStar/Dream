package entities.bosses;

import java.awt.Graphics2D;
import java.util.List;

import display.Camera;
import collision.Hitbox;
import entities.player.Player;
import helpers.ResourceLoader;
import helpers.EntityConstants;
import static helpers.EntityConstants.AliceState.*;

public class Alice extends Boss {
	private EntityConstants.AliceState aliceState;
	
	private static final int WIDTH = 32;
	private static final int HEIGHT = 56;
	private static final float OFFSET_X = 32f;
	private static final float OFFSET_Y = 40f;
	
	private Hitbox attackBox;
	private boolean attacking = false;
	private final int attackDamage = 1;
	
	private int tick = 0;
	private int actionInterval = 300;

	public Alice(float x, float y) {
		super(EntityConstants.BossId.ALICE, ResourceLoader.ALICE_SPRITES, 
				x, y, WIDTH, HEIGHT, OFFSET_X, OFFSET_Y, 30); //maxhealth = 30
		this.aliceState = IDLE;
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
		super.draw(g2, aliceState.getId());
		attackBox.drawDebug(g2);
	}
	
	protected void updateAnimationTick() {
		super.updateAnimationTick(aliceState.getAnimationAmount(), aliceState.isLooping());
	}

	@Override
	protected void updateBossBehavior(List<Integer> collisionData, int levelWidth, int levelHeight, Player player) {
		if (aliceState == IDLE)
			facePlayer(player.getX());
	}

	private void facePlayer(float playerX) {
		facingLeft = (playerX < this.x);
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
