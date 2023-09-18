package com.lyrica0954.mineleft.mc;

import com.lyrica0954.mineleft.mc.math.AxisAlignedBB;
import com.lyrica0954.mineleft.utils.CodecHelper;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

public class Block implements BlockInfo {

	protected int networkId;

	protected String identifier;

	protected List<AxisAlignedBB> collisionBoxes;

	protected List<AxisAlignedBB> currentCollisionBoxes;

	protected float friction;

	protected int flags;

	protected BlockStateData stateData;

	public Block(int networkId, String identifier, List<AxisAlignedBB> collisionBoxes, float friction, int flags, BlockStateData stateData) {
		this.networkId = networkId;
		this.identifier = identifier;
		this.collisionBoxes = collisionBoxes;
		this.friction = friction;
		this.flags = 0;
		this.currentCollisionBoxes = collisionBoxes;
		this.stateData = stateData;
	}

	public Block(int networkId, String identifier) {
		this.networkId = networkId;
		this.identifier = identifier;
		this.collisionBoxes = new ArrayList<>();
		this.friction = 0.6f;
		this.flags = 0;
		this.currentCollisionBoxes = new ArrayList<>();
		this.stateData = new BlockStateData();
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
		this.identifier = CodecHelper.readUTFSequence(buf);
		this.collisionBoxes = CodecHelper.produceList(buf, () -> new AxisAlignedBB().read(buf));
		this.friction = buf.readFloat();
		this.flags = buf.readInt();
		this.stateData.read(buf);
	}

	public void write(ByteBuf buf) throws Exception {
		buf.writeInt(this.networkId);
		CodecHelper.writeUTFSequence(buf, this.identifier);
		CodecHelper.consumeList(buf, this.collisionBoxes, (box) -> box.write(buf));
		buf.writeFloat(this.friction);
		buf.writeInt(this.flags);
		this.stateData.write(buf);
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

	public List<AxisAlignedBB> getUnitCollisionBoxes() {
		return collisionBoxes;
	}

	public BlockStateData getStateData() {
		return stateData;
	}

	public float getFriction() {
		return friction;
	}
}
