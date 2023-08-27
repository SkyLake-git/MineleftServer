package com.lyrica0954.mineleft.mc;

import com.lyrica0954.mineleft.mc.math.AxisAlignedBB;
import com.lyrica0954.mineleft.utils.ByteBufHelper;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

public class Block {

	protected int networkId;

	protected String identifier;

	protected List<AxisAlignedBB> collisionBoxes;

	public Block(int networkId, String identifier, List<AxisAlignedBB> collisionBoxes) {
		this.networkId = networkId;
		this.identifier = identifier;
		this.collisionBoxes = collisionBoxes;
	}

	public Block(int networkId, String identifier) {
		this.networkId = networkId;
		this.identifier = identifier;
		this.collisionBoxes = new ArrayList<>();
	}

	public Block() {
		this.networkId = 0;
		this.identifier = "";
		this.collisionBoxes = new ArrayList<>();
	}

	public void read(ByteBuf buf) throws Exception {
		this.networkId = buf.readInt();
		this.identifier = ByteBufHelper.readStandardCharSequence(buf);
		this.collisionBoxes = ByteBufHelper.produceList(buf, () -> new AxisAlignedBB().read(buf));
	}

	public void write(ByteBuf buf) throws Exception {
		buf.writeInt(this.networkId);
		ByteBufHelper.writeStandardCharSequence(buf, this.identifier);
		ByteBufHelper.consumeList(buf, this.collisionBoxes, (box) -> box.write(buf));
	}

	public int getNetworkId() {
		return networkId;
	}

	public String getIdentifier() {
		return identifier;
	}

	public List<AxisAlignedBB> getCollisionBoxes() {
		return collisionBoxes;
	}
}
