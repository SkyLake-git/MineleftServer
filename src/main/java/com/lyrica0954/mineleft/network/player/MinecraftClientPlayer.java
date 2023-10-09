package com.lyrica0954.mineleft.network.player;

import com.lyrica0954.mineleft.mc.level.WorldInterface;
import com.lyrica0954.mineleft.mc.math.Vec3d;

public class MinecraftClientPlayer extends MinecraftEntityLiving {

	public EntityMovementInput keyboardInput;

	protected boolean usingItem;

	protected int continueSprintTicks;

	protected int sneakTicks;

	protected float baseMovementSpeed;

	protected float lastMovementForward;

	public MinecraftClientPlayer(WorldInterface world, Vec3d position, double sizeWidth, double sizeHeight) {
		super(world, position, sizeWidth, sizeHeight);
		this.keyboardInput = new EntityMovementInput();
		this.usingItem = false;
		this.continueSprintTicks = 0;
		this.baseMovementSpeed = 0.1f;
		this.flyingSpeed = 0.02f;
		this.lastMovementForward = 0.0f;
	}


	public float getBaseMovementSpeed() {
		return baseMovementSpeed;
	}

	public void setBaseMovementSpeed(float baseMovementSpeed) {
		this.baseMovementSpeed = baseMovementSpeed;
	}

	public boolean isUsingItem() {
		return usingItem;
	}

	public void setUsingItem(boolean usingItem) {
		this.usingItem = usingItem;
	}

	public boolean isWalking() {
		return this.touchingWater ? Math.abs(this.keyboardInput.movementForward) > 1.0e-5f : Math.abs(this.keyboardInput.movementForward) >= 0.8f;
	}

	@Override
	public void updateMovement() {
		boolean lastSprinting = this.isSprinting();
		boolean sneakAlternate = this.isSneaking() && !this.keyboardInput.sneaking;
		boolean sprintAlternate = this.isSprinting() && !this.keyboardInput.pressingSprint;
		this.sneaking = this.keyboardInput.sneaking;
		this.jumping = this.keyboardInput.jumping;

		if (this.isSneaking()) {
			++this.sneakTicks;
		} else {
			this.sneakTicks = 0;
		}

		this.keyboardInput.tick();

		if (this.isUsingItem()) {
			this.keyboardInput.movementSideways *= 0.2f;
			this.keyboardInput.movementForward *= 0.2f;
		}

		//todo: check submerged, food
		if (!this.isSprinting() && this.keyboardInput.pressingSprint && !this.isUsingItem()) {
			this.setSprinting(true);
		}

		if (this.isSprinting() && (!this.keyboardInput.pressingForward)) {
			this.setSprinting(false);
		}

		if (this.touchingWater && this.keyboardInput.sneaking) {
			this.setMotion(this.getMotion().subtract(0, 0.04d, 0));
		}

		this.movementSpeed = this.baseMovementSpeed;
		if (this.isSprinting()) {
			this.movementSpeed *= 1.3f;
		}

		if (Math.abs(this.keyboardInput.movementForward) > 1e-4 || Math.abs(this.keyboardInput.movementSideways) > 1e-4) {
			Vec3d movementInput = new Vec3d(this.keyboardInput.movementSideways, 0d, this.keyboardInput.movementForward).normalize();

			if (this.isSneaking() || sneakAlternate) {
				movementInput.x *= 0.3f;
				movementInput.z *= 0.3f;
			}

			this.forwardSpeed = (float) movementInput.z;
			this.upwardSpeed = (float) movementInput.y;
			this.sidewaysSpeed = (float) movementInput.x;
		} else {
			this.forwardSpeed = 0f;
			this.upwardSpeed = 0f;
			this.sidewaysSpeed = 0f;
		}

		this.flyingSpeed = 0.02f;
		if (this.isSprinting() && lastSprinting) {
			this.flyingSpeed += 0.005999999865889549f;
		}

		super.updateMovement();


		this.lastMovementForward = this.keyboardInput.movementForward;
	}
}
