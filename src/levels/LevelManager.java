package levels;

import gamestates.Playing;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

import helper.ResourceLoader;
import static main.Game.TILES_SIZE;

public class LevelManager {
	private Playing playing;
	private BufferedImage[] tiles;
	private List<Level> levels;
	private int currentLevelIndex = 1;
	
	public LevelManager(Playing playing) {
		this.playing = playing;
		this.levels = new ArrayList<>();
		buildLevels();
		loadLevel(currentLevelIndex);
	}
	
	private void buildLevels() {
		// Index 0
		levels.add(new Level(ResourceLoader.TEST_MAP, ResourceLoader.TEST_LEVEL));
		// Index 1
		levels.add(new Level(ResourceLoader.LEVEL1_MAP, ResourceLoader.LEVEL1_SET));
		// Index 2
		levels.add(new Level(ResourceLoader.LEVEL1_1_MAP, ResourceLoader.LEVEL1_SET));
	}
	
	public void loadLevel(int index, float spawnX, float spawnY) {
		if (index < 0 || index >= levels.size()) return;

		currentLevelIndex = index;
		Level current = getCurrentLevel();

		setTiles(current.getTilesetImg(), current.getTileRow() * current.getTileColumn(), current.getTileColumn());

		if (playing != null) {
			playing.loadLevelData(this, spawnX, spawnY);
		}
	}

	public void loadLevel(int index) {
		if (index < 0 || index >= levels.size()) return;
		Level current = levels.get(index);
		loadLevel(index, current.getSpawnX(), current.getSpawnY());
	}
	
	private void setTiles(String tilesetImg, int tileSetSize, int tileSetColumns) {
		BufferedImage img = ResourceLoader.ImagesLoader(tilesetImg);
		tiles = new BufferedImage[tileSetSize];
		for (int i = 0; i < tileSetSize; i++) {
			int x = (i % tileSetColumns) * 32;
			int y = (i / tileSetColumns) * 32;
			tiles[i] = img.getSubimage(x, y, 32, 32);
		}
	}
	
	public void draw(Graphics2D g2) {
		Level current = getCurrentLevel();
		int layers = current.getLevelLayers();
		int levelSize = current.getLevelWidth() * current.getLevelHeight();
		for (int i = 2; i < layers; i++) {
			for (int j = 0; j < levelSize; j++) {
				int tileID = current.getTileAt(i, j);
				if (tileID == 0) continue;
				int tileX = j % current.getLevelWidth();
				int tileY = j / current.getLevelWidth();
				g2.drawImage(tiles[tileID], tileX * TILES_SIZE, tileY * TILES_SIZE, TILES_SIZE, TILES_SIZE, null);
			}
		}
	}
	
	public void update() { }
	
	public Level getCurrentLevel() { return levels.get(currentLevelIndex); }
	public int getCurrentLevelIndex() { return currentLevelIndex; }
}
