package display;

import helpers.Settings;
import gamestates.Playing;

public final class Camera {
	private float x;
	private float y;
	private int width;
	private int height;
	private int levelWidth;
	private int levelHeight;
	private Playing playing;
	
	private boolean locked = false;
	private float lockMinX = 0;
	private float lockMaxX = 0;
	
	private float targetLockMinX = 0;
	private float targetLockMaxX = 0;
	
	private final float panningSpeed = 0.1f;

	public Camera(Playing playing) {
		this.playing = playing;
		this.x = 0;
		this.y = 0;
		this.width = Settings.getGameWidth();
		this.height = Settings.getGameHeight();
		setLevelBounds();
	}

	public void update() {
		float targetX = playing.getPlayer().getX() - (width/2) + (playing.getPlayer().getWidth() * Settings.getCurrentScale());
		float targetY = playing.getPlayer().getY() - (height/2) + Settings.CAMERA_Y_RATIO * (playing.getPlayer().getHeight() * Settings.getCurrentScale());

		x += (targetX - this.x) * panningSpeed;
		y += (targetY - this.y) * panningSpeed;
		
		if (locked) {
			lockMinX += (targetLockMinX - lockMinX) * panningSpeed;
			lockMaxX += (targetLockMaxX - lockMaxX) * panningSpeed;
			
			if (x < lockMinX) x = lockMinX;
			if (x > lockMaxX) x = lockMaxX;
		} else {
			if (x < 0) x = 0;
			if (x > levelWidth - width) x = levelWidth - width;
		}
		if (y < 0) y = 0;
		if (y > levelHeight - height) y = levelHeight - height;
	}
	
	public void snapToPlayer() {
		this.locked = false;
		if (playing.getPlayer() != null) {
			this.x = playing.getPlayer().getX() - (width / 2f) + (playing.getPlayer().getWidth() * Settings.getCurrentScale());
			this.y = playing.getPlayer().getY() - (height / 2f) + Settings.CAMERA_Y_RATIO * (playing.getPlayer().getHeight() * Settings.getCurrentScale());

			if (x < 0) x = 0;
			if (x > levelWidth - width) x = levelWidth - width;
			if (y < 0) y = 0;
			if (y > levelHeight - height) y = levelHeight - height;
		}
	}
	
	public void lockToArea(float targetMinX, float targetMaxX) {
		if (!locked) {
			this.locked = true;
			this.lockMinX = this.x;
			this.lockMaxX = this.x;
		}
		
		this.targetLockMinX = targetMinX;
		this.targetLockMaxX = targetMaxX - width;
	}

	public void unlock() {
		this.locked = false;
	}

	public boolean isLocked() { return locked; }
	public float getLockMinX() { return lockMinX; }
	public float getLockMaxX() { return lockMaxX + width; }

	public void setLevelBounds() {
		this.levelWidth = playing.getLevelWidth() * Settings.getTileSize();
		this.levelHeight = playing.getLevelHeight() * Settings.getTileSize();
	}

	public float getX() { return x; }
	public float getY() { return y; }
}
