package com.lyrica0954.mineleft.mc;

import com.erenck.mortonlib.Morton3D;

import java.util.Hashtable;

public class BlockPalette {

	protected Hashtable<Long, PaletteBlock> palette;

	protected Morton3D morton;

	public BlockPalette() {
		this.palette = new Hashtable<>();
		this.morton = new Morton3D();
	}

	public void set(int x, int y, int z, PaletteBlock block) {
		this.palette.put(this.morton.encode(x, y, z), block);
	}

	public Morton3D getMorton() {
		return morton;
	}

	public PaletteBlock get(int x, int y, int z) {
		return this.palette.getOrDefault(this.morton.encode(x, y, z), new PaletteBlock());
	}
}
