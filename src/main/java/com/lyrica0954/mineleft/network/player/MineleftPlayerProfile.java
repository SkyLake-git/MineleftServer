package com.lyrica0954.mineleft.network.player;

import com.lyrica0954.mineleft.mc.Block;
import com.lyrica0954.mineleft.mc.level.SubChunk;
import com.lyrica0954.mineleft.mc.level.World;
import com.lyrica0954.mineleft.mc.math.Vec3d;
import com.lyrica0954.mineleft.network.MineleftSession;
import com.lyrica0954.mineleft.network.protocol.types.InputData;
import com.lyrica0954.mineleft.network.protocol.types.PlayerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineleftPlayerProfile {

	protected MineleftSession session;

	protected PlayerInfo info;

	protected World world;

	protected Vec3d position;

	protected Logger logger;

	public MineleftPlayerProfile(MineleftSession session, PlayerInfo info, Vec3d position, World currentWorld) {
		this.session = session;
		this.info = info;
		this.position = position;
		this.world = currentWorld;
		this.logger = LoggerFactory.getLogger(this.session.getLogger().getName() + String.format("[MineleftPlayer: %s] ", info.getName()));
	}

	public Vec3d getPosition() {
		return position;
	}

	public void setPosition(Vec3d position) {
		this.position = position;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public PlayerInfo getInfo() {
		return info;
	}

	public void onAuthInput(InputData inputData) {
		this.logger.info("flags: " + inputData.getFlags() + " delta: " + inputData.getDelta().toString());
		this.logger.info(String.format("chunk x: %d, z: %d", (int) this.position.x >> SubChunk.COORD_BIT_SIZE, (int) this.position.z >> SubChunk.COORD_BIT_SIZE));
		Block block = this.world.getBlock(this.position.subtract(0, 1, 0));

		if (block != null) {
			this.logger.info("Block: " + block.getIdentifier());
		}
	}
}
