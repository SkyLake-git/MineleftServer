package com.lyrica0954.mineleft.mc;

import com.lyrica0954.mineleft.mc.math.AxisAlignedBB;
import com.lyrica0954.mineleft.mc.math.Vec3i;

import java.util.List;

public class WorldBlock implements BlockInfoDelegate {

	protected Vec3i position;

	protected List<AxisAlignedBB> collisionBoxes;

	protected BlockInfo info;

	public WorldBlock(BlockInfo info, Vec3i position, List<AxisAlignedBB> collisionBoxes) {
		this.info = info;
		this.position = position;
		this.collisionBoxes = collisionBoxes;
	}

	public BlockInfo getInfo() {
		return info;
	}

	public List<AxisAlignedBB> getCollisionBoxes() {
		return collisionBoxes;
	}

	public Vec3i getPosition() {
		return position;
	}
}
