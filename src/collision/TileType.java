package collision;

public enum TileType {
	NONE(0),
	SOLID(1),
	SLOPE_UP(2),
	SLOPE_DOWN(3),
	PLATFORM(4),
	WATER(5),
	LADDER(6);

	private final int id;

	TileType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static TileType fromId(int id) {
		for (TileType type : values()) {
			if (type.id == id) {
				return type;
			}
		}
		return NONE;
	}
}