package com.lyrica0954.mineleft.mc.level;

import com.lyrica0954.mineleft.mc.Block;
import com.lyrica0954.mineleft.mc.BlockMappings;
import com.lyrica0954.mineleft.mc.WorldBlock;
import com.lyrica0954.mineleft.mc.math.AxisAlignedBB;
import com.lyrica0954.mineleft.mc.math.Vec3i;
import com.lyrica0954.mineleft.network.NetworkConverters;

import java.util.ArrayList;
import java.util.List;

public class TemporaryWorld implements WorldInterface {

	protected BlockPalette palette;

	public TemporaryWorld() {
		this.palette = new BlockPalette();
	}

	public TemporaryWorld(BlockPalette.Builder builder) {
		this.palette = builder.build();
	}

	@Override
	public boolean isBlockLoaded(int x, int y, int z) {
		return this.palette.get(x, y, z) != null;
	}

	@Override
	public boolean isInWorld(int x, int y, int z) {
		return true;
	}

	@Override
	public WorldBlock getBlockAt(int x, int y, int z) {
		Block blockInfo;

		PaletteBlock paletteBlock = this.palette.get(x, y, z);

		BlockMappings mappings = NetworkConverters.getInstance().getBlockMappings();

		if (paletteBlock == null) {
			blockInfo = mappings.getNullBlock();
		} else {
			blockInfo = mappings.get(paletteBlock.getNetworkId());

			if (blockInfo == null) {
				blockInfo = mappings.getNullBlock();
			}
		}

		List<AxisAlignedBB> boxes = new ArrayList<>();
		for (AxisAlignedBB unitBB : blockInfo.getUnitCollisionBoxes()) {
			boxes.add(unitBB.offsetCopy(x, y, z));
		}

		return new WorldBlock(blockInfo, new Vec3i(x, y, z), boxes);
	}
}
