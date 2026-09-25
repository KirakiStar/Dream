package levels;

import java.util.List;

import helper.ResourceLoader;

public class Level {
	private LevelData levelData;
	
	public Level(String jsonFileName) {
		this.levelData = ResourceLoader.LoadLevels(jsonFileName);
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
		return levelData.getLayers().get(levelData.getLayers().size()-1).getData();
	}
}
