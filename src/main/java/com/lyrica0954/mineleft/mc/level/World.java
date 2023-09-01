package com.lyrica0954.mineleft.mc.level;

import com.erenck.mortonlib.Morton2D;
import com.lyrica0954.mineleft.mc.Block;
import com.lyrica0954.mineleft.mc.PaletteBlock;
import com.lyrica0954.mineleft.mc.math.AxisAlignedBB;
import com.lyrica0954.mineleft.mc.math.Vec3d;
import com.lyrica0954.mineleft.mc.math.Vec3i;
import com.lyrica0954.mineleft.network.NetworkConverters;
import com.lyrica0954.mineleft.utils.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

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

	public Block getBlockAt(int x, int y, int z) {
		int chunkX = x >> SubChunk.COORD_BIT_SIZE;
		int chunkZ = z >> SubChunk.COORD_BIT_SIZE;

		Chunk chunk = this.getChunk(chunkX, chunkZ);

		if (chunk == null) {
			return NetworkConverters.getInstance().getBlockMappings().getNullBlock();
		}

		PaletteBlock paletteBlock = chunk.getBlock(x & SubChunk.COORD_MASK, y, z & SubChunk.COORD_MASK);
		Block block = NetworkConverters.getInstance().getBlockMappings().get(paletteBlock.getNetworkId());

		if (block == null) {
			block = NetworkConverters.getInstance().getBlockMappings().getNullBlock();
		}

		block.positionCollisionBoxes(new Vec3i(x, y, z));

		return block;
	}

	public Block getBlock(Vec3d vec) {
		return this.getBlockAt((int) Math.floor(vec.x), (int) Math.floor(vec.y), (int) Math.floor(vec.z));
	}

	public Stream<Block> getBlocksInBoundingBox(AxisAlignedBB bb) {
		double epsilon = 0.0001d;
		int minX = MathHelper.floor(bb.minX - epsilon - 1.0d);
		int minY = MathHelper.floor(bb.minY - epsilon - 1.0d);
		int minZ = MathHelper.floor(bb.minZ - epsilon - 1.0d);
		int maxX = MathHelper.floor(bb.maxX + epsilon + 1.0d);
		int maxY = MathHelper.floor(bb.maxY + epsilon + 1.0d);
		int maxZ = MathHelper.floor(bb.maxZ + epsilon + 1.0d);

		Stream.Builder<Block> stream = Stream.builder();

		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				for (int z = minZ; z < maxZ; z++) {
					Block block = this.getBlockAt(x, y, z);

					stream.add(block);
				}
			}
		}

		return stream.build();
	}

	public List<AxisAlignedBB> getCollisionBoxes(AxisAlignedBB bb) {
		List<AxisAlignedBB> list = new ArrayList<>();

		for (Block block : this.getBlocksInBoundingBox(bb).toList()) {
			for (AxisAlignedBB blockBB : block.getCollisionBoxes()) {
				if (blockBB.intersectsWith(bb)) {
					list.add(blockBB);
				}
			}
		}

		return list;
	}

	public boolean hasBlockIn(AxisAlignedBB bb, Function<Block, Boolean> observer) {
		for (Block block : this.getBlocksInBoundingBox(bb).toList()) {
			if (observer.apply(block)) {
				return true;
			}
		}

		return false;
	}

	public void setChunk(int x, int z, Chunk chunk) {
		this.chunks.put(this.morton.encode(x, z), chunk);
	}
}
