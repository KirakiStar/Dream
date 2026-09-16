package display;

import gamestates.Playing;
import main.Game;

public class Camera {
	private float x;
	private float y;
	private int width;
	private int height;
	private int levelWidth;
	private int levelHeight;
	private Playing playing;

	public Camera(Playing playing) {
		this.playing = playing;
		this.x = 0;
		this.y = 0;
		this.width = Game.GAME_WIDTH;
		this.height = Game.GAME_HEIGHT;
		setLevelBounds();
	}

	public void update() {
		float targetX = playing.getPlayer().getX() - (width/2) + (playing.getPlayer().getWidth() * Game.SCALE);
		float targetY = playing.getPlayer().getY() - (height/2) + Game.CAMERA_Y_RATIO * (playing.getPlayer().getHeight() * Game.SCALE);

		x += (targetX - this.x) * 0.1f;
		y += (targetY - this.y) * 0.1f;

		if (x < 0) x = 0;
		if (y < 0) y = 0;
		if (x > levelWidth - width) x = levelWidth - width;
		if (y > levelHeight - height) y = levelHeight - height;
	}

	private void setLevelBounds() {
		this.levelWidth = playing.getLevelWidth() * Game.TILES_SIZE;
		this.levelHeight = playing.getLevelHeight() * Game.TILES_SIZE;
	}

	public float getX() { return x; }
	public float getY() { return y; }
}
