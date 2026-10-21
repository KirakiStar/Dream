package helpers;

public class EntityConstants {
	public interface EntityAnimation {
		int getId();
		int getAnimationAmount();
		boolean isLooping();
	}

	public enum PlayerState implements EntityAnimation {
		IDLE(0, 4, true),
		RUNNING(1, 4, true),
		JUMPING(2, 1, false),
		JUMPING2(3, 2, false),
		FALLING(4, 2, true),
		ON_LADDER(5, 1, false),
		CLIMBING(6, 2, true),
		ATTACK(7, 2, false),
		ATTACK_AIR(8, 4, false),
		ATTACKED(9, 2, false),
		DEATH(10, 4, false);

		private final int id;
		private final int animationAmount;
		private final boolean looping;

		PlayerState(int id, int animationAmount, boolean looping) {
			this.id = id;
			this.animationAmount = animationAmount;
			this.looping = looping;
		}

		@Override public int getId() { return id; }
		@Override public int getAnimationAmount() { return animationAmount; }
		@Override public boolean isLooping() { return looping; }
	}
	
	public enum MittenState implements EntityAnimation {
		IDLE(0, 2, true),
		JUMPING(1, 1, false);

		private final int id;
		private final int animationAmount;
		private final boolean looping;

		MittenState(int id, int animationAmount, boolean looping) {
			this.id = id;
			this.animationAmount = animationAmount;
			this.looping = looping;
		}

		@Override public int getId() { return id; }
		@Override public int getAnimationAmount() { return animationAmount; }
		@Override public boolean isLooping() { return looping; }
	}
	
	public enum GoldyState implements EntityAnimation {
		IDLE(0, 2, true),
		WALKING(1, 1, true),
		ATTACK(2, 1, false);

		private final int id;
		private final int animationAmount;
		private final boolean looping;

		GoldyState(int id, int animationAmount, boolean looping) {
			this.id = id;
			this.animationAmount = animationAmount;
			this.looping = looping;
		}

		@Override public int getId() { return id; }
		@Override public int getAnimationAmount() { return animationAmount; }
		@Override public boolean isLooping() { return looping; }
	}
	
	public enum SheepState implements EntityAnimation {
		IDLE(0, 2, true),
		JUMPING(1, 1, false);

		private final int id;
		private final int animationAmount;
		private final boolean looping;

		SheepState(int id, int animationAmount, boolean looping) {
			this.id = id;
			this.animationAmount = animationAmount;
			this.looping = looping;
		}

		@Override public int getId() { return id; }
		@Override public int getAnimationAmount() { return animationAmount; }
		@Override public boolean isLooping() { return looping; }
	}
	
	public enum ShabiState implements EntityAnimation {
		IDLE(0, 4, true),
		WALKING(1, 4, true),
		JUMPING(2, 2, false),
		ATTACK(3, 4, true);

		private final int id;
		private final int animationAmount;
		private final boolean looping;

		ShabiState(int id, int animationAmount, boolean looping) {
			this.id = id;
			this.animationAmount = animationAmount;
			this.looping = looping;
		}

		@Override public int getId() { return id; }
		@Override public int getAnimationAmount() { return animationAmount; }
		@Override public boolean isLooping() { return looping; }
	}
	
	public enum AliceState implements EntityAnimation {
		IDLE(0, 4, true),
		WALKING(1, 2, true),
		ATTACK1(2, 4, false),
		ATTACK2(3, 4, false);

		private final int id;
		private final int animationAmount;
		private final boolean looping;

		AliceState(int id, int animationAmount, boolean looping) {
			this.id = id;
			this.animationAmount = animationAmount;
			this.looping = looping;
		}

		@Override public int getId() { return id; }
		@Override public int getAnimationAmount() { return animationAmount; }
		@Override public boolean isLooping() { return looping; }
	}
	
	public enum NightmareState implements EntityAnimation {
		IDLE(0, 2, true),
		JUMPING(1, 1, false);

		private final int id;
		private final int animationAmount;
		private final boolean looping;

		NightmareState(int id, int animationAmount, boolean looping) {
			this.id = id;
			this.animationAmount = animationAmount;
			this.looping = looping;
		}

		@Override public int getId() { return id; }
		@Override public int getAnimationAmount() { return animationAmount; }
		@Override public boolean isLooping() { return looping; }
	}
	
	public enum ItemType implements EntityAnimation {
		STRAWBERRY(0, 4, true, 1, null),
		BAT(1, 4, true, 0, BossesKillStatus.BIG_V),
		MAGICIAN(2, 4, true, 2, BossesKillStatus.SHABI),
		PUPPETEER(3, 4, true, 3, BossesKillStatus.ALICE),
		HYPNOTIST(4, 4, true, 5, BossesKillStatus.NIGHTMARE);

		private final int id;
		private final int animationAmount;
		private final boolean looping;
		private final int healAmount;
		private final BossesKillStatus bossId;

		ItemType(int id, int animationAmount, boolean looping, int healAmount, BossesKillStatus bossId) {
			this.id = id;
			this.animationAmount = animationAmount;
			this.looping = looping;
			this.healAmount = healAmount;
			this.bossId = bossId;
		}

		@Override public int getId() { return id; }
		@Override public int getAnimationAmount() { return animationAmount; }
		@Override public boolean isLooping() { return looping; }
		public int getHealAmount() { return healAmount; }
		public BossesKillStatus getBossId() { return bossId; }
	}
	
	public enum Obstacle implements EntityAnimation {
		FIREBALL(0, 3, true),
		SHADOW(1, 3, true);

		private final int id;
		private final int animationAmount;
		private final boolean looping;

		Obstacle(int id, int animationAmount, boolean looping) {
			this.id = id;
			this.animationAmount = animationAmount;
			this.looping = looping;
		}

		@Override public int getId() { return id; }
		@Override public int getAnimationAmount() { return animationAmount; }
		@Override public boolean isLooping() { return looping; }
	}
	
//	public enum BossesKillStatus {
//		BIG_V(5,3,false),
//		SHABI(5,4,false),
//		ALICE(5,5,false),
//		NIGHTMARE(5,6,false);
//		
//		private final int iconRow; // in gui.png
//		private final int iconCol; // in gui.png
//		private boolean collected;
//		
//		BossesKillStatus (int iconRow, int iconCol, boolean collected) {
//			this.iconRow = iconRow;
//			this.iconCol = iconCol;
//			this.collected = collected;
//		}
//		
//		public static BossesKillStatus getBossAtId(int id) {
//			return switch (id) {
//				default -> BIG_V;
//				case 1,10 -> SHABI;
//				case 2,11 -> ALICE;
//				case 3,12 -> NIGHTMARE;
//			};
//		}
//
//		public int getIconRow() { return iconRow; }
//		public int getIconCol() { return iconCol; }
//		public boolean isKilled() { return collected; }
//		
//		public void setKilled() { this.collected = true; }
//	}
}
