package com.lyrica0954.mineleft.mc.level;

import com.lyrica0954.mineleft.mc.BlockPalette;
import com.lyrica0954.mineleft.mc.PaletteBlock;
import io.netty.buffer.ByteBuf;

public class SubChunk {

	public static final int COORD_BIT_SIZE = 4;

	public static final int COORD_MASK = ~(~0 << COORD_BIT_SIZE);

	protected BlockPalette palette;

	public SubChunk() {
		this.palette = new BlockPalette();
	}

	public BlockPalette getPalette() {
		return palette;
	}

	public void setBlock(int x, int y, int z, PaletteBlock block) {
		this.palette.set(x, y, z, block);
	}

	public PaletteBlock getBlock(int x, int y, int z) {
		return this.palette.get(x, y, z);
	}

	public void read(ByteBuf buf) throws Exception {
		int count = buf.readInt();
		for (int j = 0; j < count; j++) {
			int layerBlockCount = buf.readInt();

			for (int i = 0; i < layerBlockCount; i++) {
				int[] v = this.palette.getMorton().decode(buf.readLong());

				PaletteBlock block = new PaletteBlock();
				block.read(buf);

				this.setBlock(v[0], v[1] & SubChunk.COORD_MASK, v[2], block);
			}
		}
	}
}
