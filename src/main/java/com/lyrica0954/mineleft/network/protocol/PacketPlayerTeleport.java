package com.lyrica0954.mineleft.network.protocol;

import com.lyrica0954.mineleft.mc.math.Vec3f;
import com.lyrica0954.mineleft.utils.CodecHelper;
import com.lyrica0954.protocol.PacketBounds;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PacketPlayerTeleport extends MineleftPacket {

	public UUID playerUuid;

	public String worldName;

	public Vec3f position;

	@Override
	public void encode(ByteBuf out) throws Exception {
		CodecHelper.writeUUID(out, this.playerUuid);
		CodecHelper.writeUTFSequence(out, this.worldName);
		CodecHelper.writeVec3f(out, this.position);
	}

	@Override
	public void decode(ByteBuf in) throws Exception {
		this.playerUuid = CodecHelper.readUUID(in);
		this.worldName = CodecHelper.readUTFSequence(in);
		this.position = CodecHelper.readVec3f(in);
	}

	@Override
	public @NotNull PacketBounds bounds() {
		return PacketBounds.SERVER;
	}

	@Override
	@NotNull
	public ProtocolIds getProtocolId() {
		return ProtocolIds.PLAYER_TELEPORT;
	}
}
