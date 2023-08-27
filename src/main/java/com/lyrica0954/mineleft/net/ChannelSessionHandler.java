package com.lyrica0954.mineleft.net;

import com.lyrica0954.mineleft.network.MineleftSession;
import com.lyrica0954.mineleft.network.SessionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.jetbrains.annotations.NotNull;

public class ChannelSessionHandler extends ChannelInboundHandlerAdapter {

	protected SessionManager sessionManager;

	public ChannelSessionHandler(SessionManager sessionManager){
		this.sessionManager = sessionManager;
	}

	@Override
	public void channelActive(@NotNull ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);

		this.sessionManager.add(new MineleftSession(ctx));
	}

	@Override
	public void channelInactive(@NotNull ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);

		this.sessionManager.remove(ctx.channel().id());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);

		MineleftSession session = this.sessionManager.get(ctx.channel().id());

		if (session != null){
			session.handleException(cause);
		}
	}
}
