package com.lyrica0954.mineleft.network.player.logic;

import com.lyrica0954.mineleft.mc.math.Vec3f;

public class EntityPhysics {

	public static Vec3f movementInputToVelocity(Vec3f input, float speed, float yaw) {
		float d = input.lengthSquared();

		System.out.println("Input: " + input);
		System.out.println("Speed: " + speed);

		if (d < 1.0e-7) {
			return Vec3f.zero();
		}

		Vec3f move = input.multiply(speed);

		System.out.println("Move: " + move);

		float f = (float) Math.sin(yaw * ((float) Math.PI / 180f));
		float g = (float) Math.cos(yaw * ((float) Math.PI / 180f));

		return new Vec3f(
				move.x * g - move.z * f,
				move.y,
				move.z * g + move.x * f
		);
	}
}
