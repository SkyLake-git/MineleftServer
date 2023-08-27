package com.lyrica0954.mineleft.network.protocol.types;

import com.lyrica0954.mineleft.mc.math.Vec3d;
import com.lyrica0954.mineleft.utils.ByteBufHelper;
import io.netty.buffer.ByteBuf;

public class InputData {

	protected int flags;

	protected Vec3d delta;

	protected float moveVecX;

	protected float moveVecZ;

	public InputData() {
		this.flags = 0;
		this.delta = new Vec3d();
		this.moveVecX = 0;
		this.moveVecZ = 0;
	}

	public float getMoveVecX() {
		return moveVecX;
	}

	public float getMoveVecZ() {
		return moveVecZ;
	}

	public int getFlags() {
		return flags;
	}

	public Vec3d getDelta() {
		return delta;
	}

	public void read(ByteBuf buf) throws Exception {
		this.flags = buf.readInt();
		this.delta = ByteBufHelper.readVec3d(buf);
		this.moveVecX = buf.readFloat();
		this.moveVecZ = buf.readFloat();
	}

	public void write(ByteBuf buf) throws Exception {
		buf.writeInt(this.flags);
		ByteBufHelper.writeVec3d(buf, this.delta);
		buf.writeFloat(this.moveVecX);
		buf.writeFloat(this.moveVecZ);
	}

	public void appendFlag(int flag) {
		this.flags |= (1 << flag);
	}

	public boolean hasFlag(int flag) {
		return (this.flags & (1 << flag)) != 0;
	}
}
