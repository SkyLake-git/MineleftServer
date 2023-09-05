package com.lyrica0954.mineleft.network;

import com.lyrica0954.mineleft.network.handler.IPacketHandler;
import com.lyrica0954.mineleft.network.handler.MineleftPacketHandler;
import com.lyrica0954.mineleft.network.player.MineleftPlayerProfile;
import com.lyrica0954.mineleft.network.protocol.MineleftPacket;
import com.lyrica0954.mineleft.utils.ParameterMethodCaller;
import com.lyrica0954.protocol.ISession;
import com.lyrica0954.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;
import java.util.UUID;

public class MineleftSession implements ISession {

	protected ChannelHandlerContext ctx;

	protected Logger logger;

	protected IPacketHandler packetHandler;

	protected ParameterMethodCaller<MineleftPacket> packetHandlerCaller;

	protected WorldManager worldManager;

	protected Hashtable<UUID, MineleftPlayerProfile> players;

	public MineleftSession(ChannelHandlerContext ctx) {
		this.ctx = ctx;
		this.logger = LoggerFactory.getLogger(String.format("[MineleftSession: %s]", ctx.channel().id().asShortText()));
		this.packetHandler = new MineleftPacketHandler(this);
		this.packetHandlerCaller = new ParameterMethodCaller<>(this.packetHandler);
		this.worldManager = new WorldManager();
		this.players = new Hashtable<>();

		this.logger.info("New session created");
	}

	public Hashtable<UUID, MineleftPlayerProfile> getPlayers() {
		return players;
	}

	public void addPlayer(MineleftPlayerProfile profile) {
		this.players.put(profile.getInfo().getUuid(), profile);
	}

	public void removePlayer(UUID uuid) {
		this.players.remove(uuid);
	}

	public Logger getLogger() {
		return logger;
	}

	public void remove() {
		this.logger.info("Session removed");
	}

	public WorldManager getWorldManager() {
		return worldManager;
	}

	public void handleException(Throwable ex) {

	}

	public void handlePacket(Packet packet) throws Exception {
		if (!packet.bounds().server()) {
			this.logger.warn("Invalid bounding packet received. ignoring");
			return;
		}

		if (!(packet instanceof MineleftPacket)) {
			this.logger.warn("Non-mineleft packet received. ignoring");
			return;
		}

		try {
			this.packetHandlerCaller.call((MineleftPacket) packet);
		} catch (PacketHandlingException e) {
			this.ctx.disconnect();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public ChannelHandlerContext getContext() {
		return ctx;
	}
}
