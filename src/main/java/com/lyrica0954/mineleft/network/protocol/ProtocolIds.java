package com.lyrica0954.mineleft.network.protocol;

public enum ProtocolIds {
	PLAYER_LOGIN(0),
	LEVEL_CHUNK(1),
	PLAYER_TELEPORT(2),
	CONFIGURATION(3),
	PLAYER_AUTH_INPUT(4),
	BLOCK_MAPPINGS(5),
	SET_PLAYER_FLAGS(6),
	SET_PLAYER_ATTRIBUTE(7),
	PLAYER_VIOLATION(8);


	public final short id;

	ProtocolIds(int id) {
		this.id = (short) id;
	}
}
