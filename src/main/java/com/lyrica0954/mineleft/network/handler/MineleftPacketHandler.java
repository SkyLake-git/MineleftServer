package com.lyrica0954.mineleft.network.handler;

import com.lyrica0954.mineleft.MortonCode;
import com.lyrica0954.mineleft.mc.Block;
import com.lyrica0954.mineleft.mc.BlockMappings;
import com.lyrica0954.mineleft.mc.level.*;
import com.lyrica0954.mineleft.network.MineleftSession;
import com.lyrica0954.mineleft.network.NetworkConverters;
import com.lyrica0954.mineleft.network.chunk.ChunkDeserializer;
import com.lyrica0954.mineleft.network.player.MineleftPlayerProfile;
import com.lyrica0954.mineleft.network.protocol.*;
import com.lyrica0954.mineleft.network.protocol.types.ChunkSendingMethod;

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

		if (world == null && this.session.getChunkSendingMethod() != ChunkSendingMethod.ALTERNATE) {
			throw new PacketHandlingException("Unknown world");
		}

		MineleftPlayerProfile player = this.session.getPlayers().get(packet.playerUuid);

		if (player == null) {
			return;
		}

		if (world != null) {
			player.setWorld(world);
		}

		player.setPosition(packet.position.copy());
	}

	@Override
	public void handleConfiguration(PacketConfiguration packet) throws PacketHandlingException {
		this.session.getWorldManager().setDefaultWorldName(packet.defaultWorldName);

		this.session.getLogger().info("Configured.");
		this.session.getLogger().info("Default world name: " + packet.defaultWorldName);
		this.session.getLogger().info("Chunk sending method: " + packet.chunkSendingMethod);

		this.session.setChunkSendingMethod(packet.chunkSendingMethod);
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

		TemporaryWorld temporaryWorld = null;
		if (!packet.nearbyBlocks.isEmpty()) {
			BlockPalette.Builder builder = BlockPalette.builder();
			for (Map.Entry<Long, PaletteBlock> data : packet.nearbyBlocks.entrySet()) {
				int[] v = MortonCode.get3D().decode(data.getKey());
				builder.set(v[0], v[1], v[2], data.getValue());
			}

			temporaryWorld = new TemporaryWorld(builder);
		}

		player.onAuthInput(packet.inputData, packet.requestedPosition.copy().subtract(0, 1.62, 0).round(4), temporaryWorld);
	}

	@Override
	public void handleBlockMappings(PacketBlockMappings packet) throws PacketHandlingException {
		if (NetworkConverters.getInstance().isBlockMappingsInitialized()) {
			throw new PacketHandlingException("Block mappings already initialized");
		}

		BlockMappings.Builder builder = BlockMappings.builder();

		for (Map.Entry<Integer, Block> entry : packet.mapping.entrySet()) {
			builder.register(entry.getValue());

			this.session.getLogger().info("Registered block mapping " + entry.getKey() + " -> " + entry.getValue().toString());
		}

		builder.setNullBlock(packet.nullBlockNetworkId);
		this.session.getLogger().info("Null block identifier: " + packet.nullBlockNetworkId);

		NetworkConverters.getInstance().initializeBlockMappings(builder.build());
	}

	@Override
	public void handleSetPlayerFlags(PacketSetPlayerFlags packet) throws PacketHandlingException {
		MineleftPlayerProfile player = this.session.getPlayers().get(packet.playerUuid);

		if (player == null) {
			return;
		}

		// fixme: ugly method name
		player.getPlayerFlags().setFlags(packet.flags);

		player.getLogger().info("Updated player flags: " + packet.flags);
	}

	@Override
	public void handleSetPlayerAttribute(PacketSetPlayerAttribute packet) throws PacketHandlingException {
		MineleftPlayerProfile player = this.session.getPlayers().get(packet.playerUuid);

		if (player == null) {
			return;
		}

		player.setBaseMovementSpeed(packet.movementSpeed);

		player.getLogger().info("Updated movement speed: " + packet.movementSpeed);
	}
}
