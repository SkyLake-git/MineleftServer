package com.lyrica0954.mineleft.mc;

import com.lyrica0954.mineleft.mc.constants.BlockStateKeys;

/**
 * @see <a href="https://minecraft.fandom.com/wiki/Water">FlowingWater</a>
 */
public class BlockStateUtils {

	public static int getLiquidDepth(BlockInfo block) {
		Integer liquidDepth = block.getStateData().getIntegerMap().get(BlockStateKeys.LIQUID_DEPTH);
		if (liquidDepth == null) {
			throw new RuntimeException("Block " + block.getIdentifier() + " has no liquid depth");
		}

		return liquidDepth;
	}

	public static int getLiquidDecay(BlockInfo block) {
		return isLiquidFalling(block) ? 0 : Math.min(7, getLiquidDepth(block) & 0x7);
	}

	public static boolean isLiquidFalling(BlockInfo block) {
		return (getLiquidDepth(block) & 0x8) != 0;
	}

	public static boolean isLiquidStill(BlockInfo block) {
		return getLiquidDepth(block) == 0;
	}
}
