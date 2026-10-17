package collision;

import java.util.List;

import helpers.Settings;

public final class CollisionChecker {

	private CollisionChecker() { }

	public static boolean isSolid(Hitbox hitbox, float nextX, float nextY, List<Integer> collisionData, int levelWidth, int levelHeight) {
		int leftTile   = (int) Math.floor((nextX + hitbox.getOffsetX()) / Settings.getTileSize());
		int rightTile  = (int) (nextX + hitbox.getOffsetX() + hitbox.getWidth() - 1) / Settings.getTileSize();
		int topTile    = (int) Math.floor((nextY + hitbox.getOffsetY()) / Settings.getTileSize());
		int bottomTile = (int) (nextY + hitbox.getOffsetY() + hitbox.getHeight() - 1) / Settings.getTileSize();

		for (int tileX = leftTile; tileX <= rightTile; tileX++) {
			for (int tileY = topTile; tileY <= bottomTile; tileY++) {
				TileType tileType = getTileType(tileX, tileY, collisionData, levelWidth, levelHeight);
				if (tileType == TileType.SOLID) {
					return true;
				}
			}
		}
		return false;
	}
	
	//camera lock
	public static boolean isSolid(Hitbox hitbox, float nextX, float nextY, List<Integer> collisionData, int levelWidth, int levelHeight, display.Camera camera) {
		if (camera != null && camera.isLocked()) {
			float leftEdge = nextX + hitbox.getOffsetX();
			float rightEdge = leftEdge + hitbox.getWidth() - 1;

			if (leftEdge < camera.getLockMinX() || rightEdge > camera.getLockMaxX()) {
				return true;
			}
		}
		
		return isSolid(hitbox, nextX, nextY, collisionData, levelWidth, levelHeight);
	}

	// for player
	public static boolean isPlatform(Hitbox hitbox, float currentY, float nextY, float airSpeed, List<Integer> collisionData, int levelWidth, int levelHeight) {
		return isPlatform(hitbox, hitbox.getOwner().getX(), currentY, nextY, airSpeed, collisionData, levelWidth, levelHeight);
	}
	
