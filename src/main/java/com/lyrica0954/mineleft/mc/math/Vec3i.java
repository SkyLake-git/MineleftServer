package com.lyrica0954.mineleft.mc.math;

public class Vec3i {

	public int x;

	public int y;

	public int z;

	public Vec3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3d toDouble() {
		return new Vec3d(this.x, this.y, this.z);
	}
}
