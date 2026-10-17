package helpers;

public enum BossesKillStatus {
	BIG_V(5,3,false),
	SHABI(5,4,false),
	ALICE(5,5,false),
	NIGHTMARE(5,6,false);

	private final int iconRow; // in gui.png
	private final int iconCol; // in gui.png
	private boolean collected;

	BossesKillStatus (int iconRow, int iconCol, boolean collected) {
		this.iconRow = iconRow;
		this.iconCol = iconCol;
		this.collected = collected;
	}

	public static BossesKillStatus getBossAtId(int id) {
		return switch (id) {
			default -> BIG_V;
			case 1,10 -> SHABI;
			case 2,11 -> ALICE;
			case 3,12 -> NIGHTMARE;
		};
	}

	public int getIconRow() { return iconRow; }
	public int getIconCol() { return iconCol; }
	public boolean isKilled() { return collected; }

	public void setKilled() { this.collected = true; }
}
