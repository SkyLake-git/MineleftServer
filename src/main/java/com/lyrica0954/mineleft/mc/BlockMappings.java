package com.lyrica0954.mineleft.mc;

import net.intelie.tinymap.TinyMap;
import net.intelie.tinymap.TinyMapBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BlockMappings {

	protected Map<Integer, Block> blocks;

	protected int nullBlockNetworkId;

	protected BlockMappings(Map<Integer, Block> blocks, int nullBlockNetworkId) {
		this.blocks = blocks;
		this.nullBlockNetworkId = nullBlockNetworkId;
	}

	public static Builder builder() {
		return new Builder();
	}

	public @Nullable Block get(int networkId) {
		return this.blocks.get(networkId);
	}

	public Block getNullBlock() {
		return this.blocks.get(this.nullBlockNetworkId);
	}

	public Map<Integer, Block> getRegistered() {
		return blocks;
	}

	public static class Builder {

		protected TinyMapBuilder<Integer, Block> mapBuilder;

		protected Integer nullBlockNetworkId;

		private Builder() {
			this.mapBuilder = TinyMap.builder();
			this.nullBlockNetworkId = null;
		}

		public void register(Block block) {
			this.mapBuilder.put(block.getNetworkId(), block);
		}

		public void setNullBlock(int nullBlockNetworkId) {
			this.nullBlockNetworkId = nullBlockNetworkId;
		}

		public BlockMappings build() {
			if (this.nullBlockNetworkId == null) {
				throw new RuntimeException("Null block not set");
			}

			if (this.mapBuilder.get(this.nullBlockNetworkId) == null) {
				throw new RuntimeException("Null block not registered");
			}

			return new BlockMappings(this.mapBuilder.build(), this.nullBlockNetworkId);
		}

	}
}
