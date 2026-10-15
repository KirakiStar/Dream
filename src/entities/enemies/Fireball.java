package entities.enemies;

import java.util.List;
import java.util.Random;
import java.awt.Graphics2D;

import display.Camera;
import entities.player.Player;
import helpers.ResourceLoader;
import helpers.EntityConstants;
import static helpers.EntityConstants.Obstacle.FIRE_BALL;

public class Fireball extends Enemy {
	private EntityConstants.Obstacle fireball;
	
	public Fireball(float x, float y) {
		super(ResourceLoader.PLAYER_SPRITES, x, y, 16, 16, 8, 8, 333);
		this.fireball = FIRE_BALL;
		Random random = new Random();
		this.facingLeft = random.nextBoolean();
		this.knockbackSpeed = 0f;
		this.knockbackHeight = 0f;
	}

	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2, 0);
	}
	
	@Override
	public void update(List<Integer> collisionData, int levelWidth, int levelHeight, Player player, Camera camera) {
		super.update(collisionData, levelWidth, levelHeight, player, camera);
		updateAnimationTick(fireball.getAnimationAmount(), fireball.isLooping());
	}

	@Override
	protected void updateBehavior(List<Integer> collisionData, int levelWidth, int levelHeight, Player player) {
		currentHealth--;
		if (currentHealth <= 0) {
			die();
		}
	}
	
	@Override
	public void takeDamage(int amount) { }
}
