package com.lyrica0954.mineleft.mc.level;

import com.lyrica0954.mineleft.MortonCode;
import com.lyrica0954.mineleft.mc.VanillaBlockNetworkIds;
import net.intelie.tinymap.ObjectCache;
import net.intelie.tinymap.TinyMap;
import net.intelie.tinymap.TinyMapBuilder;
import net.intelie.tinymap.util.ObjectOptimizer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BlockPalette {

	protected Map<Long, Integer> palette;

	public BlockPalette(Map<Long, Integer> palette) {
		this.palette = palette;
	}

	public BlockPalette() {
		this.palette = TinyMap.<Long, Integer>builder().build();
	}

	public static Builder builder() {
		return new Builder();
	}

	private static PaletteBlock parse(int internalData) {
		return new PaletteBlock(internalData);
	}

	private static int encode(PaletteBlock block) {
		return block.networkId;
	}

	public synchronized void set(int x, int y, int z, PaletteBlock block) {
		this.modify(ctx -> ctx.set(x, y, z, block));
	}

	public synchronized void modify(Consumer<ModifyContext> modifier) {
		HashMap<Long, Integer> palette = new HashMap<>(this.palette);

		ModifyContext context = new ModifyContext(palette);
		modifier.accept(context);

		ObjectOptimizer optimizer = new ObjectOptimizer(new ObjectCache());
		this.palette = optimizer.optimizeMap(palette);
	}

	public PaletteBlock get(int x, int y, int z) {
		return parse(this.palette.getOrDefault(MortonCode.get3D().encode(x, y, z), VanillaBlockNetworkIds.AIR));
	}

	public static class ModifyContext {

		protected Map<Long, Integer> palette;

		public ModifyContext(Map<Long, Integer> palette) {
			this.palette = palette;
		}

		public void set(int x, int y, int z, PaletteBlock block) {
			this.palette.put(MortonCode.get3D().encode(x, y, z), encode(block));
		}
	}

	public static class Builder {

		protected TinyMapBuilder<Long, Integer> mapBuilder;

		private Builder() {
			this.mapBuilder = TinyMap.builder();
		}

		public void set(int x, int y, int z, PaletteBlock block) {
			this.mapBuilder.put(MortonCode.get3D().encode(x, y, z), encode(block));
		}

		public BlockPalette build() {
			return new BlockPalette(this.mapBuilder.build());
		}
	}
}
