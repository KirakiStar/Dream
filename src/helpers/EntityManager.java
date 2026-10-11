package helpers;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import main.Game;
import entities.*;
import gamestates.Playing;

public final class EntityManager {
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
					entities.add(new Item(x, y));
					break;
				case 2:
					entities.add(new Mitten(x, y, (int)(Math.random() * Mitten.jumpInterval)));
					break;
				case 3:
					entities.add(new Goldy(x, y, (int)(Math.random() * Goldy.attackInterval)));
					break;
				case 10:
					entities.add(new Shabi(x, y));
					break;
				case 11:
					entities.add(new Alice(x, y));
					break;
				default: break;
			}
		}
	}
	
	public void update() {
		List<Integer> cd = playing.getCollisionData();
		int lw = playing.getLevelWidth();
		int lh = playing.getLevelHeight();
		Player player = playing.getPlayer();
		
		entities.removeIf(e -> !e.isAlive());

		for (Entity e : entities) {
			if (e instanceof Enemy enemy) {
				enemy.update(cd, lw, lh, player, playing.getCamera());
			} else if (e instanceof Boss boss) {
				boss.update(cd, lw, lh, player, playing.getCamera());
			} else {
				e.update();
			}
		}
		
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
			if (e.isAlive()) {
				if (e instanceof Enemy enemy) {
					if (player.getAttackBox().entityIntersects(enemy.getHitbox())) {
						enemy.takeDamage(player.getDamage(), player.getX());
					}
				} else if (e instanceof Boss boss) {
					if (player.getAttackBox().entityIntersects(boss.getHitbox())) {
						boss.takeDamage(player.getDamage());
					}
				}
			}
		}
	}
	
	public void checkEnemyAttack(Player player) {
		for (Entity e : entities) {
			if (e.isAlive() && e instanceof Attackable attacker &&
				(attacker.isAttacking() || attacker instanceof Goldy || attacker instanceof Alice)) {
				if (attacker.getAttackBox().entityIntersects(player.getHitbox())) {
					player.takeDamage(attacker.getDamage(), e.getX());
				}
			}
		}
	}

	public void checkEntityCollide(Player player) {
		for (Entity e : entities) {
			if (e.isAlive()) {
				if (e instanceof Enemy enemy) {
					if (player.getHitbox().entityIntersects(enemy.getHitbox())) {
						player.takeDamage(1, enemy.getX());
						enemy.takeDamage(1, player.getX());
					}
				} else if (e instanceof Boss boss) {
					if (player.getHitbox().entityIntersects(boss.getHitbox())) {
						player.takeDamage(1, boss.getX());
					}
				} else if (e instanceof Item item) {
					if (player.getHitbox().itemIntersects(item.getHitbox())) {
						int healAmount = item.getHealAmount();
						player.heal(healAmount);
						item.collect(player);
						if (healAmount > 1) {
							player.maxhealthUp(healAmount);
							player.attackDamageUp(1);
							player.heal(10);
						}
					}
				}
			}
		}
	}
	
	public Boss getActiveBoss() {
		for (Entity e : entities) {
			if (e instanceof Boss boss && boss.isAlive()) {
				return boss;
			}
		}
		return null;
	}
	
	public List<Entity> getEntities() { return entities; }
}
