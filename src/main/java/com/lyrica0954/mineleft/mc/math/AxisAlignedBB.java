package com.lyrica0954.mineleft.mc.math;

import com.lyrica0954.mineleft.utils.MathHelper;
import io.netty.buffer.ByteBuf;

public class AxisAlignedBB {

	public float minX;

	public float minY;

	public float minZ;

	public float maxX;

	public float maxY;

	public float maxZ;

	public AxisAlignedBB(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
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

	public AxisAlignedBB round(int n) {
		return new AxisAlignedBB(
				MathHelper.round(this.minX, n),
				MathHelper.round(this.minY, n),
				MathHelper.round(this.minZ, n),
				MathHelper.round(this.maxX, n),
				MathHelper.round(this.maxY, n),
				MathHelper.round(this.maxZ, n)
		);
	}

	public AxisAlignedBB expand(float x, float y, float z) {
		this.minX -= x;
		this.minY -= y;
		this.minZ -= z;
		this.maxX += x;
		this.maxY += y;
		this.maxZ += z;

		return this;
	}

	public AxisAlignedBB offset(float x, float y, float z) {
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

	public AxisAlignedBB addCoord(float x, float y, float z) {
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

	public AxisAlignedBB offsetCopy(float x, float y, float z) {
		return this.copy().offset(x, y, z);
	}

	public AxisAlignedBB contract(float x, float y, float z) {
		this.minX += x;
		this.minY += y;
		this.minZ += z;
		this.maxX -= x;
		this.maxY -= y;
		this.maxZ -= z;

		return this;
	}

	public float calculateXOffset(AxisAlignedBB bb, float x) {
		if (bb.maxY <= this.minY || bb.minY >= this.maxY) {
			return x;
		}
		if (bb.maxZ <= this.minZ || bb.minZ >= this.maxZ) {
			return x;
		}

		if (x > 0 && bb.maxX <= this.minX) {
			float x1 = this.minX - bb.maxX;
			if (x1 < x) {
				x = x1;
			}
		} else if (x < 0 && bb.minX >= this.maxX) {
			float x2 = this.maxX - bb.minX;
			if (x2 > x) {
				x = x2;
			}
		}

		// OK

		return x;
	}

	public float calculateYOffset(AxisAlignedBB bb, float y) {
		if (bb.maxX <= this.minX || bb.minX >= this.maxX) {
			return y;
		}
		if (bb.maxZ <= this.minZ || bb.minZ >= this.maxZ) {
			return y;
		}

		if (y > 0 && bb.maxY <= this.minY) {
			float y1 = this.minY - bb.maxY;
			if (y1 < y) {
				y = y1;
			}
		} else if (y < 0 && bb.minY >= this.maxY) {
			float y2 = this.maxY - bb.minY;
			if (y2 > y) {
				y = y2;
			}
		}


		// OK
		return y;
	}

	public float calculateZOffset(AxisAlignedBB bb, float z) {
		if (bb.maxX <= this.minX || bb.minX >= this.maxX) {
			return z;
		}
		if (bb.maxY <= this.minY || bb.minY >= this.maxY) {
			return z;
		}

		if (z > 0 && bb.maxZ <= this.minZ) {
			float z1 = this.minZ - bb.maxZ;
			if (z1 < z) {
				z = z1;
			}
		} else if (z < 0 && bb.minZ >= this.maxZ) {
			float z2 = this.maxZ - bb.minZ;
			if (z2 > z) {
				z = z2;
			}
		}

		return z;
	}

	public boolean intersectsWith(AxisAlignedBB bb) {
		float epsilon = 1e-7f;
		if (bb.maxX - this.minX > epsilon && this.maxX - bb.minX > epsilon) {
			if (bb.maxY - this.minY > epsilon && this.maxY - bb.minY > epsilon) {
				return bb.maxZ - this.minZ > epsilon && this.maxZ - bb.minZ > epsilon;
			}
		}

		return false;
	}

	public AxisAlignedBB read(ByteBuf buf) throws Exception {
		this.minX = buf.readFloat();
		this.minY = buf.readFloat();
		this.minZ = buf.readFloat();
		this.maxX = buf.readFloat();
		this.maxY = buf.readFloat();
		this.maxZ = buf.readFloat();

		return this;
	}

	public void write(ByteBuf buf) throws Exception {
		buf.writeFloat(this.minX);
		buf.writeFloat(this.minY);
		buf.writeFloat(this.minZ);
		buf.writeFloat(this.maxX);
		buf.writeFloat(this.maxY);
		buf.writeFloat(this.maxZ);
	}

}
