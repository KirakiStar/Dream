package levels;

import java.util.List;

import helpers.ResourceLoader;

public class Level {
	private final LevelData levelData;
	private final String tilesetImg;
	private final String bgImg;
	
	public Level(String jsonFileName) {
		this.levelData = ResourceLoader.LoadLevels(jsonFileName);
		this.tilesetImg = levelData.getAppearnace().getTileset();
		this.bgImg = levelData.getAppearnace().getBgImg();
	}
	
	public int getLevelLayers() { return levelData.getLayers().size(); }
	public int getLevelWidth() { return levelData.getWidth(); }
	public int getLevelHeight() { return levelData.getHeight(); }
	public float getArenaMinX() { return levelData.getArenaMinX(); }
	public float getArenaMaxX() { return levelData.getArenaMaxX(); }
	public float getSpawnX() { return levelData.getSpawnX(); }
	public float getSpawnY() { return levelData.getSpawnY(); }
	
	public int getTileAt(int layerIndex, int tileIndex) { return levelData.getLayers().get(layerIndex).getData().get(tileIndex); }
	public List<Integer> getCollisionData() { return levelData.getLayers().get(0).getData(); }
	public List<Integer> getEntityData() { return levelData.getLayers().get(1).getData(); }
	public List<LevelData.Trigger> getTriggers() { return levelData.getTriggers(); }
	public String getTilesetImg() { return tilesetImg; }
	public int getTileColumn() { return levelData.getAppearnace().getTileSetColumn(); }
	public int getTileRow() { return levelData.getAppearnace().getTileSetRow(); }
	public String getBgImg() { return bgImg; }
	public int getBgWidth() { return levelData.getAppearnace().getBgWidth(); }
	public int getBgHeight() { return levelData.getAppearnace().getBgHeight(); }
}
