package com.lyrica0954.mineleft.mc.level;

import com.lyrica0954.mineleft.mc.WorldBlock;
import com.lyrica0954.mineleft.mc.math.AxisAlignedBB;
import com.lyrica0954.mineleft.mc.math.Vec3d;
import com.lyrica0954.mineleft.utils.MathHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public interface WorldInterface {

	boolean isBlockLoaded(int x, int y, int z);

	boolean isInWorld(int x, int y, int z);

	WorldBlock getBlockAt(int x, int y, int z);

	default WorldBlock getBlock(Vec3d vec) {
		return this.getBlockAt(MathHelper.floor(vec.x), MathHelper.floor(vec.y), MathHelper.floor(vec.z));
	}

	default Stream<WorldBlock> getBlocksInBoundingBox(AxisAlignedBB bb) {
		double epsilon = 0.0001d;
		int minX = MathHelper.floor(bb.minX - epsilon - 1.0d);
		int minY = MathHelper.floor(bb.minY - epsilon - 1.0d);
		int minZ = MathHelper.floor(bb.minZ - epsilon - 1.0d);
		int maxX = MathHelper.floor(bb.maxX + epsilon + 1.0d);
		int maxY = MathHelper.floor(bb.maxY + epsilon + 1.0d);
		int maxZ = MathHelper.floor(bb.maxZ + epsilon + 1.0d);

		Stream.Builder<WorldBlock> stream = Stream.builder();

		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				for (int z = minZ; z < maxZ; z++) {
					WorldBlock block = this.getBlockAt(x, y, z);

					stream.add(block);
				}
			}
		}

		return stream.build();
	}

	default List<AxisAlignedBB> getCollisionBoxes(AxisAlignedBB bb) {
		List<AxisAlignedBB> list = new ArrayList<>();

		for (WorldBlock block : this.getBlocksInBoundingBox(bb).toList()) {
			for (AxisAlignedBB blockBB : block.getCollisionBoxes()) {
				if (blockBB.intersectsWith(bb)) {
					list.add(blockBB);
				}
			}
		}

		return list;
	}

	default boolean hasBlockIn(AxisAlignedBB bb, Function<WorldBlock, Boolean> observer) {
		for (WorldBlock block : this.getBlocksInBoundingBox(bb).toList()) {
			if (observer.apply(block)) {
				return true;
			}
		}

		return false;
	}
}
