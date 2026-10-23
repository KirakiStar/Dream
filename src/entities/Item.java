package entities;

import java.awt.Graphics2D;

import entities.player.Player;
import helpers.ResourceLoader;
import helpers.EntityConstants.ItemType;
import static helpers.EntityConstants.ItemType.*;

public class Item extends Entity {
	private ItemType item;
	private boolean collected = false;
	
	private static final int WIDTH = 40;
	private static final int HEIGHT = 40;
	private static final float OFFSET_X = 12f;
	private static final float OFFSET_Y = 12f;
	
	public Item(float x, float y, ItemType item) {
		super(ResourceLoader.ITEM_SPRITES, x, y, WIDTH, HEIGHT, OFFSET_X, OFFSET_Y, 1);
		this.item = item;
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
//		hitbox.drawDebug(g2);
	}
	
	public void collect(Player player) {
		if (collected) return;

		switch (item) {
			case STRAWBERRY -> player.heal(item.getHealAmount());
			case BAT -> {
				player.heal(3);
				item.getBossId().setKilled();
			}
			default -> {
				player.maxHealthUp(item.getHealAmount());
				player.heal(10);
				item.getBossId().setKilled();
				player.attackDamageUp(1);
			}
		}
		
		collected = true;
		die();
	}
	
	public int getHealAmount() { return item.getHealAmount(); }
	public ItemType getItemType() { return item; }
}
