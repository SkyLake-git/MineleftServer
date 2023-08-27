package com.lyrica0954.mineleft.network.protocol.types;

import java.util.UUID;

public class PlayerInfo {

	protected String name;

	protected UUID uuid;

	public PlayerInfo(String name, UUID uuid) {
		this.name = name;
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public UUID getUuid() {
		return uuid;
	}
}
