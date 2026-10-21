package entities.enemies;

import java.util.List;
import java.util.Random;
import java.awt.Graphics2D;

import display.Camera;
import entities.player.Player;
import helpers.ResourceLoader;
import helpers.EntityConstants.Obstacle;
import static helpers.EntityConstants.Obstacle.*; //fire or shadow

public class ShadowFire extends Enemy {
	private Obstacle type;
	
	public ShadowFire(float x, float y, Obstacle type) {
		super(ResourceLoader.PLAYER_SPRITES, x, y, 20, 20, 10, 10, 333); // 40 * 40
		this.type = type;
		Random random = new Random();
		this.facingLeft = random.nextBoolean();
		this.knockbackSpeed = 0f;
		this.knockbackHeight = 0f;
	}

	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2, type.getId());
	}
	
	@Override
	public void update(List<Integer> collisionData, int levelWidth, int levelHeight, Player player, Camera camera) {
		super.update(collisionData, levelWidth, levelHeight, player, camera);
		updateAnimationTick(type.getAnimationAmount(), type.isLooping());
	}

	@Override
	protected void updateBehavior(List<Integer> collisionData, int levelWidth, int levelHeight, Player player) {
		if (type == FIREBALL) currentHealth--;
		else facePlayer(player.getX());
		
		if (currentHealth <= 0) die();
	}
	
	@Override
	public void takeDamage(int amount, float PlayerX) {
		if (type == FIREBALL) die();
	}
}
