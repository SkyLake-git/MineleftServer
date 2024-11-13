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

	public Vec3i add(int x, int y, int z) {
		return new Vec3i(this.x + x, this.y + y, this.z + z);
	}

	public Vec3i addVector(Vec3i vec) {
		return this.add(vec.x, vec.y, vec.z);
	}

	public Vec3i subtract(int x, int y, int z) {
		return new Vec3i(this.x - x, this.y - y, this.z - z);
	}

	public Vec3i subtractVector(Vec3i vec) {
		return new Vec3i(this.x - vec.x, this.y - vec.y, this.z - vec.z);
	}

	public Vec3i copy() {
		return new Vec3i(this.x, this.y, this.z);
	}


	@Override
	public String toString() {
		return String.format("Vec3i(x=%d,y=%d,z=%d)", this.x, this.y, this.z);
	}

	public Vec3f toFloat() {
		return new Vec3f(this.x, this.y, this.z);
	}
}
