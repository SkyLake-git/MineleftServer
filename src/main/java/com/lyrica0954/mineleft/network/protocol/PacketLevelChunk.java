package com.lyrica0954.mineleft.network.protocol;

import com.lyrica0954.mineleft.utils.CodecHelper;
import com.lyrica0954.protocol.PacketBounds;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class PacketLevelChunk extends MineleftPacket {

	public int x;

	public int z;

	public ByteBuf extraPayload;

	public String worldName;

	@Override
	public void encode(ByteBuf out) throws Exception {
		out.writeInt(this.x);
		out.writeInt(this.z);

		out.writeInt(this.extraPayload.array().length);
		out.writeBytes(this.extraPayload);

		CodecHelper.writeUTFSequence(out, this.worldName);
	}

	@Override
	public void decode(ByteBuf in) throws Exception {
		this.x = in.readInt();
		this.z = in.readInt();
		int length = in.readInt();
		this.extraPayload = in.readBytes(length);
		this.worldName = CodecHelper.readUTFSequence(in);
	}

	@Override
	public @NotNull PacketBounds bounds() {
		return PacketBounds.SERVER;
	}

	@Override
	@NotNull
	public ProtocolIds getProtocolId() {
		return ProtocolIds.LEVEL_CHUNK;
	}
}
