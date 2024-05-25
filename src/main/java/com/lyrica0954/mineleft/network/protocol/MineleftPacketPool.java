package com.lyrica0954.mineleft.network.protocol;

import com.lyrica0954.protocol.IPacketPool;
import com.lyrica0954.protocol.Packet;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class MineleftPacketPool implements IPacketPool {

	protected HashMap<Short, Constructor<? extends Packet>> pool;

	public MineleftPacketPool() {
		this.pool = new HashMap<>();

		this.register(ProtocolIds.PLAYER_LOGIN.id, new PacketPlayerLogin());
		this.register(ProtocolIds.LEVEL_CHUNK.id, new PacketLevelChunk());
		this.register(ProtocolIds.PLAYER_TELEPORT.id, new PacketPlayerTeleport());
		this.register(ProtocolIds.CONFIGURATION.id, new PacketConfiguration());
		this.register(ProtocolIds.PLAYER_AUTH_INPUT.id, new PacketPlayerAuthInput());
		this.register(ProtocolIds.BLOCK_MAPPINGS.id, new PacketBlockMappings());
		this.register(ProtocolIds.SET_PLAYER_FLAGS.id, new PacketSetPlayerFlags());
		this.register(ProtocolIds.SET_PLAYER_ATTRIBUTE.id, new PacketSetPlayerAttribute());
		this.register(ProtocolIds.SET_PLAYER_MOTION.id, new PacketSetPlayerMotion());
		this.register(ProtocolIds.PLAYER_EFFECT.id, new PacketPlayerEffect());
	}

	public void register(short protocolId, Packet packet) {
		try {
			this.pool.put(protocolId, packet.getClass().getConstructor());
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public @Nullable Packet get(short id) {
		Constructor<? extends Packet> constructor = this.pool.get(id);

		if (constructor == null) {
			return null;
		}

		try {
			return constructor.newInstance();
		} catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
