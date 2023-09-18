package com.lyrica0954.mineleft.network.protocol;

import com.lyrica0954.mineleft.utils.CodecHelper;
import com.lyrica0954.protocol.PacketBounds;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class PacketConfiguration extends MineleftPacket {

	public String defaultWorldName;

	public int chunkSendingMethod;

	@Override
	public void encode(ByteBuf out) throws Exception {
		CodecHelper.writeUTFSequence(out, this.defaultWorldName);
		out.writeInt(this.chunkSendingMethod);
	}

	@Override
	public void decode(ByteBuf in) throws Exception {
		this.defaultWorldName = CodecHelper.readUTFSequence(in);
		this.chunkSendingMethod = in.readInt();
	}

	@Override
	public @NotNull PacketBounds bounds() {
		return PacketBounds.SERVER;
	}

	@Override
	@NotNull
	public ProtocolIds getProtocolId() {
		return ProtocolIds.CONFIGURATION;
	}
}
