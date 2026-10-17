package entities.player;

import helpers.Settings;

public final class PlayerStats {
	private boolean cheating;
	private int maxHealth;
	private float playerSpeed;
	private float jumpSpeed;
	private int maxJump;
	private int attackDamage;
	
	private int currentMaxHealth = 0;
	private int currentAttack = 0;
	
	PlayerStats(int defaultMaxHealth, int defaultAttack) {
		MaxHealthUp(defaultMaxHealth);
		attackDamageUp(defaultAttack);
		resetCheat();
	}
	
	public void MaxHealthUp(int amount) {
		this.currentMaxHealth += amount;
		resetCheat();
	}
	public void attackDamageUp(int amount) {
		this.currentAttack += amount;
		resetCheat();
	}

	public int getMaxHealth() { return maxHealth; }
	public float getPlayerSpeed() { return playerSpeed; }
	public float getJumpSpeed() { return jumpSpeed; }
	public int getMaxJump() { return maxJump; }
	public int getAttackDamage() { return attackDamage; }
	
	public void TheSevenDeadlyCheats() {
		if (cheating) resetCheat();
		else {
			cheating = true;
			maxHealth = 77;
			playerSpeed = 7f * Settings.getCurrentScale();
			jumpSpeed = -4f * Settings.getCurrentScale();
			maxJump = 77;
			attackDamage = 7;
		}
	}
	
	private void resetCheat() {
		cheating = false;
		maxHealth = currentMaxHealth;
		playerSpeed = 1.5f * Settings.getCurrentScale();
		jumpSpeed = -2.3f * Settings.getCurrentScale();
		maxJump = 2;
		attackDamage = currentAttack;
	}
}
