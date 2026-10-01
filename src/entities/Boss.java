package entities;

import java.util.List;

public abstract class Boss extends MovingEntity implements Attackable {
	
	
	public Boss(float x, float y, int width, int height, float offsetX, float offsetY, int maxHealth) {
		super(x, y, width, height, offsetX, offsetY, maxHealth);
	}
	
	protected abstract void updateBossBehavior(List<Integer> collisionData, int levelWidth, int levelHeight);
	protected abstract void onDeath();
}
