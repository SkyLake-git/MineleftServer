package com.lyrica0954.mineleft.network.player;

import com.lyrica0954.mineleft.mc.level.TemporaryWorld;
import com.lyrica0954.mineleft.mc.level.World;
import com.lyrica0954.mineleft.mc.level.WorldInterface;
import com.lyrica0954.mineleft.mc.math.AxisAlignedBB;
import com.lyrica0954.mineleft.mc.math.Vec3d;
import com.lyrica0954.mineleft.network.MineleftSession;
import com.lyrica0954.mineleft.network.protocol.PacketPlayerViolation;
import com.lyrica0954.mineleft.network.protocol.types.*;
import com.lyrica0954.mineleft.utils.CustomFlags;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineleftPlayerProfile {

	protected MineleftSession session;

	protected PlayerInfo info;

	protected World world;

	protected Vec3d position;

	protected Vec3d lastPosition;

	protected Logger logger;

	protected MinecraftClientPlayer entity;

	protected CustomFlags playerFlags;

	protected float baseMovementSpeed;

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
		this.lastPosition = position;
		this.logger = LoggerFactory.getLogger(this.session.getLogger().getName() + String.format("[MineleftPlayer: %s] ", info.getName()));
		this.playerFlags = new CustomFlags(0);
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


	public float getBaseMovementSpeed() {
		return baseMovementSpeed;
	}

	public void setBaseMovementSpeed(float baseMovementSpeed) {
		this.baseMovementSpeed = baseMovementSpeed;
	}

	public CustomFlags getPlayerFlags() {
		return playerFlags;
	}

	public Logger getLogger() {
		return logger;
	}

	public PlayerInfo getInfo() {
		return info;
	}

	public void onAuthInput(InputData inputData, Vec3d requestedPosition, @Nullable TemporaryWorld temporaryWorld) {
		WorldInterface world;
		if (temporaryWorld != null) {
			world = temporaryWorld;
			this.logger.debug("Using temporary world");
		} else {
			world = this.world;
		}

		if (world == null) {
			this.logger.debug("World not ready, ignoring auth input");
			return;
		}

		this.entity.getRot().yaw = inputData.getRot().yaw;
		this.entity.getRot().pitch = inputData.getRot().pitch;

		this.entity.keyboardInput.sneaking = inputData.hasFlag(InputFlags.SNEAK);
		this.entity.keyboardInput.jumping = inputData.hasFlag(InputFlags.JUMP);
		this.entity.keyboardInput.pressingBack = inputData.hasFlag(InputFlags.DOWN);
		this.entity.keyboardInput.pressingForward = inputData.hasFlag(InputFlags.UP);
		this.entity.keyboardInput.pressingLeft = inputData.hasFlag(InputFlags.LEFT);
		this.entity.keyboardInput.pressingRight = inputData.hasFlag(InputFlags.RIGHT);
		this.entity.keyboardInput.pressingSprint = inputData.hasFlag(InputFlags.SPRINT) || this.playerFlags.hasFlag(PlayerFlags.SPRINTING);

		float baseMovementSpeed = this.getBaseMovementSpeed();

		if (this.playerFlags.hasFlag(PlayerFlags.SPRINTING))
			baseMovementSpeed /= 1.3f;

		this.entity.setWorld(world);
		this.entity.setBaseMovementSpeed(baseMovementSpeed);
		this.entity.updateMovement();

		Vec3d diff = requestedPosition.subtractVector(this.lastPosition);

		this.lastPosition = requestedPosition;

		Vec3d motionDiff = this.entity.getMotion().subtractVector(inputData.getDelta());

		this.logger.debug("paired sprint: " + (this.playerFlags.hasFlag(PlayerFlags.SPRINTING) ? "true" : "false"));
		this.logger.debug("paired sneak: " + (this.playerFlags.hasFlag(PlayerFlags.SNEAKING) ? "true" : "false"));
		this.logger.debug("sprint: " + (this.entity.keyboardInput.pressingSprint ? "true" : "false"));
		this.logger.debug("sneak: " + (this.entity.keyboardInput.sneaking ? "true" : "false"));
		this.logger.debug("base movement speed: " + baseMovementSpeed);
		this.logger.debug("motion: " + this.entity.getMotion().toString());
		this.logger.debug("delta: " + inputData.getDelta().toString());
		this.logger.debug("w: " + (this.entity.keyboardInput.pressingForward ? "true" : "false"));
		this.logger.debug("sideways: " + inputData.getMoveVecX() + " forward: " + inputData.getMoveVecZ());
		this.logger.debug("e: " + this.entity.getPosition().toString() + " p: " + this.position.toString());
		this.logger.debug("y delta: " + (this.entity.getMotion().y - inputData.getDelta().y));
		this.logger.debug("motion delta: " + motionDiff);

		if (motionDiff.length() > 0.2d) {
			PacketPlayerViolation pk = new PacketPlayerViolation();
			pk.message = "Motion";
			pk.playerUuid = this.info.getUuid();
			pk.level = ViolationLevel.PROBABLY;
			this.session.sendPacket(pk);

			this.session.getLogger().debug("violation");
			this.entity.setPosition(requestedPosition.copy());

			this.session.getLogger().debug("Accepted requested position...");

			// ブロックの変化に対応できてないのを検証する
		}
	}
}
