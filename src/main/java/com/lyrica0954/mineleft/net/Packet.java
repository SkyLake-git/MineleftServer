package com.lyrica0954.mineleft.net;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public interface Packet {

	void encode(ByteBuf out) throws Exception;

	void decode(ByteBuf in) throws Exception;

	@NotNull PacketBounds bounds();
}
