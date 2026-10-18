package levels;

import java.util.List;

public class LevelData {
	private int width;
	private int height;
	private int tileWidth;
	private int tileHeight;
	private float arenaMinX;
	private float arenaMaxX;
	private float spawnX;
	private float spawnY;
	private List<Layer> layers;
	private List<Trigger> triggers;
	private Appearance appearance;

	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getTileWidth() { return tileWidth; }
	public int getTileHeight() { return tileHeight; }
	public float getArenaMinX() { return arenaMinX; }
	public float getArenaMaxX() { return arenaMaxX; }
	public float getSpawnX() { return spawnX; }
	public float getSpawnY() { return spawnY; }
	public List<Layer> getLayers() { return layers; }
	public List<Trigger> getTriggers() { return triggers; }
	public Appearance getAppearnace() { return appearance; }

	public static class Layer {
		private String name;
		private boolean visible;
		private List<Integer> data;
		public String getName() { return name; }
		public boolean isVisible() { return visible; }
		public List<Integer> getData() { return data; }
	}
	
	public static class Trigger {
		private String type;
		private String activation;
		private float x;
		private float y;
		private float width;
		private float height;
		private int targetLevel;
		private float spawnX;
		private float spawnY;

		public String getType() { return type; }
		public String getActivation() { return activation; }
		public float getX() { return x; }
		public float getY() { return y; }
		public float getWidth() { return width; }
		public float getHeight() { return height; }
		public int getTargetLevel() { return targetLevel; }
		public float getSpawnX() { return spawnX; }
		public float getSpawnY() { return spawnY; }
	}
	
	public static class Appearance {
		private String tileset;
		private int tileSetColumn;
		private int tileSetRow;
		
		public String getTileset() { return tileset; }
		public int getTileSetColumn() { return tileSetColumn; }
		public int getTileSetRow() { return tileSetRow; }
	}
}