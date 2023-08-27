package com.lyrica0954.mineleft;


import com.lyrica0954.mineleft.network.SessionManager;
import io.netty.channel.Channel;

public class MineleftServer {

	protected Channel channel;

	protected SessionManager sessionManager;

	public MineleftServer(Channel channel, SessionManager sessionManager){
		this.channel = channel;
		this.sessionManager = sessionManager;
	}
}
