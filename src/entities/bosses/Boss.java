package entities.bosses;

import java.util.List;
import java.util.Random;
import java.awt.Graphics2D;

import display.Camera;
import gamestates.Playing;
import entities.Attackable;
import entities.player.Player;
import entities.MovingEntity;
import helpers.ResourceLoader;
import helpers.EntityConstants.BossId;

public abstract class Boss extends MovingEntity implements Attackable {
	protected BossId bossId;
	protected boolean hit = false;
	protected float xSpeed = 0;
	protected float walkSpeed;
	protected float jumpSpeed;
	
	protected Playing playing;
	Random random;

	public Boss(BossId bossId, ResourceLoader.SpriteSheet entitySprites, float x, float y, int width, int height, float offsetX, float offsetY, int maxHealth) {
		super(entitySprites, x, y, width, height, offsetX, offsetY, maxHealth);
		this.bossId = bossId;
		this.random = new Random();
	}
	
	public void setPlaying(Playing playing) {
		this.playing = playing;
	}
	
	public void update(List<Integer> collisionData, int levelWidth, int levelHeight, Player player, Camera camera) {
		super.update();
		xSpeed = 0;
		updateBossBehavior(collisionData, levelWidth, levelHeight, player, camera);
		updateGroundPosition(xSpeed, collisionData, levelWidth, levelHeight, false, camera);
	}
	
	@Override
	public void draw(Graphics2D g2, int entityState) {
		super.draw(g2, entityState);
		hitbox.drawDebug(g2);
	}
	
	protected void facePlayer(float playerX) {
		facingLeft = (playerX < this.x);
	}
	
	protected abstract void updateAnimation();
	protected abstract void updateBossBehavior(List<Integer> collisionData, int levelWidth, int levelHeight, Player player, Camera camera);
	protected abstract void onDeath();
	
	public int getId() { return bossId.getId(); }
}
