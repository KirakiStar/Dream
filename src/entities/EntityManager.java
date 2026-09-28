package entities;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import main.Game;
import gamestates.Playing;

public class EntityManager {
	private Playing playing;
	private List<Integer> entityData;
	private int levelWidth;
	private List<Entity> entities;

	public EntityManager(Playing playing) {
		this.playing = playing;
		this.entities = new ArrayList<>();
	}

	public void loadEntities() {
		entities.clear();
		this.entityData = playing.getEntityData();
		this.levelWidth = playing.getLevelWidth();
		
		for (int i = 0; i < entityData.size(); i++) {
			int tileID = entityData.get(i);
			if (tileID == 0) continue;

			int tileX = i % levelWidth;
			int tileY = i / levelWidth;
			float x = tileX * Game.TILES_SIZE;
			float y = tileY * Game.TILES_SIZE;
			
			switch (tileID) {
				case 1:
					entities.add(new Mitten(x, y));
					break;
				default: break;
			}
		}
	}
	
	public void update() {
		List<Integer> cd = playing.getCollisionData();
		int lw = playing.getLevelWidth();
		int lh = playing.getLevelHeight();

		for (Entity e : entities) {
			if (e instanceof Enemy) {
				((Enemy) e).update(cd, lw, lh);
			} else {
				e.update();
			}
		}
	}

	public void draw(Graphics2D g2) {
		for (Entity e : entities) {
			e.draw(g2);
		}
	}

	public List<Entity> getEntities() { return entities; }
}
