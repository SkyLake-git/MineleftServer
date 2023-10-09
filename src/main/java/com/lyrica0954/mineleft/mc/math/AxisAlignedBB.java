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

	public AxisAlignedBB copy() {
		return new AxisAlignedBB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
	}

	public AxisAlignedBB expand(double x, double y, double z) {
		this.minX -= x;
		this.minY -= y;
		this.minZ -= z;
		this.maxX += x;
		this.maxY += y;
		this.maxZ += z;

		return this;
	}

	public AxisAlignedBB offset(double x, double y, double z) {
		this.minX += x;
		this.minY += y;
		this.minZ += z;
		this.maxX += x;
		this.maxY += y;
		this.maxZ += z;

		return this;
	}

	@Override
	public String toString() {
		return String.format("AxisAlignedBB(minX=%f,minY=%f,minZ=%f,maxX=%f,maxY=%f,maxZ=%f)", this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
	}

	public AxisAlignedBB addCoord(double x, double y, double z) {
		if (x < 0) {
			this.minX += x;
		} else if (x > 0) {
			this.maxX += x;
		}

		if (y < 0) {
			this.minY += y;
		} else if (y > 0) {
			this.maxY += y;
		}

		if (z < 0) {
			this.minZ += z;
		} else if (z > 0) {
			this.maxZ += z;
		}

		return this;
	}

	public AxisAlignedBB offsetCopy(double x, double y, double z) {
		return this.copy().offset(x, y, z);
	}

	public AxisAlignedBB contract(double x, double y, double z) {
		this.minX += x;
		this.minY += y;
		this.minZ += z;
		this.maxX -= x;
		this.maxY -= y;
		this.maxZ -= z;

		return this;
	}

	public double calculateXOffset(AxisAlignedBB bb, double x) {
		if (bb.maxY <= this.minY || bb.minY >= this.maxY) {
			return x;
		}
		if (bb.maxZ <= this.minZ || bb.minZ >= this.maxZ) {
			return x;
		}

		if (x > 0 && bb.maxX <= this.minX) {
			double x1 = this.minX - bb.maxX;
			if (x1 < x) {
				x = x1;
			}
		} else if (x < 0 && bb.minX >= this.maxX) {
			double x2 = this.maxX - bb.minX;
			if (x2 > x) {
				x = x2;
			}
		}

		return x;
	}

	public double calculateYOffset(AxisAlignedBB bb, double y) {
		if (bb.maxX <= this.minX || bb.minX >= this.maxX) {
			return y;
		}
		if (bb.maxZ <= this.minZ || bb.minZ >= this.maxZ) {
			return y;
		}

		if (y > 0 && bb.maxY <= this.minY) {
			double y1 = this.minY - bb.maxY;
			if (y1 < y) {
				y = y1;
			}
		} else if (y < 0 && bb.minY >= this.maxY) {
			double y2 = this.maxY - bb.minY;
			if (y2 > y) {
				y = y2;
			}
		}

		return y;
	}

	public double calculateZOffset(AxisAlignedBB bb, double z) {
		if (bb.maxX <= this.minX || bb.minX >= this.maxX) {
			return z;
		}
		if (bb.maxY <= this.minY || bb.minY >= this.maxY) {
			return z;
		}

		if (z > 0 && bb.maxZ <= this.minZ) {
			double z1 = this.minZ - bb.maxZ;
			if (z1 < z) {
				z = z1;
			}
		} else if (z < 0 && bb.minZ >= this.maxZ) {
			double z2 = this.maxZ - bb.minZ;
			if (z2 > z) {
				z = z2;
			}
		}

		return z;
	}

	public boolean intersectsWith(AxisAlignedBB bb) {
		double epsilon = 0.00001d;
		if (bb.maxX - this.minX > epsilon && this.maxX - bb.minX > epsilon) {
			if (bb.maxY - this.minY > epsilon && this.maxY - bb.minY > epsilon) {
				return bb.maxZ - this.minZ > epsilon && this.maxZ - bb.minZ > epsilon;
			}
		}

		return false;
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
