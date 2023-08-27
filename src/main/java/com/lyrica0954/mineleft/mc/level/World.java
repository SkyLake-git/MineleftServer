package com.lyrica0954.mineleft.mc.level;

import com.erenck.mortonlib.Morton2D;
import com.lyrica0954.mineleft.mc.Block;
import com.lyrica0954.mineleft.mc.PaletteBlock;
import com.lyrica0954.mineleft.mc.math.Vec3d;
import com.lyrica0954.mineleft.network.NetworkConverters;
import org.jetbrains.annotations.Nullable;

import java.util.Hashtable;

public class World {

	protected Hashtable<Long, Chunk> chunks;

	protected Morton2D morton;

	public World() {
		this.chunks = new Hashtable<>();
		this.morton = new Morton2D();
	}

	public @Nullable Chunk getChunk(int x, int z) {
		return this.chunks.get(this.morton.encode(x, z));
	}

	public @Nullable Block getBlockAt(int x, int y, int z) {
		int chunkX = x >> SubChunk.COORD_BIT_SIZE;
		int chunkZ = z >> SubChunk.COORD_BIT_SIZE;

		Chunk chunk = this.getChunk(chunkX, chunkZ);

		if (chunk == null) {
			return null;
		}

		PaletteBlock paletteBlock = chunk.getBlock(x & SubChunk.COORD_MASK, y, z & SubChunk.COORD_MASK);
		Block block = NetworkConverters.getInstance().getBlockMappings().get(paletteBlock.getNetworkId());

		if (block == null) {
			block = NetworkConverters.getInstance().getBlockMappings().getNullBlock();
		}

		return block;
	}

	public @Nullable Block getBlock(Vec3d vec) {
		return this.getBlockAt((int) Math.floor(vec.x), (int) Math.floor(vec.y), (int) Math.floor(vec.z));
	}

	public void setChunk(int x, int z, Chunk chunk) {
		this.chunks.put(this.morton.encode(x, z), chunk);
	}
}
