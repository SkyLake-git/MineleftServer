package com.lyrica0954.mineleft.network.protocol.types;

import com.lyrica0954.mineleft.mc.math.EntityRot;
import com.lyrica0954.mineleft.mc.math.Vec3d;
import com.lyrica0954.mineleft.utils.CodecHelper;
import com.lyrica0954.mineleft.utils.FlagInterface;
import io.netty.buffer.ByteBuf;

public class InputData implements FlagInterface {

	protected long flags;

	protected Vec3d delta;

	protected EntityRot rot;

	protected float moveVecX;

	protected float moveVecZ;

	public InputData() {
		this.flags = 0;
		this.delta = new Vec3d();
		this.moveVecX = 0;
		this.moveVecZ = 0;
		this.rot = new EntityRot();
	}

	public EntityRot getRot() {
		return rot;
	}

	public float getMoveVecX() {
		return moveVecX;
	}

	public float getMoveVecZ() {
		return moveVecZ;
	}

	@Override
	public long getFlags() {
		return flags;
	}

	@Override
	public void setFlags(long flags) {
		this.flags = flags;
	}

	public Vec3d getDelta() {
		return delta;
	}

	public void read(ByteBuf buf) throws Exception {
		this.flags = buf.readLong();
		this.delta = CodecHelper.readVec3d(buf);
		this.moveVecX = buf.readFloat();
		this.moveVecZ = buf.readFloat();
		this.rot = new EntityRot();
		this.rot.yaw = buf.readFloat();
		this.rot.pitch = buf.readFloat();
	}

	public void write(ByteBuf buf) throws Exception {
		buf.writeLong(this.flags);
		CodecHelper.writeVec3d(buf, this.delta);
		buf.writeFloat(this.moveVecX);
		buf.writeFloat(this.moveVecZ);

		buf.writeFloat(this.rot.yaw);
		buf.writeFloat(this.rot.pitch);
	}
}
