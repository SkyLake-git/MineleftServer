package com.lyrica0954.mineleft.mc.level;

import io.netty.buffer.ByteBuf;

public class PaletteBlock {

	protected int networkId;

	public PaletteBlock(int networkId) {
		this.networkId = networkId;
	}

	public PaletteBlock() {
		this.networkId = 0;
	}

	public int getNetworkId() {
		return networkId;
	}

	public void read(ByteBuf buf) throws Exception {
		this.networkId = buf.readInt();
	}
}
