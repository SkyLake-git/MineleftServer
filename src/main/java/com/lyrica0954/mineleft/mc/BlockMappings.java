package com.lyrica0954.mineleft.mc;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class BlockMappings {

	protected HashMap<Integer, Block> blocks;

	public BlockMappings() {
		this.blocks = new HashMap<>();
	}

	public void register(Block block) {
		this.blocks.put(block.getNetworkId(), block);
	}

	public @Nullable Block get(int networkId) {
		return this.blocks.get(networkId);
	}

	public Block getNullBlock() {
		return new Block(VanillaBlockNetworkIds.AIR, "minecraft:air");
	}

	public HashMap<Integer, Block> getBlocks() {
		return blocks;
	}
}
