package com.lyrica0954.mineleft.network.player;

import com.lyrica0954.mineleft.mc.level.World;
import com.lyrica0954.mineleft.mc.math.AxisAlignedBB;
import com.lyrica0954.mineleft.mc.math.Vec3d;
import com.lyrica0954.mineleft.network.MineleftSession;
import com.lyrica0954.mineleft.network.protocol.types.InputData;
import com.lyrica0954.mineleft.network.protocol.types.InputFlags;
import com.lyrica0954.mineleft.network.protocol.types.PlayerInfo;
import com.lyrica0954.mineleft.utils.MathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineleftPlayerProfile {

	protected MineleftSession session;

	protected PlayerInfo info;

	protected World world;

	protected Vec3d position;

	protected Logger logger;

	protected MinecraftClientPlayer entity;

	public MineleftPlayerProfile(MineleftSession session, PlayerInfo info, Vec3d position, World currentWorld) {
		this.session = session;
		this.info = info;
		this.position = position;
		this.world = currentWorld;
		float w = 1.8f;
		this.entity = new MinecraftClientPlayer(world, position, new AxisAlignedBB(
				this.position.x + -w / 2,
				this.position.y + 0,
				this.position.z + -w / 2,
				this.position.x + w / 2,
				this.position.y + 0.6,
				this.position.z + w / 2
		));
		this.logger = LoggerFactory.getLogger(this.session.getLogger().getName() + String.format("[MineleftPlayer: %s] ", info.getName()));
	}

	public MinecraftEntityLiving getEntity() {
		return entity;
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
		if (!this.world.isChunkLoaded(MathHelper.floor(this.position.x), MathHelper.floor(this.position.z))) {
			return;
		}

		this.entity.getRot().yaw = inputData.getRot().yaw;
		this.entity.getRot().pitch = inputData.getRot().pitch;

		this.entity.entityInput.sneaking = inputData.hasFlag(InputFlags.SNEAK);
		this.entity.entityInput.jumping = inputData.hasFlag(InputFlags.JUMP);
		this.entity.entityInput.pressingBack = inputData.hasFlag(InputFlags.DOWN);
		this.entity.entityInput.pressingForward = inputData.hasFlag(InputFlags.UP);
		this.entity.entityInput.pressingLeft = inputData.hasFlag(InputFlags.LEFT);
		this.entity.entityInput.pressingRight = inputData.hasFlag(InputFlags.RIGHT);
		this.entity.entityInput.pressingSprint = inputData.hasFlag(InputFlags.SPRINT);
		//fixme: support tap
		this.entity.updateMovement();
		
		// fixme: なんか subchunk y が -5 以下になる

		this.logger.info("motion: " + this.entity.getMotion().toString());
		this.logger.info("e: " + this.entity.getPosition().toString() + " p: " + this.position.toString());
	}
}
