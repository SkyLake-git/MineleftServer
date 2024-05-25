package com.lyrica0954.mineleft.network.protocol.types;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public enum Effect {

	JUMP_BOOST(1);

	private static final Map<Integer, Effect> idMap;

	static {
		idMap = new HashMap<>();
		for (Effect effect : values()) {
			idMap.put(effect.getEffectId(), effect);
		}
	}

	private final int effectId;

	Effect(int effectId) {
		this.effectId = effectId;
	}

	public static @Nullable Effect idOf(int effectId) {
		return idMap.get(effectId);
	}

	public int getEffectId() {
		return effectId;
	}
}
