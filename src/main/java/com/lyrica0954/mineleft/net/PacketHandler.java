package com.lyrica0954.mineleft.net;

import com.lyrica0954.mineleft.network.MineleftSession;
import com.lyrica0954.mineleft.network.SessionManager;
import com.lyrica0954.mineleft.network.protocol.MineleftPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PacketHandler extends SimpleChannelInboundHandler<InboundData> {

	protected SessionManager sessionManager;

	public PacketHandler(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, InboundData msg) throws Exception {
		MineleftSession session = this.sessionManager.get(ctx.channel().id());

		if (session != null) {
			Packet packet = msg.getPacket();

			if (!(packet instanceof MineleftPacket)) {
				session.getLogger().info("Received un-mineleft packet. ignoring");
				return;
			}

			session.handlePacket((MineleftPacket) packet);
		}
	}
}
