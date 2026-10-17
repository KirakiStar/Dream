package entities;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.enemies.*;
import entities.bosses.*;
import entities.player.Player;
import gamestates.Playing;
import helpers.Settings;
import helpers.EntityConstants.ItemType;
import static helpers.BossesKillStatus.*;

public final class EntityManager {
	private Playing playing;
	private List<Integer> entityData;
	private int levelWidth;
	private List<Entity> entities;
	private List<Entity> pendingEntities = new ArrayList<>();

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
			float x = tileX * Settings.TILES_SIZE;
			float y = tileY * Settings.TILES_SIZE;
			
			Random random = new Random();
			switch (tileID) {
				case 1 -> entities.add(new Item(x, y));
				case 2 -> entities.add(new Mitten(x, y, random.nextInt(Mitten.jumpInterval)));
				case 3 -> entities.add(new Goldy(x, y, random.nextInt(Goldy.attackInterval)));
//				case 4 -> entities.add(new Shadow(x,y)); //stand still
//				case 5 -> entities.add(new JumpSheep(x, y)); //jump left and right
//				case 6 -> entities.add(new StalkSheep(x, y)); // always walks towards player
				case 4 -> entities.add(new Mitten(x, y, random.nextInt(Mitten.jumpInterval)));
				case 5 -> entities.add(new Mitten(x, y, random.nextInt(Mitten.jumpInterval)));
				case 6 -> entities.add(new Goldy(x, y, random.nextInt(Goldy.attackInterval)));
				case 9 -> {
					if (!BIG_V.isKilled())
						entities.add(new Item(x, y, ItemType.BAT));
				}
				case 10 -> {
					if (!SHABI.isKilled()) {
						Shabi shabi = new Shabi(x, y);
						shabi.setPlaying(playing);
						entities.add(shabi);
					}
				}
				case 11 -> {
					if (!ALICE.isKilled()) {
						Alice alice = new Alice(x, y);
						alice.setPlaying(playing);
						entities.add(alice);
					}
				}
				default -> { }
			}
		}
	}
	
	public void addEntity(Entity newbie) {
		pendingEntities.add(newbie);
	}
	
	public void removeWantedEntity(Class<?> wantedEntity) {
		entities.removeIf(e -> wantedEntity.isInstance(e));
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
				if (playing.getCamera().isLocked())
					boss.update(cd, lw, lh, player, playing.getCamera());
			} else {
				e.update();
			}
		}
		
		if (!pendingEntities.isEmpty()) {
			entities.addAll(pendingEntities);
			pendingEntities.clear();
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
				} else if (e instanceof Item item && !(item.getItemType() == ItemType.STRAWBERRY)) {
					if (player.getAttackBox().entityIntersects(item.getHitbox())) {
						item.collect(player);
					}
				}
			}
		}
	}
	
	public void checkEnemyAttack(Player player) {
		for (Entity e : entities) {
			if (e.isAlive() && e instanceof Attackable attacker &&
				(attacker.isAttacking() || attacker instanceof Goldy || attacker instanceof Alice)) {
				if (attacker.getAttackBox() != null && attacker.getAttackBox().entityIntersects(player.getHitbox())) {
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
						item.collect(player);
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
