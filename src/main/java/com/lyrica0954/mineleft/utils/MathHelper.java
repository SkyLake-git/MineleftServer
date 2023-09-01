package com.lyrica0954.mineleft.utils;

public class MathHelper {

	public static float sqrtFloat(float v) {
		return (float) Math.sqrt(v);
	}

	public static int floor(double v) {
		return (int) Math.floor(v);
	}

	public static double clamp(double num, double min, double max) {
		return num < min ? min : (Math.min(num, max));
	}
}
