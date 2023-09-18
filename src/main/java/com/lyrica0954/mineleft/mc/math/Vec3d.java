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

	public static Vec3d zero() {
		return new Vec3d(0, 0, 0);
	}

	@Override
	public String toString() {
		return String.format("Vec3d(x=%f,y=%f,z=%f)", this.x, this.y, this.z);
	}

	public Vec3d add(double x, double y, double z) {
		return new Vec3d(this.x + x, this.y + y, this.z + z);
	}

	public Vec3d addVector(Vec3d vec) {
		return this.add(vec.x, vec.y, vec.z);
	}

	public Vec3d subtract(double x, double y, double z) {
		return new Vec3d(this.x - x, this.y - y, this.z - z);
	}

	public Vec3d subtractVector(Vec3d vec) {
		return new Vec3d(this.x - vec.x, this.y - vec.y, this.z - vec.z);
	}

	public Vec3d copy() {
		return new Vec3d(this.x, this.y, this.z);
	}

	public double lengthSquared() {
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}

	public Vec3d normalize(double f) {
		double d = this.lengthSquared();

		if (d < 1.0e-5) {
			return zero();
		}

		double len = Math.sqrt(d) * f;

		return new Vec3d(this.x / len, this.y / len, this.z / len);
	}

	public Vec3d normalize() {
		return this.normalize(1.0d);
	}

	public double length() {
		return Math.sqrt(this.lengthSquared());
	}

	public Vec3d multiply(double v) {
		return new Vec3d(this.x * v, this.y * v, this.z * v);
	}
}
