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
}
