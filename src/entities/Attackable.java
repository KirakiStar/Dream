package entities;

import collision.Hitbox;

public interface Attackable {
	void attack();
	Hitbox getAttackBox();
	int getDamage();
	boolean isAttacking();
}
