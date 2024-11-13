package com.lyrica0954.mineleft.mc.level;

import com.lyrica0954.mineleft.mc.WorldBlock;
import com.lyrica0954.mineleft.mc.math.AxisAlignedBB;
import com.lyrica0954.mineleft.mc.math.Vec3f;
import com.lyrica0954.mineleft.mc.math.Vec3i;
import com.lyrica0954.mineleft.utils.MathHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public interface WorldInterface {

	boolean isBlockLoaded(int x, int y, int z);

	boolean isInWorld(int x, int y, int z);

	WorldBlock getBlockAt(int x, int y, int z);

	default WorldBlock getBlock(Vec3f vec) {
		return this.getBlockAt(MathHelper.floor(vec.x), MathHelper.floor(vec.y), MathHelper.floor(vec.z));
	}

	default WorldBlock getBlock(Vec3i vec) {
		return this.getBlockAt(vec.x, vec.y, vec.z);
	}

	default Stream<WorldBlock> getBlocksInBoundingBox(AxisAlignedBB bb) {
		float epsilon = 0.0001f;
		int minX = MathHelper.floor(bb.minX - epsilon - 1.0f);
		int minY = MathHelper.floor(bb.minY - epsilon - 1.0f);
		int minZ = MathHelper.floor(bb.minZ - epsilon - 1.0f);
		int maxX = MathHelper.floor(bb.maxX + epsilon + 1.0f);
		int maxY = MathHelper.floor(bb.maxY + epsilon + 1.0f);
		int maxZ = MathHelper.floor(bb.maxZ + epsilon + 1.0f);

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

	default List<AxisAlignedBB> getCollisionBoxes(AxisAlignedBB bb, boolean targetFirst) {
		List<AxisAlignedBB> list = new ArrayList<>();

		for (WorldBlock block : this.getBlocksInBoundingBox(bb).toList()) {
			for (AxisAlignedBB blockBB : block.getCollisionBoxes()) {
				if (blockBB.intersectsWith(bb)) {
					list.add(blockBB);
					if (targetFirst) {
						return list;
					}
				}
			}
		}

		return list;
	}

	default List<AxisAlignedBB> getCollisionBoxes(AxisAlignedBB bb) {
		return this.getCollisionBoxes(bb, false);
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
