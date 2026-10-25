package helpers;

import com.google.gson.Gson;

import java.util.List;
import java.util.ArrayList;
import java.io.Reader;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import levels.LevelData;

public final class ResourceLoader {
	
	public static class SpriteSheet {
		private String fileName;
		private int rows;
		private int columns;
		private int width;
		private int height;

		public SpriteSheet(String fileName, int rows, int columns, int width, int height) {
			this.fileName = fileName;
			this.rows = rows;
			this.columns = columns;
			this.width = width;
			this.height = height;
		}

		public String getFileName() { return fileName; }
		public int getRow() { return rows; }
		public int getColumn() { return columns; }
		public int getWidth() { return width; }
		public int getHeight() { return height; }
	}
	
	public static final String ICON = "icon.png";
	public static final String TITLESCREEN = "titlescreen.png";
	public static final String BLACKSCREEN = "330049.png";
	public static BufferedImage[][] fontCharacters;
	
	public static final String[] INTRO_CUTSCENE = {"house2.png", "house1.png"};
	public static final String[] ENDING_CUTSCENE = {"nightmare.png"};
	
	public static final SpriteSheet GUI_ITEMS = new SpriteSheet("gui.png", 9, 10, 32, 32);
	public static final SpriteSheet TS_BUTTONS = new SpriteSheet("buttons.png", 5, 1, 128, 64);
	public static final SpriteSheet PLAYER_SPRITES = new SpriteSheet("silver.png", 11, 4, 64, 64);
	public static final SpriteSheet ITEM_SPRITES = new SpriteSheet("items.png", 5, 4, 64, 64);
	
	public static final SpriteSheet MITTEN_SPRITES = new SpriteSheet("mitten.png", 4, 4, 64, 64);
	public static final SpriteSheet GOLDY_SPRITES = new SpriteSheet("goldy.png", 4, 4, 64, 64);
	public static final SpriteSheet SHADOW_SPRITES = new SpriteSheet("silver.png", 11, 4, 64, 64);
	public static final SpriteSheet WHITESHEEP_SPRITES = new SpriteSheet("mitten.png", 4, 4, 64, 64);
	public static final SpriteSheet BLACKSHEEP_SPRITES = new SpriteSheet("goldy.png", 4, 4, 64, 64);
	public static final SpriteSheet SHADOWFIRE_SPRITES = new SpriteSheet("magic.png", 2, 2, 40, 40);
	
	public static final SpriteSheet SHABI_SPRITES = new SpriteSheet("shabi.png", 6, 4, 96, 96);
	public static final SpriteSheet ALICE_SPRITES = new SpriteSheet("alice.png", 6, 4, 96, 96);
	public static final SpriteSheet NIGHTMARE_SPRITES = new SpriteSheet("nightmare.png", 3, 4, 128, 128);

	public static final List<String> levelFiles = new ArrayList<>(List.of(
													"house1", "level1", "level1_1", "level1_2", 
													"level2_1", "level2_2", "level2_3", 
													"level3_1", "level3_2", "backAt1", "house2"));

	public static BufferedImage[][] loadSpriteSheet(SpriteSheet name) {
		int rows = name.getRow();
		int columns = name.getColumn();
		int imgWidth = name.getWidth();
		int imgHeight = name.getHeight();
		BufferedImage img = ImagesLoader(name.getFileName());
		BufferedImage[][] temp = new BufferedImage[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				temp[i][j] = img.getSubimage(j * imgWidth, i * imgHeight, imgWidth, imgHeight);
			}
		}
		
		return temp;
	}
	
	public static BufferedImage ImagesLoader(String fileName) {
		BufferedImage img = null;
		InputStream is = ResourceLoader.class.getResourceAsStream("/resources/images/" + fileName);
		try {
			img = ImageIO.read(is);

		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		if (img == null)
			System.err.println("Failed to load image: " + fileName);
		return img;
	}

	public static LevelData LoadLevels(String fileName) {
		Gson gson = new Gson();
		Reader reader = null;
		LevelData levelData = null;
		InputStream is = ResourceLoader.class.getResourceAsStream("/resources/json/" + fileName + ".json");
		try {
			reader = new InputStreamReader(is);
			levelData = gson.fromJson(reader, LevelData.class);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		if (levelData == null)
			System.err.println("Failed to load level: " + fileName + "json");
		return levelData;
	}
}
