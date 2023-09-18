package com.lyrica0954.mineleft.mc;

import net.intelie.tinymap.TinyMap;
import net.intelie.tinymap.TinyMapBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class BlockMappings {

	private static final Block NULL_BLOCK;

	static {
		NULL_BLOCK = new Block(VanillaBlockNetworkIds.AIR, "minecraft:air");
	}

	protected Map<Integer, Block> blocks;

	protected BlockMappings(Map<Integer, Block> blocks) {
		this.blocks = blocks;
	}

	public static BlockMappings none() {
		return new BlockMappings(new HashMap<>());
	}

	public static Builder builder() {
		return new Builder();
	}

	public @Nullable Block get(int networkId) {
		return this.blocks.get(networkId);
	}

	public Block getNullBlock() {
		return NULL_BLOCK;
	}

	public Map<Integer, Block> getRegistered() {
		return blocks;
	}

	public static class Builder {

		protected TinyMapBuilder<Integer, Block> mapBuilder;

		private Builder() {
			this.mapBuilder = TinyMap.builder();
		}

		public void register(Block block) {
			this.mapBuilder.put(block.getNetworkId(), block);
		}

		public BlockMappings build() {
			return new BlockMappings(this.mapBuilder.build());
		}

	}
}
