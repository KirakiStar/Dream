package entities.enemies;

import display.Camera;
import java.util.List;
import java.util.Random;
import java.awt.Graphics2D;

import helpers.Settings;
import entities.player.Player;
import helpers.ResourceLoader;
import helpers.EntityConstants.SheepState;
import static helpers.EntityConstants.SheepState.*;

public class Sheep extends Enemy {
	private static Random random = new Random();
	private SheepState sheepState;
	private final float jumpSpeed = -2f * Settings.getCurrentScale();
	public static final int jumpInterval = 300;

	public Sheep(float x, float y) {
		super(random.nextBoolean()? 
				ResourceLoader.WHITESHEEP_SPRITES : 
				ResourceLoader.BLACKSHEEP_SPRITES,
				x, y, 16, 32, 24, 31, 5); //maxHealth = 5
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
		if (sheepState == IDLE) facePlayer(player.getX());
	}

	private void jump() {
		if (!inAir) {
			inAir = true;
			airSpeed = jumpSpeed;
		}
	}
}
