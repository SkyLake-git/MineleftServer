package com.lyrica0954.mineleft.network;

import com.lyrica0954.mineleft.mc.BlockMappings;
import org.jetbrains.annotations.Nullable;

public class NetworkConverters {

	private static NetworkConverters instance = null;

	protected @Nullable BlockMappings blockMappings;

	private NetworkConverters() {
		this.blockMappings = null;
	}

	public static NetworkConverters getInstance() {
		if (instance == null) {
			instance = new NetworkConverters();
		}
		return instance;
	}

	public void initializeBlockMappings(BlockMappings mappings) {
		if (this.blockMappings != null) {
			throw new RuntimeException("Already initialized");
		}

		this.blockMappings = mappings;
	}

	public boolean isBlockMappingsInitialized() {
		return this.blockMappings != null;
	}

	public BlockMappings getBlockMappings() {
		if (this.blockMappings == null) {
			throw new RuntimeException("Block mappings not initialized");
		}

		return this.blockMappings;
	}


}
