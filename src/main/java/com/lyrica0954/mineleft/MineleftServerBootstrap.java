package com.lyrica0954.mineleft;

import com.lyrica0954.mineleft.net.IPacketPool;
import com.lyrica0954.mineleft.net.NettyChannelInitializer;
import com.lyrica0954.mineleft.network.SessionManager;
import com.lyrica0954.mineleft.network.protocol.MineleftPacketPool;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineleftServerBootstrap {

	public MineleftServerBootstrap() {

	}
	public void start(int port) {
		EventLoopGroup mainGroup = new NioEventLoopGroup();
		Logger logger = LoggerFactory.getLogger(this.getClass());

		try {
			logger.info("Booting server...");
			ServerBootstrap bootstrap = new ServerBootstrap();
			IPacketPool packetPool = new MineleftPacketPool();
			SessionManager sessionManager = new SessionManager();

			bootstrap.group(mainGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new NettyChannelInitializer(packetPool, sessionManager))
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture future = bootstrap.bind(port);

			future.sync();

			if (future.isSuccess()){
				logger.info("Socket bound. Starting server");
				MineleftServer server = new MineleftServer(future.channel(), sessionManager);
			}

			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			logger.info("Socket closed, closing event loop");
			mainGroup.shutdownGracefully();
		}
	}
}
