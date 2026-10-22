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
		WALKING(1, 2, true),
		JUMPING(2, 2, true),
		FALLING(3, 2, true),
		ATTACK(4, 2, true),
		DEATH(5, 4, true);

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
		WALKING(1, 4, true);

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
		FIREBALL(0, 2, true),
		SHADOW(1, 2, true);

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
}
