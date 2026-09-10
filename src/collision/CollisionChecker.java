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

    public static int getTileType(int tileX, int tileY, List<Integer> collisionData, int levelWidth, int levelHeight) {
        if (tileX < 0 || tileX >= levelWidth ||
			tileY < 0 || tileY >= levelHeight) {
            return SOLID;
        }

        int index = tileX + tileY * levelWidth;
        if (index < 0 || index >= collisionData.size()) {
            return SOLID;
        }

        return collisionData.get(index);
    }
}
