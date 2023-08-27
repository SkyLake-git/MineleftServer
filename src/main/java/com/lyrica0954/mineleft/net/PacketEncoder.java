package com.lyrica0954.mineleft.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<OutboundData> {

	@Override
	protected void encode(ChannelHandlerContext ctx, OutboundData msg, ByteBuf out) throws Exception {
		out.writeShort(msg.getPacketId());
		msg.getPacket().encode(out);
	}
}
