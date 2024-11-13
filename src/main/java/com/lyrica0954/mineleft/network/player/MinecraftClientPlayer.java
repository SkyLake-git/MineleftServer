package com.lyrica0954.mineleft.network.player;

import com.lyrica0954.mineleft.mc.level.WorldInterface;
import com.lyrica0954.mineleft.mc.math.AxisAlignedBB;
import com.lyrica0954.mineleft.mc.math.Vec3f;

public class MinecraftClientPlayer extends MinecraftEntityLiving {

	public EntityMovementInput keyboardInput;

	protected boolean usingItem;

	protected int continueSprintTicks;

	protected int sneakTicks;

	protected float baseMovementSpeed;

	protected float lastMovementForward;

	public MinecraftClientPlayer(WorldInterface world, Vec3f position, float sizeWidth, float sizeHeight) {
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

	public void updateMovement(AdvanceInputType.Vector vectorInput) {
		// todo: normalize and validation
		Vec3f raw = new Vec3f(vectorInput.getVecX(), 0, vectorInput.getVecZ());

		this.keyboardInput.tick(false);
		this.keyboardInput.movementSideways = raw.x;
		this.keyboardInput.movementForward = raw.z;
		this.updateMovement();
	}

	public void updateMovement(AdvanceInputType.Key keyInput) {
		this.keyboardInput.sneaking = keyInput.sneaking;
		this.keyboardInput.tick(keyInput.sneaking);
		this.updateMovement();
	}

	@Override
	public void updateMovement() {
		boolean lastSprinting = this.isSprinting();
		this.sneaking = this.keyboardInput.sneaking;
		this.jumping = this.keyboardInput.jumping;

		if (this.isSneaking()) {
			++this.sneakTicks;
		} else {
			this.sneakTicks = 0;
		}

		if (this.isUsingItem()) {
			this.keyboardInput.movementSideways *= 0.2f;
			this.keyboardInput.movementForward *= 0.2f;
		}

		//todo: check submerged, food
		if (!this.isSprinting() && this.keyboardInput.pressingSprint && !this.isUsingItem()) {
			this.setSprinting(true);
		}

		if (this.isSprinting() && !this.keyboardInput.pressingSprint) {
			this.setSprinting(false);
		}

		if (this.touchingWater && this.keyboardInput.sneaking) {
			this.setMotion(this.getMotion().subtract(0, 0.04f, 0));
		}

		if (this.isSprinting() && (!this.keyboardInput.pressingForward)) {
			this.setSprinting(false);
		}


		if (Math.abs(this.keyboardInput.movementForward) > 1e-4 || Math.abs(this.keyboardInput.movementSideways) > 1e-4) {
			Vec3f movementInput = new Vec3f(this.keyboardInput.movementSideways, 0, this.keyboardInput.movementForward);

			movementInput = (movementInput.length() > 1f) ? movementInput.normalize() : movementInput;

			this.forwardSpeed = movementInput.z;
			this.upwardSpeed = movementInput.y;
			this.sidewaysSpeed = movementInput.x;
			this.actuallyForwardSpeed = this.keyboardInput.movementForward;
			this.actuallySidewaysSpeed = this.keyboardInput.movementSideways;
		} else {
			this.forwardSpeed = 0f;
			this.upwardSpeed = 0f;
			this.sidewaysSpeed = 0f;
			this.actuallySidewaysSpeed = 0f;
			this.actuallyForwardSpeed = 0f;
		}

		this.flyingSpeed = 0.02f;
		if (this.isSprinting() && lastSprinting) {
			this.flyingSpeed += 0.005999999865889549f;
		}

		this.movementSpeed = this.baseMovementSpeed;
		if (this.isSprinting()) {
			this.movementSpeed *= 1.3f;
		}

		super.updateMovement();

		this.lastMovementForward = this.keyboardInput.movementForward;
	}

	@Override
	public Vec3f adjustMovementForSneaking(Vec3f movement, Vec3f movementInput) {
		movement = super.adjustMovementForSneaking(movement, movementInput);
		movementInput.y = 0f;
		Vec3f normalized = movementInput.normalize();
		System.out.println("bef: " + normalized);
		if (normalized.x > 0.01f) {
			normalized.x = 1.0f;
		}
		if (normalized.x < -0.01f) {
			normalized.x = -1.0f;
		}
		if (normalized.z > 0.01f) {
			normalized.z = 1.0f;
		}
		if (normalized.z < -0.01f) {
			normalized.z = -1.0f;
		}
		normalized = normalized.multiply(0.04f); // todo:
		System.out.println(normalized);
		float nx = normalized.x;
		float nz = normalized.z;
		float x = movement.x;
		float z = movement.z;

		AxisAlignedBB bb = this.getBoundingBox().copy();

		if (this.getWorld().getCollisionBoxes(bb.offsetCopy(nx, -1, 0.0f), true).isEmpty()) {
			nx = 0.0f;
			x = 0.0f;
		}

		if (this.getWorld().getCollisionBoxes(bb.offsetCopy(0.0f, -1, nz), true).isEmpty()) {
			nz = 0.0f;
			z = 0.0f;
		}

		if (this.getWorld().getCollisionBoxes(bb.offsetCopy(nx, -1, nz), true).isEmpty()) {
			nx = 0.0f;
			nz = 0.0f;
			x = 0.0f;
			z = 0.0f;
		}

		return new Vec3f(x, movement.y, z);
	}
}
