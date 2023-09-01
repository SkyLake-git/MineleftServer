package com.lyrica0954.mineleft.network.handler;

import com.lyrica0954.mineleft.mc.Block;
import com.lyrica0954.mineleft.mc.level.Chunk;
import com.lyrica0954.mineleft.mc.level.World;
import com.lyrica0954.mineleft.network.MineleftSession;
import com.lyrica0954.mineleft.network.NetworkConverters;
import com.lyrica0954.mineleft.network.chunk.ChunkDeserializer;
import com.lyrica0954.mineleft.network.player.MineleftPlayerProfile;
import com.lyrica0954.mineleft.network.protocol.*;

import java.util.Map;

public class MineleftPacketHandler implements IPacketHandler {

	protected MineleftSession session;

	public MineleftPacketHandler(MineleftSession session) {
		this.session = session;
	}

	@Override
	public void handlePlayerLogin(PacketPlayerLogin packet) throws PacketHandlingException {
		World world = this.session.getWorldManager().getWorld(packet.worldName);
		if (world == null) {
			world = this.session.getWorldManager().getDefaultWorld();
		}

		MineleftPlayerProfile profile = new MineleftPlayerProfile(this.session, packet.playerInfo, packet.position, world);

		this.session.addPlayer(profile);

		this.session.getLogger().info("Added player: " + packet.playerInfo.getName() + " uuid: " + packet.playerInfo.getUuid().toString());
	}

	@Override
	public void handlePlayerTeleport(PacketPlayerTeleport packet) throws PacketHandlingException {
		World world = this.session.getWorldManager().getWorld(packet.worldName);

		if (world == null) {
			throw new PacketHandlingException("Unknown world");
		}

		MineleftPlayerProfile player = this.session.getPlayers().get(packet.playerUuid);

		if (player == null) {
			return;
		}

		player.setWorld(world);
		player.setPosition(packet.position.copy());
	}

	@Override
	public void handleConfiguration(PacketConfiguration packet) throws PacketHandlingException {
		this.session.getWorldManager().setDefaultWorldName(packet.defaultWorldName);

		this.session.getLogger().info("Configured.");
		this.session.getLogger().info("Default world name: " + packet.defaultWorldName);
	}

	@Override
	public void handleLevelChunk(PacketLevelChunk packet) throws PacketHandlingException {
		Chunk chunk;
		try {
			chunk = ChunkDeserializer.deserialize(packet.extraPayload);
		} catch (Exception e) {
			throw PacketHandlingException.wrap(e);
		}

		//this.session.getLogger().info(String.format("Deserialized chunk! x: %d, z: %d", packet.x, packet.z));


		World world;
		if (!this.session.getWorldManager().existsWorld(packet.worldName)) {
			world = new World();

			this.session.getWorldManager().addWorld(packet.worldName, world);
		} else {
			world = this.session.getWorldManager().getWorld(packet.worldName);
			assert world != null;
		}

		world.setChunk(packet.x, packet.z, chunk);
	}

	@Override
	public void handlePlayerAuthInput(PacketPlayerAuthInput packet) throws PacketHandlingException {
		MineleftPlayerProfile player = this.session.getPlayers().get(packet.playerUuid);

		if (player == null) {
			return;
		}

		player.onAuthInput(packet.inputData);
	}

	@Override
	public void handleBlockMappings(PacketBlockMappings packet) throws PacketHandlingException {
		for (Map.Entry<Integer, Block> entry : packet.mapping.entrySet()) {
			NetworkConverters.getInstance().getBlockMappings().register(entry.getValue());

			this.session.getLogger().info("Registered block mapping " + entry.getKey() + " -> " + entry.getValue().getIdentifier());
		}
	}
}
