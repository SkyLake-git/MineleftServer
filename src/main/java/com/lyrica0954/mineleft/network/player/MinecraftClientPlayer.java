package com.lyrica0954.mineleft.network.player;

import com.lyrica0954.mineleft.mc.level.World;
import com.lyrica0954.mineleft.mc.math.AxisAlignedBB;
import com.lyrica0954.mineleft.mc.math.Vec3d;

public class MinecraftClientPlayer extends MinecraftEntityLiving {

	public EntityMovementInput entityInput;

	protected boolean usingItem;

	public MinecraftClientPlayer(World world, Vec3d position, AxisAlignedBB boundingBox) {
		super(world, position, boundingBox);
		this.entityInput = new EntityMovementInput();
		this.usingItem = false;
	}

	public boolean isUsingItem() {
		return usingItem;
	}

	public void setUsingItem(boolean usingItem) {
		this.usingItem = usingItem;
	}

	@Override
	public void updateMovement() {
		this.entityInput.tick(this.entityInput.sneaking);

		if (this.isUsingItem()) {
			this.entityInput.movementSideways *= 0.2f;
			this.entityInput.movementForward *= 0.2f;
		}

		//todo: check submerged, food
		if (this.entityInput.pressingSprint && !this.isUsingItem()) {
			this.sprinting = true;
		}

		this.sneaking = this.entityInput.sneaking;
		this.jumping = this.entityInput.jumping;

		if (this.sprinting) {
			// todo: check submerged
		}

		this.forwardSpeed = this.entityInput.movementForward;
		this.sidewaysSpeed = this.entityInput.movementSideways;

		if (this.touchingWater && this.entityInput.sneaking) {
			this.setMotion(this.getMotion().subtract(0, 0.04d, 0));
		}

		super.updateMovement();

		this.flyingSpeed = 0.02f;
		if (this.sprinting) {
			this.flyingSpeed += 0.005999999865889549f;
		}
	}
}
