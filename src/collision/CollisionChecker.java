package collision;

import java.util.List;

import main.Game;

public final class CollisionChecker {
    public static final int NONE = 0;
    public static final int SOLID = 1;
    public static final int SLOPE_UP = 2;
    public static final int SLOPE_DOWN = 3;
    public static final int PLATFORM = 4;
    public static final int WATER = 5;
    public static final int LADDER = 6;

    private CollisionChecker() { }

    public static boolean isSolid(Hitbox hitbox, float nextX, float nextY, List<Integer> collisionData, int levelWidth, int levelHeight) {
        int leftTile   = (int) Math.floor((nextX + hitbox.getOffsetX()) / Game.TILES_SIZE);
        int rightTile  = (int) (nextX + hitbox.getOffsetX() + hitbox.getWidth() - 1) / Game.TILES_SIZE;
        int topTile    = (int) Math.floor((nextY + hitbox.getOffsetY()) / Game.TILES_SIZE);
        int bottomTile = (int) (nextY + hitbox.getOffsetY() + hitbox.getHeight() - 1) / Game.TILES_SIZE;

        for (int tileX = leftTile; tileX <= rightTile; tileX++) {
            for (int tileY = topTile; tileY <= bottomTile; tileY++) {
                int tileType = getTileType(tileX, tileY, collisionData, levelWidth, levelHeight);
                if (tileType == SOLID) {
                    return true;
                }
            }
        }
        return false;
    }

	public static boolean isPlatform(Hitbox hitbox, float currentY, float nextY, float airSpeed, List<Integer> collisionData, int levelWidth, int levelHeight) {
		if (airSpeed <= 0) return false;

		float feetY = nextY + hitbox.getOffsetY() + hitbox.getHeight();
		float prevFeetY = currentY + hitbox.getOffsetY() + hitbox.getHeight();

		int leftTile  = (int) Math.floor((hitbox.getBounds().x) / Game.TILES_SIZE);
		int rightTile = (int) (hitbox.getBounds().x + hitbox.getWidth() - 1) / Game.TILES_SIZE;
		int tileY     = (int) Math.floor(feetY / Game.TILES_SIZE);

		for (int tileX = leftTile; tileX <= rightTile; tileX++) {
			int tileType = getTileType(tileX, tileY, collisionData, levelWidth, levelHeight);

			if (tileType == PLATFORM || tileType == LADDER) {
				float platformTop = tileY * Game.TILES_SIZE;
				if (prevFeetY <= platformTop + 2.0f) {
					return true;
				}
			}
		}
		return false;
	}

    public static float getSlopeY(Hitbox hitbox, float nextX, float nextY, List<Integer> collisionData, int levelWidth, int levelHeight) {
		int leftTile   = (int) Math.floor((nextX + hitbox.getOffsetX()) / Game.TILES_SIZE);
		int rightTile  = (int) Math.floor((nextX + hitbox.getOffsetX() + hitbox.getWidth() - 1) / Game.TILES_SIZE);
		int topTile    = (int) Math.floor((nextY + hitbox.getOffsetY()) / Game.TILES_SIZE);
		int bottomTile = (int) Math.floor((nextY + hitbox.getOffsetY() + hitbox.getHeight()) / Game.TILES_SIZE);

		float slopeY = -1f;

		for (int tileX = leftTile; tileX <= rightTile; tileX++) {
			for (int tileY = topTile; tileY <= bottomTile; tileY++) {

				int tileType = getTileType(tileX, tileY, collisionData, levelWidth, levelHeight);

				if (tileType == SLOPE_UP || tileType == SLOPE_DOWN) {
					float xToCheck;
					if (tileType == SLOPE_UP) {
						xToCheck = Math.min(nextX + hitbox.getOffsetX() + hitbox.getWidth() - 1, (tileX + 1) * Game.TILES_SIZE - 0.01f);
					} else {
						xToCheck = Math.max(nextX + hitbox.getOffsetX(), tileX * Game.TILES_SIZE);
					}

					float xInTile = xToCheck - (tileX * Game.TILES_SIZE);
					float yOffsetInTile = (tileType == SLOPE_UP) ? (Game.TILES_SIZE - xInTile) : xInTile;

					float calculatedSlopeY = (tileY * Game.TILES_SIZE) + yOffsetInTile;

					if (slopeY == -1f || calculatedSlopeY < slopeY) {
						slopeY = calculatedSlopeY;
					}
				}
			}
		}

		return slopeY;
	}

    public static boolean isLadder(Hitbox hitbox, List<Integer> collisionData, int levelWidth, int levelHeight) {
		int centerX = (int) Math.floor((hitbox.getBounds().x + (hitbox.getWidth() / 2f)) / Game.TILES_SIZE);
		int bottomY = (int) Math.floor((hitbox.getBounds().y + hitbox.getHeight() - 1f) / Game.TILES_SIZE);

		return getTileType(centerX, bottomY, collisionData, levelWidth, levelHeight) == LADDER;
	}

    public static boolean isWater(Hitbox hitbox, List<Integer> collisionData, int levelWidth, int levelHeight) {
        int leftTile   = (int) Math.floor((hitbox.getBounds().x) / Game.TILES_SIZE);
        int rightTile  = (int) (hitbox.getBounds().x + hitbox.getWidth() - 1) / Game.TILES_SIZE;
        int topTile    = (int) Math.floor((hitbox.getBounds().y) / Game.TILES_SIZE);
        int bottomTile = (int) Math.floor((hitbox.getBounds().y + hitbox.getHeight() - 1) / Game.TILES_SIZE);

        for (int tileX = leftTile; tileX <= rightTile; tileX++) {
            for (int tileY = topTile; tileY <= bottomTile; tileY++) {
                if (getTileType(tileX, tileY, collisionData, levelWidth, levelHeight) == WATER) {
                    return true;
                }
            }
        }
        return false;
    }
	
	public static float GetYFromBlocks(Hitbox hitbox, float nextY, float airSpeed) {
		if (airSpeed > 0) {
			float feetY = nextY + hitbox.getOffsetY() + hitbox.getHeight();
			int tileY = (int) Math.floor((feetY - 0.01f) / Game.TILES_SIZE);
			float yIndex = tileY * Game.TILES_SIZE;
			float yOffset = hitbox.getOffsetY() + hitbox.getHeight();

			return yIndex - yOffset;
		} else {
			float headY = nextY + hitbox.getOffsetY();
			int tileY = (int) Math.floor(headY / Game.TILES_SIZE);
			float yIndex = tileY * Game.TILES_SIZE;
			float yOffset = hitbox.getOffsetY();

			return yIndex + Game.TILES_SIZE - yOffset;
		}
	}

    public static int getTileType(int tileX, int tileY, List<Integer> collisionData, int levelWidth, int levelHeight) {
        if (tileX < 0 || tileX >= levelWidth || tileY < 0 || tileY >= levelHeight) {
            return SOLID;
        }

        int index = tileX + tileY * levelWidth;
        if (index < 0 || index >= collisionData.size()) {
            return SOLID;
        }

        return collisionData.get(index);
    }
}