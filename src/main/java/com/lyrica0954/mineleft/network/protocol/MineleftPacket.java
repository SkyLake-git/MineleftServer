package com.lyrica0954.mineleft.network.protocol;

import com.lyrica0954.mineleft.net.Packet;
import org.jetbrains.annotations.NotNull;

abstract public class MineleftPacket implements Packet {

	public String getName(){
		return this.getClass().getSimpleName();
	}

	abstract @NotNull ProtocolIds getProtocolId();
}
