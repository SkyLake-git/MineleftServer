package com.lyrica0954.mineleft;


import com.lyrica0954.mineleft.network.MineleftSession;
import com.lyrica0954.mineleft.network.SessionManager;
import io.netty.channel.Channel;

import java.util.concurrent.TimeUnit;

public class MineleftServer {

	protected Channel channel;

	protected SessionManager sessionManager;

	public MineleftServer(Channel channel, SessionManager sessionManager) {
		this.channel = channel;
		this.sessionManager = sessionManager;

		channel.eventLoop().scheduleAtFixedRate(this::tick, 0L, 50L, TimeUnit.MILLISECONDS);
	}

	private void tick() {
		for (MineleftSession session : this.sessionManager.getSessions().values()) {
			session.flushSendQueue();
		}
	}
}
