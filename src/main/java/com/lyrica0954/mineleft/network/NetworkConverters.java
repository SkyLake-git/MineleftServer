package com.lyrica0954.mineleft.network;

import com.lyrica0954.mineleft.mc.BlockMappings;

public class NetworkConverters {

	private static NetworkConverters instance = null;

	protected BlockMappings blockMappings;

	private NetworkConverters() {
		this.blockMappings = new BlockMappings();
	}

	public static NetworkConverters getInstance() {
		if (instance == null) {
			instance = new NetworkConverters();
		}
		return instance;
	}

	public BlockMappings getBlockMappings() {
		return blockMappings;
	}


}
