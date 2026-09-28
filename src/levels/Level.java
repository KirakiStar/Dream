package levels;

import java.util.List;

import helper.ResourceLoader;

public class Level {
	private final LevelData levelData;
	private final String tilesetImg;
	
	public Level(String jsonFileName, String tilesetImg) {
		this.levelData = ResourceLoader.LoadLevels(jsonFileName);
		this.tilesetImg = tilesetImg;
	}
	
	public int getLevelLayers() { return levelData.getLayers().size(); }
	public int getLevelWidth() { return levelData.getWidth(); }
	public int getLevelHeight() { return levelData.getHeight(); }
	public int getTileColumn() { return levelData.getTileSetColumn(); }
	public int getTileRow() { return levelData.getTileSetRow(); }
	public float getSpawnX() { return levelData.getSpawnX(); }
	public float getSpawnY() { return levelData.getSpawnY(); }
	
	public int getTileAt(int layerIndex, int tileIndex) {
		return levelData.getLayers().get(layerIndex).getData().get(tileIndex);
	}
	
	public List<Integer> getCollisionData() {
		return levelData.getLayers().get(0).getData();
	}
	
	public List<Integer> getEntityData() {
		return levelData.getLayers().get(1).getData();
	}
	
	public List<LevelData.TriggerData> getTriggers() {
		return levelData.getTriggers();
	}
	
	public String getTilesetImg() { return tilesetImg; }
}
