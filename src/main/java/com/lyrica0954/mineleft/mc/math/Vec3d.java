package com.lyrica0954.mineleft.mc.math;

public class Vec3d {

	public double x;

	public double y;

	public double z;

	public Vec3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3d() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	@Override
	public String toString() {
		return String.format("Vec3d(x=%f,y=%f,z=%f)", this.x, this.y, this.z);
	}

	public Vec3d add(double x, double y, double z) {
		return new Vec3d(this.x + x, this.y + y, this.z + z);
	}

	public Vec3d subtract(double x, double y, double z) {
		return new Vec3d(this.x - x, this.y - y, this.z - z);
	}
}
