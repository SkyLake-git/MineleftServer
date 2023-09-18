package com.lyrica0954.mineleft.network;

import com.lyrica0954.mineleft.network.handler.IPacketHandler;
import com.lyrica0954.mineleft.network.handler.MineleftPacketHandler;
import com.lyrica0954.mineleft.network.player.MineleftPlayerProfile;
import com.lyrica0954.mineleft.network.protocol.MineleftPacket;
import com.lyrica0954.mineleft.network.protocol.types.ChunkSendingMethod;
import com.lyrica0954.mineleft.utils.ParameterMethodCaller;
import com.lyrica0954.protocol.ISession;
import com.lyrica0954.protocol.OutboundData;
import com.lyrica0954.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Hashtable;
import java.util.Queue;
import java.util.UUID;

public class MineleftSession implements ISession {

	protected ChannelHandlerContext ctx;

	protected Logger logger;

	protected IPacketHandler packetHandler;

	protected ParameterMethodCaller<MineleftPacket> packetHandlerCaller;

	protected WorldManager worldManager;

	protected Hashtable<UUID, MineleftPlayerProfile> players;

	protected Queue<Packet> sendQueue;

	protected int chunkSendingMethod;

	public MineleftSession(ChannelHandlerContext ctx) {
		this.ctx = ctx;
		this.logger = LoggerFactory.getLogger(String.format("[MineleftSession: %s]", ctx.channel().id().asShortText()));
		this.packetHandler = new MineleftPacketHandler(this);
		this.packetHandlerCaller = new ParameterMethodCaller<>(this.packetHandler);
		this.worldManager = new WorldManager();
		this.players = new Hashtable<>();
		this.sendQueue = new ArrayDeque<>();
		this.chunkSendingMethod = ChunkSendingMethod.REALTIME;

		this.logger.info("New session created");
	}

	public int getChunkSendingMethod() {
		return chunkSendingMethod;
	}

	public void setChunkSendingMethod(int chunkSendingMethod) {
		this.chunkSendingMethod = chunkSendingMethod;
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

	private ByteBuf encodePacket(OutboundData data) {
		ByteBuf buf = Unpooled.buffer();

		buf.writeShort(data.getPacketId());
		try {
			data.getPacket().encode(buf);
		} catch (Exception e) {
			throw new RuntimeException("Packet encoding failed: " + data.getPacketId());
		}

		return buf;
	}

	public void sendPacket(MineleftPacket packet) {
		OutboundData data = new OutboundData();
		data.setPacketId(packet.getProtocolId().id);
		data.setPacket(packet);
		this.ctx.write(this.encodePacket(data));
	}

	public void flushSendQueue() {
		this.ctx.flush();
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
