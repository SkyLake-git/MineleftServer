package com.lyrica0954.mineleft.net;

import com.lyrica0954.mineleft.network.SessionManager;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

	protected IPacketPool packetPool;

	protected SessionManager sessionManager;

	public NettyChannelInitializer(IPacketPool packetPool, SessionManager sessionManager){
		this.packetPool = packetPool;
		this.sessionManager = sessionManager;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new ChannelSessionHandler(this.sessionManager), new PacketDecoder(this.packetPool), new PacketEncoder(), new PacketHandler(this.sessionManager));
	}
}
