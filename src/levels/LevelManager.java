package levels;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

import gamestates.Playing;
import helpers.ResourceLoader;
import helpers.Settings;

public final class LevelManager {
	private Playing playing;
	private BufferedImage[] tiles;
	private BufferedImage backgroundImg;
	private List<Level> levels;
	private int currentLevelIndex = 0;
	
	public LevelManager(Playing playing) {
		this.playing = playing;
		this.levels = new ArrayList<>();
		buildLevels();
		loadLevel(currentLevelIndex);
	}
	
	private void buildLevels() {
		List<String> levelFiles = ResourceLoader.levelFiles;
		for (String file : levelFiles) {
			levels.add(new Level(file));
		}
	}
	
	public void loadLevel(int index, float spawnX, float spawnY) {
		if (index < 0 || index >= levels.size()) return;

		currentLevelIndex = index;
		Level current = getCurrentLevel();

		setTiles(current.getTilesetImg(), current.getTileRow() * current.getTileColumn(), current.getTileColumn());
		backgroundImg = ResourceLoader.ImagesLoader(current.getBgImg());
		
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
		if (backgroundImg == null) return;

		float screenWidth = Settings.getGameWidth();
		float levelWidthPx = current.getLevelWidth() * Settings.getTileSize();
		float bgWidth = current.getBgWidth() * Settings.getCurrentScale();
		float bgHeight = current.getBgHeight() * Settings.getCurrentScale();

		float bgX = 0;
		if (bgWidth > screenWidth && levelWidthPx > screenWidth) {
			float camX = (playing != null && playing.getCamera() != null) ? playing.getCamera().getX() : 0;
			float maxCamX = levelWidthPx - screenWidth;

			if (maxCamX > 0) {
				float progress = Math.max(0f, Math.min(1f, camX / maxCamX));
				bgX = progress * (levelWidthPx - bgWidth);
			}
		}

		g2.drawImage(backgroundImg, (int)bgX, 0, (int)bgWidth, (int)bgHeight, null);

		int layers = current.getLevelLayers();
		int levelSize = current.getLevelWidth() * current.getLevelHeight();
		for (int i = 2; i < layers; i++) {
			for (int j = 0; j < levelSize; j++) {
				int tileID = current.getTileAt(i, j);
				if (tileID == 0) continue;
				int tileX = j % current.getLevelWidth();
				int tileY = j / current.getLevelWidth();
				g2.drawImage(tiles[tileID], tileX * Settings.getTileSize(), tileY * Settings.getTileSize(), 
							Settings.getTileSize(), Settings.getTileSize(), null);
			}
		}
	}
	
	public void update() { }
	
	public Level getCurrentLevel() { return levels.get(currentLevelIndex); }
	public int getCurrentLevelIndex() { return currentLevelIndex; }
}
