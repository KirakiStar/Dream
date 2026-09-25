package levels;

import java.util.List;

public class LevelData {
	private int width;
	private int height;
	private int tileWidth;
	private int tileHeight;
	private int tileSetColumn;
	private int tileSetRow;
	private float spawnX;
	private float spawnY;
	private List<Layer> layers;

	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getTileWidth() { return tileWidth; }
	public int getTileHeight() { return tileHeight; }
	public int getTileSetColumn() { return tileSetColumn; }
	public int getTileSetRow() { return tileSetRow; }
	public float getSpawnX() { return spawnX; }
	public float getSpawnY() { return spawnY; }
	public List<Layer> getLayers() { return layers; }

	public static class Layer {
		private String name;
		private boolean visible;
		private List<Integer> data;
		public String getName() { return name; }
		public boolean isVisible() { return visible; }
		public List<Integer> getData() { return data; }
	}
}