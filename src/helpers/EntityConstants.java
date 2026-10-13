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
	
	public enum ShabiState implements EntityAnimation {
		IDLE(0, 4, true),
		WALKING(1, 2, true),
		ATTACK1(2, 4, false),
		ATTACK2(3, 4, false);

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
	
	public enum ItemType implements EntityAnimation {
		STRAWBERRY(0, 4, true, 1, null),
		MAGICIAN(1, 1, false, 2, BossId.SHABI),
		PUPPETEER(2, 1, false, 3, BossId.ALICE),
		HYPNOTIST(3, 1, false, 5, BossId.NIGHTMARE);

		private final int id;
		private final int animationAmount;
		private final boolean looping;
		private final int healAmount;
		private final BossId bossId;

		ItemType(int id, int animationAmount, boolean looping, int healAmount, BossId bossId) {
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
		public BossId getBossId() { return bossId; }
	}
	
	public enum BossId {
		SHABI(10,5,4),
		ALICE(11,5,5),
		NIGHTMARE(12,5,6);
		
		private final int id;
		private final int iconRow;
		private final int iconCol;
		
		BossId (int id, int iconRow, int iconCol) {
			this.id = id;
			this.iconRow = iconRow;
			this.iconCol = iconCol;
		}
		
		public int getId() { return id; }
		public int getIconRow() { return iconRow; }
		public int getIconCol() { return iconCol; }
	}
}
