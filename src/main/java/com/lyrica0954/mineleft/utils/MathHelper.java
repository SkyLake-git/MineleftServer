package com.lyrica0954.mineleft.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

	public static boolean equals(double a, double b, double epsilon) {
		return Math.abs(a - b) < epsilon;
	}

	public static double round(double a, int n) {
		return BigDecimal.valueOf(a).setScale(n, RoundingMode.HALF_UP).doubleValue();
	}

	public static boolean equals(double a, double b) {
		return equals(a, b, 0.00001d);
	}
}
