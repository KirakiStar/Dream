package entities.enemies;

import java.util.List;
import java.util.Random;
import java.awt.Graphics2D;

import display.Camera;
import entities.player.Player;
import collision.CollisionChecker;
import helpers.Settings;
import helpers.ResourceLoader;
import helpers.EntityConstants.SheepState;
import static helpers.EntityConstants.SheepState.*;

public class Sheep extends Enemy {
	private SheepState sheepState;
	private final float scale = Settings.getCurrentScale();
	private Random random = new Random();
	private final boolean stalking;
	
	private final float walkSpeed = 0.3f * scale;
	private final float runSpeed = 0.7f * scale;
	private float currentSpeed;
	private float startX;
	private float maxWalkDistance;

	public Sheep(float x, float y, boolean stalking) {
		super(stalking?
				ResourceLoader.BLACKSHEEP_SPRITES :
				ResourceLoader.WHITESHEEP_SPRITES, 
				x, y, 16, 32, 24, 31, 5); //maxHealth = 5
		this.stalking = stalking;
		this.sheepState = IDLE;
		this.startX = x;
		this.maxWalkDistance = random.nextInt(64,128) * scale;
		this.currentSpeed = stalking? runSpeed : walkSpeed;
	}
	
	@Override
	public void update(List<Integer> collisionData, int levelWidth, int levelHeight, Player player, Camera camera) {
		super.update(collisionData, levelWidth, levelHeight, player, camera);
		updateAnimationTick(sheepState.getAnimationAmount(), sheepState.isLooping());
	}

	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2, sheepState.getId());
	}

	@Override
	protected void updateBehavior(List<Integer> collisionData, int levelWidth, int levelHeight, Player player) {
		patrol(collisionData, levelWidth, levelHeight);
		if (stalking) facePlayer(player.getX());
	}
	
	private void patrol(List<Integer> cd, int lw, int lh) {
		float trueXSpeed = facingLeft ? -currentSpeed : currentSpeed;
		float nextX = x + trueXSpeed;

		if (!stalking && Math.abs(nextX - startX) >= maxWalkDistance) {
			turnAround();
			return;
		}

		if (CollisionChecker.isSolid(hitbox, nextX, y, cd, lw, lh)) {
			if (!stalking) turnAround();
			return;
		}

		float checkX = facingLeft ? nextX - this.width : nextX + this.width;
		float checkY = y + 1.0f;
		boolean solidGround = CollisionChecker.isSolid(hitbox, checkX, checkY, cd, lw, lh);
		boolean platformGround = CollisionChecker.isPlatform(hitbox, checkX, y, checkY, 1.0f, cd, lw, lh);

		if (!solidGround && !platformGround) {
			if (!stalking) turnAround();
			return;
		}

		this.xSpeed = trueXSpeed;
//		this.sheepState = WALKING;
	}
	
	private void turnAround() {
		facingLeft = !facingLeft;
		this.xSpeed = 0;
		this.sheepState = IDLE;
	}
	
}
