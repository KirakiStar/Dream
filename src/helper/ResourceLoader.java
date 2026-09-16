package helper;

import com.google.gson.Gson;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.imageio.ImageIO;

import levels.LevelData;

public class ResourceLoader {
	public static final String ICON = "icon.png";
	public static final String TITLESCREEN = "titlescreen.png";
	public static final String PLAYER_SPRITES = "silver.png";

	public static final String TEST_LEVEL = "testLevel.png";
	public static final String TEST_MAP = "testMap.json";
	public static final String LEVEL1_SET = "level1.png";
	public static final String LEVEL1_MAP = "level1.json";

	public static BufferedImage ImagesLoader(String fileName) {
		BufferedImage img = null;
		InputStream is = ResourceLoader.class.getResourceAsStream("/resources/images/"+fileName);
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
		return img;
	}

	public static LevelData LoadLevels(String fileName) {
		Gson gson = new Gson();
		Reader reader = null;
		LevelData levelData = null;
		InputStream is = ResourceLoader.class.getResourceAsStream("/resources/json/" + fileName);
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
		return levelData;
	}
}
