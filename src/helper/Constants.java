package helper;

public class Constants {
	public static class PlayerActionID {
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int JUMPING = 2;
		public static final int JUMPING2 = 3;
		public static final int FALLING = 4;
		public static final int HIT_GROUND = 5;
		public static final int ATTACK = 6;
		public static final int ATTACK_AIR = 7;
		public static final int ATTACKED = 8;
		
		public static int getAnimationAmount(int playerAction) {
			return 0;
		}
	}
}
