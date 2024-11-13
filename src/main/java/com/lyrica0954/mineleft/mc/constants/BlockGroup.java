package com.lyrica0954.mineleft.mc.constants;

import java.util.HashMap;
import java.util.Map;

public enum BlockGroup {

	NONE,
	WATER,
	LAVA;

	private static final Map<String, BlockGroup> map = new HashMap<>();

	static {
		registerMinecraftGroup("water", WATER);
		registerMinecraftGroup("flowing_water", WATER);
		registerMinecraftGroup("lava", LAVA);
		registerMinecraftGroup("flowing_lava", LAVA);
	}

	private static void registerMinecraftGroup(String name, BlockGroup blockGroup) {
		map.put("minecraft:" + name, blockGroup);
	}

	public static BlockGroup get(String name) {
		return map.getOrDefault(name, NONE);
	}
}
