package com.lyrica0954.mineleft.network.protocol;

import com.lyrica0954.mineleft.mc.math.Vec3f;
import com.lyrica0954.mineleft.utils.CodecHelper;
import com.lyrica0954.protocol.PacketBounds;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PacketSimulationFrameDebug extends MineleftPacket {

	public UUID playerUuid;

	public int frame;

	public Vec3f position;

	public Vec3f delta;

	public Vec3f clientPosition;

	public Vec3f clientDelta;

	@Override
	public @NotNull ProtocolIds getProtocolId() {
		return ProtocolIds.SIMULATION_FRAME_DEBUG;
	}

	@Override
	public void encode(ByteBuf out) throws Exception {
		CodecHelper.writeUUID(out, playerUuid);
		out.writeInt(frame);
		CodecHelper.writeVec3f(out, this.position);
		CodecHelper.writeVec3f(out, this.delta);
		CodecHelper.writeVec3f(out, this.clientPosition);
		CodecHelper.writeVec3f(out, this.clientDelta);
	}

	@Override
	public void decode(ByteBuf in) throws Exception {
		this.playerUuid = CodecHelper.readUUID(in);
		this.frame = in.readInt();
		this.position = CodecHelper.readVec3f(in);
		this.delta = CodecHelper.readVec3f(in);
		this.clientPosition = CodecHelper.readVec3f(in);
		this.clientDelta = CodecHelper.readVec3f(in);
	}

	@Override
	public @NotNull PacketBounds bounds() {
		return PacketBounds.CLIENT;
	}
}
