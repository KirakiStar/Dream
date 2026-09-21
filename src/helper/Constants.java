package helper;

public class Constants {
	public interface EntityState {
		int getId();
		int getAnimationAmount();
	}

	public enum PlayerState implements EntityState {
		IDLE(0, 4, true),
		RUNNING(1, 4, true),
		JUMPING(2, 1, false),
		JUMPING2(3, 2, false),
		FALLING(4, 1, false),
		HIT_GROUND(5, 1, false),
		CLIMBING(6, 1, false),
		ATTACK(7, 1, false),
		ATTACK_AIR(8, 1, false),
		ATTACKED(9, 1, false),
		DEATH(10, 1, false);

		private final int id;
		private final int animationAmount;
		private final boolean looping;

		PlayerState(int id, int animationAmount, boolean looping) {
			this.id = id;
			this.animationAmount = animationAmount;
			this.looping = looping;
		}

		@Override
		public int getId() { return id; }

		@Override
		public int getAnimationAmount() { return animationAmount; }
		
		public boolean isLooping() { return looping; }
	}
	
//	public static class PlayerActionID {
//		public static final int IDLE = 0;
//		public static final int RUNNING = 1;
//		public static final int JUMPING = 2;
//		public static final int JUMPING2 = 3;
//		public static final int FALLING = 4;
//		public static final int HIT_GROUND = 5;
//		public static final int CLIMBING = 6;
//		public static final int ATTACK = 7;
//		public static final int ATTACK_AIR = 8;
//		public static final int ATTACKED = 9;
//		public static final int DEATH = 10;
//		
//		public static int getAnimationAmount(int playerAction) {
//			switch (playerAction) {
//				case IDLE:
//				case RUNNING:
//					return 4;
//				default: return 1;
//			}
//		}
//	}
	
	
}
