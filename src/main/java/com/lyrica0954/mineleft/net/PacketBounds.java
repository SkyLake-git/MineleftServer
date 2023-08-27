package com.lyrica0954.mineleft.net;

public enum PacketBounds {
	CLIENT,
	SERVER,
	BOTH;

	public boolean client(){
		return this.equals(CLIENT) || this.equals(BOTH);
	}

	public boolean server(){
		return this.equals(SERVER) || this.equals(BOTH);
	}
}
