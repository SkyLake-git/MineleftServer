package com.lyrica0954.mineleft.network;

import com.lyrica0954.protocol.ISessionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import org.jetbrains.annotations.Nullable;

import java.util.Hashtable;

public class SessionManager implements ISessionManager<MineleftSession> {

	protected Hashtable<ChannelId, MineleftSession> sessions;

	public SessionManager() {
		this.sessions = new Hashtable<>();
	}

	@Override
	public MineleftSession createNewSession(ChannelHandlerContext channelHandlerContext) {
		return new MineleftSession(channelHandlerContext);
	}

	public Hashtable<ChannelId, MineleftSession> getSessions() {
		return sessions;
	}

	public void add(MineleftSession session) {
		this.sessions.put(session.getContext().channel().id(), session);
	}

	public void remove(ChannelId id) {
		MineleftSession session = this.get(id);

		if (session != null) {
			session.remove();
		}

		this.sessions.remove(id);
	}

	public @Nullable MineleftSession get(ChannelId id) {
		return this.sessions.get(id);
	}
}