	// for other entites (check future x)
	public static boolean isPlatform(Hitbox hitbox, float customX, float currentY, float nextY, float airSpeed, List<Integer> collisionData, int levelWidth, int levelHeight) {
		if (airSpeed <= 0) return false;

		float feetY = nextY + hitbox.getOffsetY() + hitbox.getHeight();
		float prevFeetY = currentY + hitbox.getOffsetY() + hitbox.getHeight();

		float customHitboxX = customX + hitbox.getOffsetX();
		int leftTile  = (int) Math.floor(customHitboxX / Settings.getTileSize());
		int rightTile = (int) (customHitboxX + hitbox.getWidth() - 1) / Settings.getTileSize();
		int tileY     = (int) Math.floor(feetY / Settings.getTileSize());

		for (int tileX = leftTile; tileX <= rightTile; tileX++) {
			TileType tileType = getTileType(tileX, tileY, collisionData, levelWidth, levelHeight);

			boolean isTopLadder = false;
			if (tileType == TileType.LADDER) {
				TileType tileAbove = getTileType(tileX, tileY - 1, collisionData, levelWidth, levelHeight);
				if (tileAbove != TileType.LADDER) {
					isTopLadder = true;
				}
			}

			if (tileType == TileType.PLATFORM || isTopLadder) {
				float platformTop = tileY * Settings.getTileSize();
				float error = 5.0f;
				if (prevFeetY <= platformTop + error) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static float getSlopeY(Hitbox hitbox, float nextX, float nextY, List<Integer> collisionData, int levelWidth, int levelHeight) {
		int leftTile   = (int) Math.floor((nextX + hitbox.getOffsetX()) / Settings.getTileSize());
		int rightTile  = (int) Math.floor((nextX + hitbox.getOffsetX() + hitbox.getWidth() - 1) / Settings.getTileSize());
		int topTile    = (int) Math.floor((nextY + hitbox.getOffsetY()) / Settings.getTileSize());
		int bottomTile = (int) Math.floor((nextY + hitbox.getOffsetY() + hitbox.getHeight() + 2f * Settings.getCurrentScale()) / Settings.getTileSize());

		float slopeY = -1f;

		for (int tileX = leftTile; tileX <= rightTile; tileX++) {
			for (int tileY = topTile; tileY <= bottomTile; tileY++) {

				TileType tileType = getTileType(tileX, tileY, collisionData, levelWidth, levelHeight);

				if (tileType == TileType.SLOPE_UP || tileType == TileType.SLOPE_DOWN) {
					float xToCheck;
					if (tileType == TileType.SLOPE_UP) {
						xToCheck = Math.min(nextX + hitbox.getOffsetX() + hitbox.getWidth() - 1, (tileX + 1) * Settings.getTileSize() - 0.01f);
					} else {
						xToCheck = Math.max(nextX + hitbox.getOffsetX(), tileX * Settings.getTileSize());
					}

					float xInTile = xToCheck - (tileX * Settings.getTileSize());
					float yOffsetInTile = (tileType == TileType.SLOPE_UP) ? (Settings.getTileSize() - xInTile) : xInTile;

					float calculatedSlopeY = (tileY * Settings.getTileSize()) + yOffsetInTile;

					if (slopeY == -1f || calculatedSlopeY < slopeY) {
						slopeY = calculatedSlopeY;
					}
				}
			}
		}

		return slopeY;
	}

	public static boolean isLadder(Hitbox hitbox, List<Integer> collisionData, int levelWidth, int levelHeight) {
		int centerX = (int) Math.floor((hitbox.getBounds().x + (hitbox.getWidth() / 2f)) / Settings.getTileSize());
		int bottomY = (int) Math.floor((hitbox.getBounds().y + hitbox.getHeight() - 1f) / Settings.getTileSize());

		return getTileType(centerX, bottomY, collisionData, levelWidth, levelHeight) == TileType.LADDER;
	}

	public static boolean isWater(Hitbox hitbox, List<Integer> collisionData, int levelWidth, int levelHeight) {
		int leftTile   = (int) Math.floor((hitbox.getBounds().x) / Settings.getTileSize());
		int rightTile  = (int) (hitbox.getBounds().x + hitbox.getWidth() - 1) / Settings.getTileSize();
		int topTile    = (int) Math.floor((hitbox.getBounds().y) / Settings.getTileSize());
		int bottomTile = (int) Math.floor((hitbox.getBounds().y + hitbox.getHeight() - 1) / Settings.getTileSize());

		for (int tileX = leftTile; tileX <= rightTile; tileX++) {
			for (int tileY = topTile; tileY <= bottomTile; tileY++) {
				if (getTileType(tileX, tileY, collisionData, levelWidth, levelHeight) == TileType.WATER) {
					return true;
				}
			}
		}
		return false;
	}

	public static float GetYFromBlocks(Hitbox hitbox, float nextY, float airSpeed) {
		if (airSpeed > 0) {
			float feetY = nextY + hitbox.getOffsetY() + hitbox.getHeight();
			int tileY = (int) Math.floor(feetY / Settings.getTileSize());
			float yIndex = tileY * Settings.getTileSize();
			float yOffset = hitbox.getOffsetY() + hitbox.getHeight();

			return yIndex - yOffset;
		} else {
			float headY = nextY + hitbox.getOffsetY();
			int tileY = (int) Math.floor(headY / Settings.getTileSize());
			float yIndex = tileY * Settings.getTileSize();
			float yOffset = hitbox.getOffsetY();

			return yIndex + Settings.getTileSize() - yOffset;
		}
	}

	public static TileType getTileType(int tileX, int tileY, List<Integer> collisionData, int levelWidth, int levelHeight) {
		if (tileY >= levelHeight + 2) {
			return TileType.WATER;
		}
		
		if (tileY < 0 || tileY >= levelHeight) {
			return TileType.NONE;
		}

		if (tileX < 0 || tileX >= levelWidth) {
			return TileType.SOLID;
		}

		int index = tileX + tileY * levelWidth;
		if (index < 0 || index >= collisionData.size()) {
			return TileType.SOLID;
		}

		return TileType.fromId(collisionData.get(index));
	}
}