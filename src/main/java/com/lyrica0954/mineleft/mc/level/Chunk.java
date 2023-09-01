package com.lyrica0954.mineleft.mc.level;

import com.lyrica0954.mineleft.mc.PaletteBlock;
import io.netty.buffer.ByteBuf;

import java.util.List;

public class Chunk {

	private static final int MIN_SUBCHUNK_INDEX = -4;

	private static final int MAX_SUBCHUNK_INDEX = 19;

	protected SubChunk[] subChunks;

	public Chunk(int maxSubchunk) {
		this.subChunks = new SubChunk[maxSubchunk];
	}

	public Chunk() {
		this.subChunks = new SubChunk[0];
	}

	public Chunk(List<SubChunk> subChunks) {
		this.subChunks = new SubChunk[subChunks.size()];

		for (int i = 0; i < subChunks.size(); i++) {
			this.subChunks[i] = subChunks.get(i);
		}
	}

	public void setBlock(int x, int y, int z, PaletteBlock block) {
		this.getSubchunk(y >> SubChunk.COORD_BIT_SIZE).setBlock(x, y & SubChunk.COORD_MASK, z, block);
	}

	public PaletteBlock getBlock(int x, int y, int z) {
		return this.getSubchunk(y >> SubChunk.COORD_BIT_SIZE).getBlock(x, y & SubChunk.COORD_MASK, z);
	}

	public SubChunk getSubchunk(int y) {
		if (y < MIN_SUBCHUNK_INDEX || y > MAX_SUBCHUNK_INDEX) {
			throw new IndexOutOfBoundsException("index: " + y);
		}

		return this.subChunks[y - MIN_SUBCHUNK_INDEX];
	}

	public void read(ByteBuf buf) throws Exception {
		int maxSize = buf.readInt();

		this.subChunks = new SubChunk[maxSize];

		for (int i = 0; i < maxSize; i++) {
			int y = buf.readInt();
			SubChunk subChunk = new SubChunk();
			subChunk.read(buf);

			this.subChunks[y - MIN_SUBCHUNK_INDEX] = subChunk;
		}
	}
}
