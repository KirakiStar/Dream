package entities;

import java.awt.Graphics2D;

import inputsAndResourceLoader.ResourceLoader;
import entities.EntityConstants.ItemType;
import static entities.EntityConstants.ItemType.*;
import main.Game;

public class Item extends Entity {
	private ResourceLoader.SpriteSheet itemSprites = ResourceLoader.ITEM_SPRITES;
	private ItemType item;
	private boolean collected = false;
	
	private static final int WIDTH = (int)(24*Game.SCALE);
	private static final int HEIGHT = (int)(24*Game.SCALE);
	private static final float OFFSET_X = 20 * Game.SCALE;
	private static final float OFFSET_Y = 20 * Game.SCALE;
	
	public Item(float x, float y, ItemType item) {
		super(x, y, WIDTH, HEIGHT, OFFSET_X, OFFSET_Y, 1);
		this.item = STRAWBERRY;
		loadAnimations(itemSprites);
	}

	public Item(float x, float y) {
		this(x, y, STRAWBERRY);
	}
	
	@Override
	public void update() {
		super.update();
		updateAnimationTick(item.getAnimationAmount(), item.isLooping());
	}

	@Override
	public void draw(Graphics2D g2) {
		if (!collected) super.draw(g2, item.getId());
		hitbox.drawDebug(g2);
	}
	
	public void collect(Player player) {
		if (collected) return;

		switch (item) {
			default:
			case STRAWBERRY:
				player.heal(item.getHealAmount());
//				System.out.println("heal");
				break;
		}
		
		collected = true;
		die();
	}
	
	public int getHealAmount() { return item.getHealAmount(); }
}
