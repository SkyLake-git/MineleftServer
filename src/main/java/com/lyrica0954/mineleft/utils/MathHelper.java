package com.lyrica0954.mineleft.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathHelper {

	public static float sqrtFloat(float v) {
		return (float) Math.sqrt(v);
	}

	public static int floor(float v) {
		return (int) Math.floor(v);
	}

	public static int ceil(float v) {
		return (int) Math.ceil(v);
	}


	public static float clamp(float num, float min, float max) {
		return num < min ? min : (Math.min(num, max));
	}

	public static boolean equals(float a, float b, float epsilon) {
		return Math.abs(a - b) < epsilon;
	}

	public static float round(float a, int n) {
		return BigDecimal.valueOf(a).setScale(n, RoundingMode.HALF_UP).floatValue();
	}

	public static boolean equals(float a, float b) {
		return equals(a, b, 1e-7f);
	}
}
