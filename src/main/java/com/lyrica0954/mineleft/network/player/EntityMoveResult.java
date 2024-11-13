package com.lyrica0954.mineleft.network.player;

import com.lyrica0954.mineleft.mc.math.Vec3f;

public class EntityMoveResult {

	public Vec3f motion;

	public boolean isXColliding;

	public boolean isYColliding;

	public boolean isZColliding;

	public EntityMoveResult(Vec3f motion, boolean isXColliding, boolean isYColliding, boolean isZColliding) {
		this.motion = motion;
		this.isXColliding = isXColliding;
		this.isYColliding = isYColliding;
		this.isZColliding = isZColliding;
	}
}
