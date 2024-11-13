package com.lyrica0954.mineleft.mc.math;

import org.decimal4j.util.DoubleRounder;

public class Vec3f {

	public float x;

	public float y;

	public float z;

	public Vec3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3f() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public static Vec3f zero() {
		return new Vec3f(0, 0, 0);
	}

	public Vec3f round(int n) {
		return new Vec3f((float) DoubleRounder.round(this.x, n), (float) DoubleRounder.round(this.y, n), (float) DoubleRounder.round(this.z, n));
	}

	@Override
	public String toString() {
		return String.format("Vec3d(x=%.10f,y=%.10f,z=%.10f)", this.x, this.y, this.z);
	}

	public Vec3f add(float x, float y, float z) {
		return new Vec3f(this.x + x, this.y + y, this.z + z);
	}

	public Vec3f addVector(Vec3f vec) {
		return this.add(vec.x, vec.y, vec.z);
	}

	public Vec3f subtract(float x, float y, float z) {
		return new Vec3f(this.x - x, this.y - y, this.z - z);
	}

	public Vec3f subtractVector(Vec3f vec) {
		return new Vec3f(this.x - vec.x, this.y - vec.y, this.z - vec.z);
	}

	public Vec3f copy() {
		return new Vec3f(this.x, this.y, this.z);
	}

	public float lengthSquared() {
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}

	public Vec3f normalize(float f) {
		float d = this.lengthSquared();

		if (d < 1.0e-5) {
			return zero();
		}

		float len = (float) (Math.sqrt(d) * f);

		return new Vec3f(this.x / len, this.y / len, this.z / len);
	}

	public Vec3f normalize() {
		return this.normalize(1.0f);
	}

	public double length() {
		return Math.sqrt(this.lengthSquared());
	}

	public Vec3f multiply(float v) {
		return new Vec3f(this.x * v, this.y * v, this.z * v);
	}

	public Vec3f divide(float v) {
		return new Vec3f(this.x / v, this.y / v, this.z / v);
	}

	public Vec3f floor() {
		return new Vec3f((float) Math.floor(this.x), (float) Math.floor(this.y), (float) Math.floor(this.z));
	}
}
