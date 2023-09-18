package com.lyrica0954.mineleft.mc.level;

import com.lyrica0954.mineleft.MortonCode;
import com.lyrica0954.mineleft.mc.Block;
import com.lyrica0954.mineleft.mc.WorldBlock;
import com.lyrica0954.mineleft.mc.math.AxisAlignedBB;
import com.lyrica0954.mineleft.mc.math.Vec3i;
import com.lyrica0954.mineleft.network.NetworkConverters;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class World implements WorldInterface, ChunkedWorldInterface {

	public static final int MAX_Y = 320;

	public static final int MIN_Y = -64;

	protected Hashtable<Long, Chunk> chunks;

	public World() {
		this.chunks = new Hashtable<>();
	}

	public @Nullable Chunk getChunk(int x, int z) {
		return this.chunks.get(MortonCode.get2D().encode(x, z));
	}

	@Override
	public boolean isBlockLoaded(int x, int y, int z) {
		int chunkX = x >> SubChunk.COORD_BIT_SIZE;
		int chunkZ = z >> SubChunk.COORD_BIT_SIZE;

		Chunk chunk = this.getChunk(chunkX, chunkZ);

		if (chunk == null) {
			return false;
		}

		PaletteBlock paletteBlock = chunk.getBlock(x & SubChunk.COORD_MASK, y, z & SubChunk.COORD_MASK);
		Block block = NetworkConverters.getInstance().getBlockMappings().get(paletteBlock.getNetworkId());

		return block != null;
	}

	public boolean isChunkLoaded(int x, int z) {
		int chunkX = x >> SubChunk.COORD_BIT_SIZE;
		int chunkZ = z >> SubChunk.COORD_BIT_SIZE;

		Chunk chunk = this.getChunk(chunkX, chunkZ);

		return chunk != null;
	}

	@Override
	public boolean isInWorld(int x, int y, int z) {
		return y > MIN_Y && y < MAX_Y;
	}

	public WorldBlock getBlockAt(int x, int y, int z) {
		Block blockInfo;
		if (!this.isInWorld(x, y, z)) {
			blockInfo = NetworkConverters.getInstance().getBlockMappings().getNullBlock();
		} else {
			int chunkX = x >> SubChunk.COORD_BIT_SIZE;
			int chunkZ = z >> SubChunk.COORD_BIT_SIZE;

			Chunk chunk = this.getChunk(chunkX, chunkZ);

			if (chunk == null) {
				blockInfo = NetworkConverters.getInstance().getBlockMappings().getNullBlock();
			} else {
				PaletteBlock paletteBlock = chunk.getBlock(x & SubChunk.COORD_MASK, y, z & SubChunk.COORD_MASK);
				Block blockMapInfo = NetworkConverters.getInstance().getBlockMappings().get(paletteBlock.getNetworkId());

				if (blockMapInfo == null) {
					blockMapInfo = NetworkConverters.getInstance().getBlockMappings().getNullBlock();
				}

				blockInfo = blockMapInfo;
			}
		}
		List<AxisAlignedBB> boxes = new ArrayList<>();

		for (AxisAlignedBB unitBB : blockInfo.getUnitCollisionBoxes()) {
			boxes.add(unitBB.offsetCopy(x, y, z));
		}

		return new WorldBlock(blockInfo, new Vec3i(x, y, z), boxes);
	}

	public synchronized void setChunk(int x, int z, Chunk chunk) {
		this.chunks.put(MortonCode.get2D().encode(x, z), chunk);
	}
}
