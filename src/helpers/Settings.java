package helpers;

public class Settings {
	public final static float SCALE = 2f;
	public final static int TILES_DEFAULT_SIZE = 32;
	public final static int TILES_IN_WIDTH = 13;
	public final static int TILES_IN_HEIGHT = 10;
	public final static int TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
	public final static float CAMERA_Y_RATIO = 0.5f;
}
