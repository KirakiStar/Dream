package entities;

import java.util.List;

import main.Game;
import collision.CollisionChecker;

public abstract class MovingEntity extends Entity {
    protected float entitySpeed;
    protected float airSpeed = 0f;
    protected float fallSpeed = 0.5f * Game.SCALE;
    protected boolean inAir = false;
    protected boolean moving = false;
    protected final float gravity = 0.05f * Game.SCALE;

    public MovingEntity(float x, float y, int width, int height, float offsetX, float offsetY, int maxHealth) {
        super(x, y, width, height, offsetX, offsetY, maxHealth);
    }

    protected void updateXPos(float xSpeed, boolean candidateIsSlope, List<Integer> cd, int lw, int lh) {
        if (!CollisionChecker.isSolid(hitbox, x + xSpeed, y, cd, lw, lh)) {
            this.x += xSpeed;
            moving = true;
        } else if (candidateIsSlope && !CollisionChecker.isSolid(hitbox, x + xSpeed, y - (4.0f * Game.SCALE), cd, lw, lh)) {
            this.x += xSpeed;
            moving = true;
        }
    }

    protected void updateYPos(float ySpeed, List<Integer> cd, int lw, int lh) {
        boolean solidHit = CollisionChecker.isSolid(hitbox, x, y + ySpeed, cd, lw, lh);
        boolean platformHit = CollisionChecker.isPlatform(hitbox, y, y + ySpeed, ySpeed, cd, lw, lh);

        if (!solidHit && !platformHit) {
            this.y += ySpeed;
            if (inAir) {
                airSpeed += gravity;
            }
            moving = true;
        } else {
            this.y = CollisionChecker.GetYFromBlocks(hitbox, y + ySpeed, airSpeed);

            if (airSpeed > 0) {
                resetInAir();
            } else {
                airSpeed = fallSpeed;
            }
        }
    }

    protected void updateGroundPosition(float xSpeed, List<Integer> cd, int lw, int lh, boolean wantsToDropPlatform) {
        float slopeFloorY = CollisionChecker.getSlopeY(hitbox, x + xSpeed, y, cd, lw, lh);

        if (xSpeed != 0) {
            updateXPos(xSpeed, slopeFloorY != -1, cd, lw, lh);
        }

        if (slopeFloorY != -1 && airSpeed >= 0) {
            y = slopeFloorY - hitbox.getOffsetY() - hitbox.getHeight();
            resetInAir();
        } else {
            boolean solidGround = CollisionChecker.isSolid(hitbox, x, y + 1.0f, cd, lw, lh);
            boolean platformGround = CollisionChecker.isPlatform(hitbox, y, y + 1.0f, 1.0f, cd, lw, lh);

            if ((solidGround || platformGround) && airSpeed >= 0) {
                if (wantsToDropPlatform && platformGround && !solidGround) {
                    inAir = true;
                    y += 3.0f;
                    airSpeed = fallSpeed;
                    dropFromEdgeOrPlatform();
                } else {
                    resetInAir();
                }
            } else if (!inAir) {
                inAir = true;
                dropFromEdgeOrPlatform();
            }
        }

        if (inAir) {
            updateYPos(airSpeed, cd, lw, lh);
        }
    }

    protected void dropFromEdgeOrPlatform() {}

    protected void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }
}