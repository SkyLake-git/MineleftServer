package com.lyrica0954.mineleft.network.chunk;

import com.lyrica0954.mineleft.mc.level.Chunk;
import io.netty.buffer.ByteBuf;

public class ChunkDeserializer {

	public static Chunk deserialize(ByteBuf buf) throws Exception {
		Chunk chunk = new Chunk();
		chunk.read(buf);

		return chunk;
	}
}
