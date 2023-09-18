package com.lyrica0954.mineleft.mc;

import com.lyrica0954.mineleft.mc.math.AxisAlignedBB;

import java.util.List;

public interface BlockInfo {

	boolean hasAttributeFlag(int flag);

	int getNetworkId();

	String getIdentifier();

	List<AxisAlignedBB> getUnitCollisionBoxes();

	BlockStateData getStateData();

	float getFriction();

	default boolean isLiquid() {
		return this.hasAttributeFlag(BlockAttributeFlags.LIQUID);
	}

	default boolean isClimbable() {
		return this.hasAttributeFlag(BlockAttributeFlags.CLIMBABLE);
	}
}
