package com.lyrica0954.mineleft.net;

public class InboundData {

	private short packetId;

	private Packet packet;

	public Packet getPacket() {
		return packet;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}

	public void setPacketId(short packetId) {
		this.packetId = packetId;
	}

	public short getPacketId() {
		return packetId;
	}
}
