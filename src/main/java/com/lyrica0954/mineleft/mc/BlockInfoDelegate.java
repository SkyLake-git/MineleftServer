package com.lyrica0954.mineleft.mc;

import com.lyrica0954.mineleft.mc.math.AxisAlignedBB;

import java.util.List;

public interface BlockInfoDelegate extends BlockInfo {

	BlockInfo getInfo();

	@Override
	default boolean hasAttributeFlag(int flag) {
		return this.getInfo().hasAttributeFlag(flag);
	}

	@Override
	default int getNetworkId() {
		return this.getInfo().getNetworkId();
	}

	@Override
	default String getIdentifier() {
		return this.getInfo().getIdentifier();
	}

	@Override
	default List<AxisAlignedBB> getUnitCollisionBoxes() {
		return this.getInfo().getUnitCollisionBoxes();
	}

	@Override
	default BlockStateData getStateData() {
		return this.getInfo().getStateData();
	}

	@Override
	default float getFriction() {
		return this.getInfo().getFriction();
	}
}
