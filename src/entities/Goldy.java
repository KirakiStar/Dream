package entities;

import java.util.List;
import java.awt.Graphics2D;

import main.Game;
import helper.ResourceLoader;
import helper.Constants.GoldyState;
import static helper.Constants.GoldyState.*;

public class Goldy extends Enemy {
	private static final ResourceLoader.SpriteSheet goldySprites = ResourceLoader.GOLDY_SPRITES;
	private GoldyState goldyState;
	
	private static final int WIDTH = (int)(16*Game.SCALE);
	private static final int HEIGHT = (int)(32*Game.SCALE);
	private static final float OFFSET_X = 24 * Game.SCALE;
	private static final float OFFSET_Y = 31 * Game.SCALE;

	public Goldy(float x, float y) {
		super(x, y, WIDTH, HEIGHT, OFFSET_X, OFFSET_Y, 4, goldySprites); //maxHealth = 4
		knockbackSpeed = 0.2f * Game.SCALE;
		goldyState = IDLE;
	}
	
	@Override
	public void update(List<Integer> collisionData, int levelWidth, int levelHeight) {
		super.update(collisionData, levelWidth, levelHeight);
		updateAnimationTick(goldyState.getAnimationAmount(), goldyState.isLooping());
	}
	
	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2, goldyState.getId());
	}

	@Override
	protected void updateBehavior(List<Integer> collisionData, int levelWidth, int levelHeight) {
		
	}
}
