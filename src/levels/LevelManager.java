package levels;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.Game;
import helper.ResourceLoader;
import static main.Game.TILES_SIZE;

public class LevelManager {
	private Game game;
	private BufferedImage[] tiles;
	private Level level;
	
	private String levelJson = ResourceLoader.LEVEL1_MAP;
	private String levelImg = ResourceLoader.LEVEL1_SET;
	
	public LevelManager(Game game) {
		this.game = game;
		level = new Level(levelJson);
		setTiles(level.getTileRow() * level.getTileColumn(), level.getTileColumn());
	}
	
	private void setTiles(int tileSetSize, int tileSetColumns) {
		BufferedImage img = ResourceLoader.ImagesLoader(levelImg);
		tiles = new BufferedImage[tileSetSize];
		for (int i=0; i<tileSetSize; i++) {
			int x = i % tileSetColumns * 32;
			int y = i / tileSetColumns * 32;
			tiles[i] = img.getSubimage(x, y, 32, 32);
		}
	}
	
	public void draw(Graphics2D g2) {
		int layers = level.getLevelLayers()-1;
		int levelSize = level.getLevelWidth() * level.getLevelHeight();
		for (int i=0; i<layers; i++) {
			for (int j=0; j<levelSize; j++) {
				int tileID = level.getTileAt(i, j);
				if (tileID == 0) continue;
				int tileX = j % level.getLevelWidth();
				int tileY = j / level.getLevelWidth();
				g2.drawImage(tiles[tileID], tileX * TILES_SIZE, tileY * TILES_SIZE, TILES_SIZE, TILES_SIZE, null);
			}
		}
	}
	
	public void update() {
		
	}
	
	public Level getCurrentLevel() { return level; }
}
