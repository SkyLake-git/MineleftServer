package com.lyrica0954.mineleft.network.protocol;

import com.lyrica0954.mineleft.mc.math.Vec3d;
import com.lyrica0954.mineleft.net.PacketBounds;
import com.lyrica0954.mineleft.utils.ByteBufHelper;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PacketPlayerTeleport extends MineleftPacket {

	public UUID playerUuid;

	public String worldName;

	public Vec3d position;

	@Override
	public void encode(ByteBuf out) throws Exception {
		ByteBufHelper.writeStandardCharSequence(out, this.playerUuid.toString());
		ByteBufHelper.writeStandardCharSequence(out, this.worldName);
		ByteBufHelper.writeVec3d(out, this.position);
	}

	@Override
	public void decode(ByteBuf in) throws Exception {
		this.playerUuid = UUID.fromString(ByteBufHelper.readStandardCharSequence(in));
		this.worldName = ByteBufHelper.readStandardCharSequence(in);
		this.position = ByteBufHelper.readVec3d(in);
	}

	@Override
	public @NotNull PacketBounds bounds() {
		return PacketBounds.SERVER;
	}

	@Override
	@NotNull ProtocolIds getProtocolId() {
		return ProtocolIds.PLAYER_TELEPORT;
	}
}
