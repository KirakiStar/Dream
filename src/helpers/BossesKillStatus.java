package helpers;

public enum BossesKillStatus {
	BIG_V(5,3,false),
	SHABI(5,4,false),
	ALICE(5,5,false),
	NIGHTMARE(5,6,false);

	private final int iconRow; // in gui.png
	private final int iconCol; // in gui.png
	private boolean killed;

	BossesKillStatus (int iconRow, int iconCol, boolean killed) {
		this.iconRow = iconRow;
		this.iconCol = iconCol;
		this.killed = killed;
	}

	public static BossesKillStatus getBossAtId(int id) {
		return switch (id) {
			default -> BIG_V;
			case 1,10 -> SHABI;
			case 2,11 -> ALICE;
			case 3,12 -> NIGHTMARE;
		};
	}
	
	public static void resetBossKillStatus() {
		BIG_V.killed = false;
		SHABI.killed = false;
		ALICE.killed = false;
		NIGHTMARE.killed = false;
	}

	public int getIconRow() { return iconRow; }
	public int getIconCol() { return iconCol; }
	public boolean isKilled() { return killed; }

	public void setKilled() { this.killed = true; }
}
