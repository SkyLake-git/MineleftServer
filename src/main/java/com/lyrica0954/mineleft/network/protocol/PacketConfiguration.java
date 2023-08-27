package com.lyrica0954.mineleft.network.protocol;

import com.lyrica0954.mineleft.net.PacketBounds;
import com.lyrica0954.mineleft.utils.ByteBufHelper;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class PacketConfiguration extends MineleftPacket {

	public String defaultWorldName;

	@Override
	public void encode(ByteBuf out) throws Exception {
		ByteBufHelper.writeStandardCharSequence(out, this.defaultWorldName);
	}

	@Override
	public void decode(ByteBuf in) throws Exception {
		this.defaultWorldName = ByteBufHelper.readStandardCharSequence(in);
	}

	@Override
	public @NotNull PacketBounds bounds() {
		return PacketBounds.SERVER;
	}

	@Override
	@NotNull ProtocolIds getProtocolId() {
		return ProtocolIds.CONFIGURATION;
	}
}
