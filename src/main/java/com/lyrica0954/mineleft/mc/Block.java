package com.lyrica0954.mineleft.mc;

import com.lyrica0954.mineleft.mc.math.AxisAlignedBB;
import com.lyrica0954.mineleft.mc.math.Vec3i;
import com.lyrica0954.mineleft.utils.ByteBufHelper;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

public class Block {

	protected int networkId;

	protected String identifier;

	protected List<AxisAlignedBB> collisionBoxes;

	protected List<AxisAlignedBB> currentCollisionBoxes;

	protected float friction;

	protected int flags;

	protected BlockStateData stateData;

	public Block(int networkId, String identifier, List<AxisAlignedBB> collisionBoxes, float friction) {
		this.networkId = networkId;
		this.identifier = identifier;
		this.collisionBoxes = collisionBoxes;
		this.friction = friction;
		this.flags = 0;
		this.currentCollisionBoxes = collisionBoxes;
	}

	public Block(int networkId, String identifier) {
		this.networkId = networkId;
		this.identifier = identifier;
		this.collisionBoxes = new ArrayList<>();
		this.friction = 0.6f;
		this.flags = 0;
		this.currentCollisionBoxes = new ArrayList<>();
	}

	public Block() {
		this.networkId = 0;
		this.identifier = "";
		this.collisionBoxes = new ArrayList<>();
		this.friction = 0.6f;
		this.flags = 0;
		this.currentCollisionBoxes = new ArrayList<>();
		this.stateData = new BlockStateData();

	}

	public void read(ByteBuf buf) throws Exception {
		this.networkId = buf.readInt();
		this.identifier = ByteBufHelper.readStandardCharSequence(buf);
		this.collisionBoxes = ByteBufHelper.produceList(buf, () -> new AxisAlignedBB().read(buf));
		this.friction = buf.readFloat();
		this.flags = buf.readInt();
		this.stateData.read(buf);
	}

	public void write(ByteBuf buf) throws Exception {
		buf.writeInt(this.networkId);
		ByteBufHelper.writeStandardCharSequence(buf, this.identifier);
		ByteBufHelper.consumeList(buf, this.collisionBoxes, (box) -> box.write(buf));
		buf.writeFloat(this.friction);
		buf.writeInt(this.flags);
		this.stateData.write(buf);
	}

	public boolean isLiquid() {
		return this.hasAttributeFlag(BlockAttributeFlags.LIQUID);
	}

	public boolean isClimbable() {
		return this.hasAttributeFlag(BlockAttributeFlags.CLIMBABLE);
	}

	public boolean hasAttributeFlag(int flag) {
		return (this.flags & (1 << flag)) != 0;
	}

	public int getNetworkId() {
		return networkId;
	}

	public String getIdentifier() {
		return identifier;
	}

	public List<AxisAlignedBB> getRawCollisionBoxes() {
		return collisionBoxes;
	}

	public BlockStateData getStateData() {
		return stateData;
	}

	public List<AxisAlignedBB> getCollisionBoxes() {
		return currentCollisionBoxes;
	}

	public void positionCollisionBoxes(Vec3i position) {
		this.currentCollisionBoxes.clear();
		for (AxisAlignedBB bb : this.collisionBoxes) {
			this.currentCollisionBoxes.add(bb.offsetCopy(position.x, position.y, position.z));
		}
	}

	public float getFriction() {
		return friction;
	}
}
