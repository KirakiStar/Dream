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

public class SheepSpawn extends Enemy {
	private static Random random = new Random();
	private SheepState sheepState;
	private Camera camera;
	private final float scale = Settings.getCurrentScale();
	
	private float jumpSpeed;
	private int jumpInterval = 300;
	private int tick = 0;
	
	private final float walkSpeed = 0.3f * scale;
	private final float runSpeed = 0.7f * scale;
	private float currentSpeed = walkSpeed;
	
	private final float followRangeX = 64f * scale;
	private final float followRangeY = 256f * scale;

	public SheepSpawn(float x, float y, Camera camera) {
		super(random.nextBoolean()? 
				ResourceLoader.WHITESHEEP_SPRITES : 
				ResourceLoader.BLACKSHEEP_SPRITES,
				x, y, 16, 32, 24, 31, 5); //maxHealth = 5
		this.camera = camera;
		this.sheepState = IDLE;
		jump();
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
		if (inAir) patrol(collisionData, levelWidth, levelHeight);
		
		else {
			tick++;
			if (seePlayer(player.getX(), player.getY())) {
				currentSpeed = runSpeed;
				facePlayer(player.getX());
				patrol(collisionData, levelWidth, levelHeight);
				if (tick >= jumpInterval) {
					jump();
					updateInterval();
				}
			} else {
				currentSpeed = walkSpeed;
				if (random.nextInt(128) == 0) turnAround();
				patrol(collisionData, levelWidth, levelHeight);
			}
		}
	}
	
	private void updateInterval() {
		jumpInterval = random.nextInt(150,300);
		tick = 0;
	}
	
	private void patrol(List<Integer> cd, int lw, int lh) {
		float trueXSpeed = facingLeft ? -currentSpeed : currentSpeed;
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
		this.xSpeed = 0;
	}

	private void jump() {
		if (!inAir) {
			jumpSpeed = -scale * random.nextFloat(2,5);
			inAir = true;
			airSpeed = jumpSpeed;
		}
	}
	
	private boolean seePlayer(float playerX, float playerY) {
		if (playerY > this.y || 
			Math.abs(playerY - this.y) > followRangeY) return false;
		return Math.abs(playerX - this.x) <= followRangeX;
	}
}
