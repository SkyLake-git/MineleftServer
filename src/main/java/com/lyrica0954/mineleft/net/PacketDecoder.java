package com.lyrica0954.mineleft.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class PacketDecoder extends ReplayingDecoder<InboundData> {

	protected IPacketPool packetPool;

	public PacketDecoder(IPacketPool packetPool) {
		this.packetPool = packetPool;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		InboundData structure = new InboundData();
		structure.setPacketId(in.readShort());

		Packet packet = this.packetPool.get(structure.getPacketId());

		if (packet == null) {
			throw new UnknownPacketException("Unknown packet id " + structure.getPacketId());
		}

		structure.setPacket(packet);

		packet.decode(in);

		out.add(structure);
	}
}
