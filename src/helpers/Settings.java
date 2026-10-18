package helpers;

public final class Settings {
	private static final float[] SCALE = {1f, 1.5f, 2f, 2.5f};
	private static int scaleIndex = 2;

	private final static int DEFAULT_TILE_SIZE = 32;
	private final static int GAME_WIDTH_IN_TILES = 13;	// 416
	private final static int GAME_HEIGHT_IN_TILES = 10;	// 320

	private static float currentScale = SCALE[scaleIndex];
	private static int tileSize = (int)(DEFAULT_TILE_SIZE * currentScale);
	private static int gameWidth = tileSize * GAME_WIDTH_IN_TILES;
	private static int gameHeight = tileSize * GAME_HEIGHT_IN_TILES;

	public final static float CAMERA_Y_RATIO = 0.5f;

	private static void calNewScale(int newIndex) {
		if (newIndex < 0 || newIndex >= SCALE.length) return;

		scaleIndex = newIndex;
		currentScale = SCALE[scaleIndex];
		tileSize = (int)(DEFAULT_TILE_SIZE * getCurrentScale());
		gameWidth = getTileSize() * GAME_WIDTH_IN_TILES;
		gameHeight = getTileSize() * GAME_HEIGHT_IN_TILES;
	}
	
	public static void setNewScale(int nextIndex) { calNewScale(scaleIndex + nextIndex); }

	public static float getCurrentScale() { return currentScale; }
	public static int getTileSize() { return tileSize; }
	public static int getGameWidth() { return gameWidth; }
	public static int getGameHeight() { return gameHeight; }
}