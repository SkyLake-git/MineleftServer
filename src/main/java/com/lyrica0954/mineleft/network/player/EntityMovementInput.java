package com.lyrica0954.mineleft.network.player;

public class EntityMovementInput {
	public float movementSideways;

	public float movementForward;

	public boolean pressingForward;

	public boolean pressingBack;

	public boolean pressingLeft;

	public boolean pressingRight;

	public boolean jumping;

	public boolean sneaking;

	public boolean pressingSprint;

	public EntityMovementInput() {
		this.movementSideways = 0.0f;
		this.movementForward = 0.0f;
		this.pressingForward = false;
		this.pressingBack = false;
		this.pressingLeft = false;
		this.pressingRight = false;
		this.jumping = false;
		this.sneaking = false;
		this.pressingSprint = false;
	}

	public void tick(boolean shouldSlowdown) {
		this.movementForward = this.pressingForward == this.pressingBack ? 0.0f : (this.pressingForward ? 1.0f : -1.0f);

		this.movementSideways = this.pressingLeft == this.pressingRight ? 0.0f : (this.pressingLeft ? 1.0f : -1.0f);

		if (shouldSlowdown) {
			this.movementForward *= 0.3f;
			this.movementSideways *= 0.3f;
		}
	}
}
