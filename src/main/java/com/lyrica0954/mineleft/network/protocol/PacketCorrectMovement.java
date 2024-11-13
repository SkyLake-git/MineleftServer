package com.lyrica0954.mineleft.network.protocol;

import com.lyrica0954.mineleft.mc.math.Vec3f;
import com.lyrica0954.mineleft.utils.CodecHelper;
import com.lyrica0954.protocol.PacketBounds;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PacketCorrectMovement extends MineleftPacket {

	public UUID playerUuid;

	public Vec3f position;

	public Vec3f delta;

	public boolean onGround;

	public int frame;

	@Override
	public @NotNull ProtocolIds getProtocolId() {
		return ProtocolIds.CORRECT_MOVEMENT;
	}

	@Override
	public void encode(ByteBuf out) throws Exception {
		CodecHelper.writeUUID(out, playerUuid);
		CodecHelper.writeVec3f(out, position);
		CodecHelper.writeVec3f(out, delta);
		out.writeBoolean(onGround);
		out.writeInt(frame);
	}

	@Override
	public void decode(ByteBuf in) throws Exception {
		this.playerUuid = CodecHelper.readUUID(in);
		this.position = CodecHelper.readVec3f(in);
		this.delta = CodecHelper.readVec3f(in);
		this.onGround = in.readBoolean();
		this.frame = in.readInt();
	}

	@Override
	public @NotNull PacketBounds bounds() {
		return PacketBounds.CLIENT;
	}
}
