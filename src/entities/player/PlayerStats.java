package entities.player;

import helpers.Settings;

public final class PlayerStats {
	private boolean cheating;
	private int maxHealth;
	private float playerSpeed;
	private float jumpSpeed;
	private int maxJump;
	private int attackDamage;
	
	private int currentMaxHealth;
	
	PlayerStats(int currentMaxHealth) {
		setCurrentMaxHealth(currentMaxHealth);
		resetCheat();
	}
	
	public void setCurrentMaxHealth(int currentMaxHealth) { this.currentMaxHealth = currentMaxHealth; }

	public int getMaxHealth() { return maxHealth; }
	public float getPlayerSpeed() { return playerSpeed; }
	public float getJumpSpeed() { return jumpSpeed; }
	public int getMaxJump() { return maxJump; }
	public int getAttackDamage() { return attackDamage; }
	
	public void activateCheat() {
		if (cheating) resetCheat();
		else {
			cheating = true;
			maxHealth = 77;
			playerSpeed = 5f * Settings.SCALE;
			jumpSpeed = -2.3f * Settings.SCALE;
			maxJump = 77;
			attackDamage = 77;
		}
	}
	
	private void resetCheat() {
		cheating = false;
		maxHealth = currentMaxHealth;
		playerSpeed = 1.5f * Settings.SCALE;
		jumpSpeed = -2.3f * Settings.SCALE;
		maxJump = 2;
		attackDamage = 1;
	}
}
