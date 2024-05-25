package com.lyrica0954.mineleft.network.handler;

import com.lyrica0954.mineleft.network.protocol.*;

public interface IPacketHandler {

	void handleLevelChunk(PacketLevelChunk packet) throws PacketHandlingException;

	void handlePlayerLogin(PacketPlayerLogin packet) throws PacketHandlingException;

	void handlePlayerTeleport(PacketPlayerTeleport packet) throws PacketHandlingException;

	void handleConfiguration(PacketConfiguration packet) throws PacketHandlingException;

	void handlePlayerAuthInput(PacketPlayerAuthInput packet) throws PacketHandlingException;

	void handleBlockMappings(PacketBlockMappings packet) throws PacketHandlingException;

	void handleSetPlayerFlags(PacketSetPlayerFlags packet) throws PacketHandlingException;

	void handleSetPlayerAttribute(PacketSetPlayerAttribute packet) throws PacketHandlingException;

	void handleSetPlayerMotion(PacketSetPlayerMotion packet) throws PacketHandlingException;

	void handlePlayerEffect(PacketPlayerEffect packet) throws PacketHandlingException;
}
