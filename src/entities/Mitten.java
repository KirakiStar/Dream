package entities;

import java.util.List;
import java.awt.Graphics2D;

import main.Game;
import helper.ResourceLoader;
import helper.Constants.MittenState;
import static helper.Constants.MittenState.*;

public class Mitten extends Enemy {
	private static final ResourceLoader.SpriteSheet mittenSprites = ResourceLoader.MITTEN_SPRITES;
	private MittenState mittenState;
	
	private static final int WIDTH = (int)(16*Game.SCALE);
	private static final int HEIGHT = (int)(32*Game.SCALE);
	private static final float OFFSET_X = 24 * Game.SCALE;
	private static final float OFFSET_Y = 31 * Game.SCALE;

	private int tick = 0;
	
	private int jumpInterval = 300;
	private float jumpSpeed = -2f * Game.SCALE;
	
	public Mitten(float x, float y, int startingTick) {
		super(x, y, WIDTH, HEIGHT, OFFSET_X, OFFSET_Y, 3, mittenSprites); //maxHealth = 2
		mittenState = IDLE;
		tick += startingTick;
	}
	
	@Override
	public void update(List<Integer> collisionData, int levelWidth, int levelHeight) {
		super.update(collisionData, levelWidth, levelHeight);
		updateAnimationTick(mittenState.getAnimationAmount(), mittenState.isLooping());
	}
	
	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2, mittenState.getId());
	}

	@Override
	protected void updateBehavior(List<Integer> collisionData, int levelWidth, int levelHeight) {
		tick++;
		
		if (tick >= jumpInterval) {
			jump();
			tick = 0;
		}
	}
	
	private void jump() {
		if (!inAir) {
			inAir = true;
			airSpeed = jumpSpeed;
		}
	}
}
