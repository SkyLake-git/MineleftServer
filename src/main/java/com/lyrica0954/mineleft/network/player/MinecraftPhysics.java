package com.lyrica0954.mineleft.network.player;

import com.lyrica0954.mineleft.mc.math.Vec3d;

public class MinecraftPhysics {

	public static Vec3d movementInputToVelocity(Vec3d input, float speed, float yaw) {
		double d = input.lengthSquared();

		if (d < 1.0e-7) {
			return Vec3d.zero();
		}

		Vec3d move = (d > 1.0 ? input.normalize() : input).multiply(speed);

		double f = Math.sin(yaw * ((float) Math.PI / 180));
		double g = Math.cos(yaw * ((float) Math.PI / 180));

		return new Vec3d(
				move.x * g - move.z * f,
				move.y,
				move.z * g + move.x * f
		);
	}
}
