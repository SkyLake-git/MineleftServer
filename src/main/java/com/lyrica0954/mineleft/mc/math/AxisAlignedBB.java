package com.lyrica0954.mineleft.mc.math;

import io.netty.buffer.ByteBuf;

public class AxisAlignedBB {

	public double minX;

	public double minY;

	public double minZ;

	public double maxX;

	public double maxY;

	public double maxZ;

	public AxisAlignedBB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	public AxisAlignedBB() {
		this.minX = 0;
		this.minY = 0;
		this.minZ = 0;
		this.maxX = 0;
		this.maxY = 0;
		this.maxZ = 0;
	}

	public AxisAlignedBB read(ByteBuf buf) throws Exception {
		this.minX = buf.readDouble();
		this.minY = buf.readDouble();
		this.minZ = buf.readDouble();
		this.maxX = buf.readDouble();
		this.maxY = buf.readDouble();
		this.maxZ = buf.readDouble();

		return this;
	}

	public void write(ByteBuf buf) throws Exception {
		buf.writeDouble(this.minX);
		buf.writeDouble(this.minY);
		buf.writeDouble(this.minZ);
		buf.writeDouble(this.maxX);
		buf.writeDouble(this.maxY);
		buf.writeDouble(this.maxZ);
	}

}
