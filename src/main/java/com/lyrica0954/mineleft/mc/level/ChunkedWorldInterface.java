package com.lyrica0954.mineleft.mc.level;

import org.jetbrains.annotations.Nullable;

public interface ChunkedWorldInterface {
	
	void setChunk(int x, int z, Chunk chunk);

	@Nullable Chunk getChunk(int x, int z);
}
