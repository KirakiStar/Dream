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
					entities.add(new Mitten(x, y, (int)(Math.random() * 299)));
					break;
				case 2:
					entities.add(new Goldy(x, y, (int)(Math.random() * 299)));
					break;
				default: break;
			}
		}
	}
	
	public void update() {
		List<Integer> cd = playing.getCollisionData();
		int lw = playing.getLevelWidth();
		int lh = playing.getLevelHeight();
		
		entities.removeIf(e -> !e.isAlive());

		for (Entity e : entities) {
			if (e instanceof Enemy) {
				((Enemy) e).update(cd, lw, lh);
			} else {
				e.update();
			}
		}
		
		Player player = playing.getPlayer();
		
		checkEnemyHit(player);
		checkEntityCollide(player);
		checkEnemyAttack(player);
	}

	public void draw(Graphics2D g2) {
		for (Entity e : entities) {
			e.draw(g2);
		}
	}
	
	public void checkEnemyHit(Player player) {
		if (!player.isAttacking()) return;
		
		for (Entity e : entities) {
			if (e.isAlive() && e instanceof Enemy) {
				if (player.getAttackBox().intersects(e.getHitbox())) {
					((Enemy) e).takeDamage(player.getDamage(), player.getX());
				}
			}
		}
	}
	
	public void checkEnemyAttack(Player player) {
		for (Entity e : entities) {
			if (e.isAlive() && e instanceof Attackable attacker && (attacker.isAttacking() || attacker instanceof Goldy)) {
				if (attacker.getAttackBox().intersects(player.getHitbox())) {
					player.takeDamage(attacker.getDamage(), e.getX());
				}
			}
		}
	}

	public void checkEntityCollide(Player player) {
		if (player.getHitbox().isInvincible()) return;
		
		for (Entity e : entities) {
			if (e.isAlive() && e instanceof Enemy && !e.getHitbox().isInvincible()) {
				if (player.getHitbox().intersects(e.getHitbox())) {
					player.takeDamage(1, e.getX());
					((Enemy) e).takeDamage(player.getDamage(), player.getX());
				}
			}
		}
	}
	
	public List<Entity> getEntities() { return entities; }
}
