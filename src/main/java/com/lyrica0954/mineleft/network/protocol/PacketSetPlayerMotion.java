package com.lyrica0954.mineleft.network.protocol;

import com.lyrica0954.mineleft.mc.math.Vec3f;
import com.lyrica0954.mineleft.utils.CodecHelper;
import com.lyrica0954.protocol.PacketBounds;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PacketSetPlayerMotion extends MineleftPacket {

	public UUID playerUuid;

	public Vec3f motion;

	@Override
	public @NotNull ProtocolIds getProtocolId() {
		return ProtocolIds.SET_PLAYER_MOTION;
	}

	@Override
	public void encode(ByteBuf out) throws Exception {
		CodecHelper.writeUUID(out, this.playerUuid);
		CodecHelper.writeVec3f(out, this.motion);
	}

	@Override
	public void decode(ByteBuf in) throws Exception {
		this.playerUuid = CodecHelper.readUUID(in);
		this.motion = CodecHelper.readVec3f(in);
	}

	@Override
	public @NotNull PacketBounds bounds() {
		return PacketBounds.SERVER;
	}
}
