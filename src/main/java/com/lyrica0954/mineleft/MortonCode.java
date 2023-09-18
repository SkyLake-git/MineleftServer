package com.lyrica0954.mineleft;

import com.erenck.mortonlib.Morton2D;
import com.erenck.mortonlib.Morton3D;

public class MortonCode {

	private static final Morton3D morton3D;

	private static final Morton2D morton2D;

	static {
		morton3D = new Morton3D();
		morton2D = new Morton2D();
	}

	public static Morton3D get3D() {
		return morton3D;
	}

	public static Morton2D get2D() {
		return morton2D;
	}
}
