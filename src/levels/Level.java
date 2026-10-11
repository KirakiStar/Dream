package levels;

import java.util.List;

import inputsAndResourceLoader.ResourceLoader;

public class Level {
	private final LevelData levelData;
	private final String tilesetImg;
	
	public Level(String jsonFileName) {
		this.levelData = ResourceLoader.LoadLevels(jsonFileName);
		this.tilesetImg = levelData.getTileset();
	}
	
	public int getLevelLayers() { return levelData.getLayers().size(); }
	public int getLevelWidth() { return levelData.getWidth(); }
	public int getLevelHeight() { return levelData.getHeight(); }
	public float getArenaMinX() { return levelData.getArenaMinX(); }
	public float getArenaMaxX() { return levelData.getArenaMaxX(); }
	public int getTileColumn() { return levelData.getTileSetColumn(); }
	public int getTileRow() { return levelData.getTileSetRow(); }
	public float getSpawnX() { return levelData.getSpawnX(); }
	public float getSpawnY() { return levelData.getSpawnY(); }
	
	public int getTileAt(int layerIndex, int tileIndex) { return levelData.getLayers().get(layerIndex).getData().get(tileIndex); }
	public List<Integer> getCollisionData() { return levelData.getLayers().get(0).getData(); }
	public List<Integer> getEntityData() { return levelData.getLayers().get(1).getData(); }
	public List<LevelData.TriggerData> getTriggers() { return levelData.getTriggers(); }
	public String getTilesetImg() { return tilesetImg; }
}
