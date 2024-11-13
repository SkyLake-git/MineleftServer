package com.lyrica0954.mineleft.mc.math;

import java.util.EnumMap;
import java.util.Map;

public enum Facing {

	DOWN,
	UP,
	NORTH,
	SOUTH,
	WEST,
	EAST;

	public static final Facing[] HORIZONTAL = new Facing[]{NORTH, SOUTH, WEST, EAST};

	public static final Facing[] ALL = new Facing[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};

	public static final Map<Facing, Vec3i> OFFSET = new EnumMap<>(Facing.class);

	static {
		OFFSET.put(DOWN, new Vec3i(0, -1, 0));
		OFFSET.put(UP, new Vec3i(0, 1, 0));
		OFFSET.put(NORTH, new Vec3i(0, 0, -1));
		OFFSET.put(SOUTH, new Vec3i(0, 0, 1));
		OFFSET.put(WEST, new Vec3i(-1, 0, 0));
		OFFSET.put(EAST, new Vec3i(1, 0, 0));
	}

	Facing() {

	}

	public Vec3i getOffset() {
		return OFFSET.get(this);
	}
}
