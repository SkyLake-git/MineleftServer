package com.lyrica0954.mineleft.network.player.logic;

import com.lyrica0954.mineleft.mc.BlockStateUtils;
import com.lyrica0954.mineleft.mc.WorldBlock;
import com.lyrica0954.mineleft.mc.constants.BlockGroup;
import com.lyrica0954.mineleft.mc.level.WorldInterface;
import com.lyrica0954.mineleft.mc.math.Facing;
import com.lyrica0954.mineleft.mc.math.Vec3f;
import com.lyrica0954.mineleft.mc.math.Vec3i;
import com.lyrica0954.mineleft.utils.MathHelper;

public class FluidLogic {

	public static Vec3f getVelocity(WorldInterface world, Vec3i position) {
		WorldBlock origin = world.getBlock(position);
		if (!origin.isLiquid()) {
			return new Vec3f();
		}

		float originHeight = getFluidHeight(world, origin);

		float x = 0.0f;
		float z = 0.0f;
		int o = 0;

		for (Facing facing : Facing.HORIZONTAL) {
			WorldBlock block = world.getBlock(position.addVector(facing.getOffset()));

			if (!block.isLiquid()) {
				continue;
			}

			float height = getFluidHeight(world, block);
			float g = 0.0f;

			if (MathHelper.equals(height, 0.0f)) {
				WorldBlock underFlowBlock = world.getBlock(position.addVector(facing.getOffset()).add(0, -1, 0));
				height = getFluidHeight(world, underFlowBlock);
				if (underFlowBlock.isLiquid() && height > 1e-7f) {
					g = originHeight - (height - 0.8888889f);
				}
			} else if (height > 1e-7f) {
				g = originHeight - height;
			}

			if (MathHelper.equals(g, 0.0f)) {
				continue;
			}

			x += facing.getOffset().x * g;
			z += facing.getOffset().z * g;
			++o;
		}

		Vec3f vel = new Vec3f(x, 0.0f, z).multiply(1.0f / o);

		if (BlockStateUtils.isLiquidFalling(origin)) {
			for (Facing facing : Facing.HORIZONTAL) {
				WorldBlock flowBlock = world.getBlock(position.addVector(facing.getOffset()));
				WorldBlock upperFlowBlock = world.getBlock(position.addVector(facing.getOffset()).add(0, 1, 0));
				if (flowBlock.isLiquid() || upperFlowBlock.isLiquid()) {
					continue;
				}

				// fixme: additional checks

				vel = vel.normalize().add(0f, -6.0f, 0f);
				break;
			}
		}

		return vel.normalize();
	}

	public static float getFluidHeight(WorldInterface world, WorldBlock fluid) {
		WorldBlock above = world.getBlock(fluid.getPosition().add(0, 1, 0));
		if (above.isLiquid() && BlockGroup.get(fluid.getIdentifier()) == BlockGroup.get(above.getIdentifier())) {
			return 1.0f;
		}

		return (8 - BlockStateUtils.getLiquidDecay(fluid)) / 9.0f;
	}
}
