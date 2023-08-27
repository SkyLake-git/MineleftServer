package com.lyrica0954.mineleft.network;

import io.netty.channel.ChannelId;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class SessionManager {

	protected HashMap<ChannelId, MineleftSession> sessions;

	public SessionManager(){
		this.sessions = new HashMap<>();
	}

	public HashMap<ChannelId, MineleftSession> getSessions() {
		return sessions;
	}

	public void add(MineleftSession session){
		this.sessions.put(session.getContext().channel().id(), session);
	}

	public void remove(ChannelId id){
		MineleftSession session = this.get(id);

		if (session != null){
			session.remove();
		}

		this.sessions.remove(id);
	}

	public @Nullable MineleftSession get(ChannelId id){
		return this.sessions.get(id);
	}
}
