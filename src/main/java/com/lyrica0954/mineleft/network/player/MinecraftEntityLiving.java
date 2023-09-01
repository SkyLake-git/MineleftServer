package com.lyrica0954.mineleft.network.player;

import com.lyrica0954.mineleft.mc.Block;
import com.lyrica0954.mineleft.mc.BlockAttributeFlags;
import com.lyrica0954.mineleft.mc.VanillaBlockIdentifiers;
import com.lyrica0954.mineleft.mc.level.World;
import com.lyrica0954.mineleft.mc.math.AxisAlignedBB;
import com.lyrica0954.mineleft.mc.math.EntityRot;
import com.lyrica0954.mineleft.mc.math.Vec3d;
import com.lyrica0954.mineleft.utils.MathHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinecraftEntityLiving {

	public float sidewaysSpeed;

	public float forwardSpeed;

	public float upwardSpeed;

	protected Vec3d position;

	protected Vec3d motion;

	protected EntityRot rot;

	protected AxisAlignedBB boundingBox;

	protected PlayerFlags flags;

	protected World world;

	protected float stepHeight;

	protected boolean onGround;

	protected boolean isCollidedHorizontally;

	protected boolean isCollidedVertically;

	protected float movementSpeed;

	protected float jumpVelocity;

	protected boolean sneaking;

	protected boolean jumping;

	protected boolean sprinting;

	protected boolean touchingWater;

	protected int jumpCoolDown;

	protected float flyingSpeed;

	protected HashMap<String, Float> fluidHeights;

	public MinecraftEntityLiving(World world, Vec3d position, AxisAlignedBB boundingBox) {
		this.position = position;
		this.motion = new Vec3d();
		this.rot = new EntityRot();
		this.boundingBox = boundingBox;
		this.flags = new PlayerFlags();
		this.world = world;
		this.isCollidedHorizontally = false;
		this.isCollidedVertically = false;
		this.movementSpeed = 0.1f;
		this.stepHeight = 0.6f;
		this.jumpVelocity = 0.4f;
		this.onGround = false;
		this.sneaking = false;
		this.jumping = false;
		this.sprinting = false;
		this.jumpCoolDown = 0;
		this.sidewaysSpeed = 0.0f;
		this.forwardSpeed = 0.0f;
		this.upwardSpeed = 0.0f;
		this.flyingSpeed = 0.02f;
		this.touchingWater = false;
		this.fluidHeights = new HashMap<>();
	}

	public boolean isJumping() {
		return jumping;
	}

	public boolean isCollidedHorizontally() {
		return isCollidedHorizontally;
	}

	public boolean isCollidedVertically() {
		return isCollidedVertically;
	}

	public boolean isOnGround() {
		return onGround;
	}

	public Vec3d getPosition() {
		return position;
	}

	public void setPosition(Vec3d position) {
		Vec3d diff = position.subtractVector(this.position);
		this.position = position;
		this.boundingBox.offset(diff.x, diff.y, diff.z);
	}

	public World getWorld() {
		return world;
	}

	public Vec3d getMotion() {
		return motion;
	}

	public void setMotion(Vec3d motion) {
		this.motion = motion;
	}

	public void setMotion(double x, double y, double z) {
		this.motion.x = x;
		this.motion.y = y;
		this.motion.z = z;
	}

	public EntityRot getRot() {
		return rot;
	}

	public void moveEntityWithHeading(float strafe, float forward) {

		if (!this.flags.has(PlayerFlags.IN_WATER)) {
			if (!this.flags.has(PlayerFlags.IN_LAVA)) {
				float f = 0.91f;

				if (this.onGround) {
					f = this.world.getBlock(this.position.subtract(0, 1d, 0)).getFriction();
				}

				float f1 = 0.16277136f / (f * f * f);
				float f5;

				if (this.onGround) {
					f5 = this.movementSpeed * f1;
				} else {
					f5 = this.jumpVelocity;
				}

				this.moveFlying(strafe, forward, f5);

				if (this.flags.has(PlayerFlags.ON_LADDER)) {
					float f6 = 0.15f;

					this.motion.x = MathHelper.clamp(this.motion.x, -f6, f6);
					this.motion.z = MathHelper.clamp(this.motion.z, -f6, f6);

					if (this.motion.y < -0.15d) {
						this.motion.y = -0.15d;
					}

					if (this.sneaking && this.motion.y < 0.0d) {
						this.motion.y = 0.0d;
					}
				}

				this.moveEntity(this.motion.x, this.motion.y, this.motion.z);

				if (this.isCollidedHorizontally && this.flags.has(PlayerFlags.ON_LADDER)) {
					this.motion.y = 0.2d;
				}

				this.motion.y -= 0.08d;

				this.motion.y *= 0.9800000190734863d;
				this.motion.x *= f;
				this.motion.z *= f;
			} else {
				double d = this.position.y;
				this.moveFlying(strafe, forward, 0.02f);
				this.moveEntity(this.motion.x, this.motion.y, this.motion.z);
				this.motion = this.motion.multiply(0.5d);
				this.motion.y -= 0.02d;

				AxisAlignedBB bb = this.boundingBox.offsetCopy(this.motion.x, this.motion.y + 0.6000000238418579d - this.position.y + d, this.motion.z);
				if (this.isCollidedHorizontally && this.world.hasBlockIn(bb, Block::isLiquid)) {
					this.motion.y = 0.30000001192092896d;
				}
			}
		} else {
			double d1 = this.position.y;
			float f1 = 0.8f;
			float f2 = 0.02f;

			this.moveFlying(strafe, forward, f2);
			this.moveEntity(this.motion.x, this.motion.y, this.motion.z);

			this.motion.x *= f1;
			this.motion.y *= 0.800000011920929d;
			this.motion.z *= f1;
			this.motion.y -= 0.02d;

			AxisAlignedBB bb = this.boundingBox.offsetCopy(this.motion.x, this.motion.y + 0.6000000238418579d - this.position.y + d1, this.motion.z);
			if (this.isCollidedHorizontally && this.world.hasBlockIn(bb, Block::isLiquid)) {
				this.motion.y = 0.30000001192092896d;
			}
		}

		this.checkLiquidState(); // todo: move to baseTick()
	}

	public void checkLiquidState() {
		//todo: updateMovementInFluid

		HashMap<String, Integer> depths = new HashMap<>();
		this.touchingWater = this.getWorld().hasBlockIn(this.boundingBox, (block -> block.isLiquid() && (depths.put(block.getIdentifier(), block.getStateData().getIntegerMap().get("liquid_depth")) != null) && block.getIdentifier().equals(VanillaBlockIdentifiers.WATER)));

		this.fluidHeights.clear();

		for (Map.Entry<String, Integer> entry : depths.entrySet()) {
			// wtf liquid depth 0 ~ 15
			int decay = MathHelper.floor((double) entry.getValue() / 2);
			this.fluidHeights.put(entry.getKey(), (decay + 1) / 8f);
			// fixme: decay calculation
		}
	}

	protected float getFluidHeight(String identifier) {
		return this.fluidHeights.getOrDefault(identifier, 0f);
	}

	public void updateVelocity(float speed, Vec3d movementInput) {
		Vec3d move = MinecraftPhysics.movementInputToVelocity(movementInput, speed, this.rot.yaw);
		this.setMotion(this.getMotion().addVector(move));
	}

	public Vec3d moveWithHeading(Vec3d movementInput, float friction) {
		this.updateVelocity(this.getVelocitySpeed(friction), movementInput);
		this.setMotion(this.applyClimbing(this.getMotion()));
		this.moveEntity(this.motion.x, this.motion.y, this.motion.z);

		Vec3d motion = this.getMotion();

		if ((this.isCollidedHorizontally || this.jumping) && this.isClimbing()) {
			motion = new Vec3d(motion.x, 0.2, motion.z);
		}

		return motion;
	}

	protected Vec3d getMotionAffectingPosition() {
		return new Vec3d(this.position.x, this.boundingBox.minY - 0.5000001, this.position.z);
	}

	public void updateMovement() {
		if (this.jumpCoolDown > 0) {
			--this.jumpCoolDown;
		}
		this.setMotion(this.getMotion().multiply(0.98));

		double x = this.motion.x;
		double y = this.motion.y;
		double z = this.motion.z;

		if (Math.abs(x) < 0.003) {
			x = 0.0;
		}

		if (Math.abs(y) < 0.003) {
			y = 0.0;
		}

		if (Math.abs(z) < 0.003) {
			z = 0.0;
		}

		this.setMotion(x, y, z);

		if (false) { //todo: immobile
			this.jumping = false;
			this.sidewaysSpeed = 0.0f;
			this.forwardSpeed = 0.0f;
		}

		if (this.jumping) {
			boolean bl = this.touchingWater; // fixme: check fluid heights

			// todo: check eye heights
			if (bl && !this.onGround) {
				this.swimUpward();
			} else if (this.onGround) {
				this.jump();
				this.jumpCoolDown = 10;
			}
		} else {
			this.jumpCoolDown = 0;
		}

		this.sidewaysSpeed *= 0.98f;
		this.forwardSpeed *= 0.98f;

		this.travel(new Vec3d(this.sidewaysSpeed, this.upwardSpeed, this.forwardSpeed));
	}

	protected void jump() {
		float f = this.jumpVelocity;

		// todo: jump boost

		Vec3d motion = this.getMotion();
		this.setMotion(motion.x, f, motion.z);

		if (this.sprinting) {
			float g = this.rot.yaw * ((float) Math.PI / 180);

			this.setMotion(this.getMotion().add(-Math.sin(g) * 0.2f, 0.0, Math.cos(g) * 0.2f));
		}
	}

	protected void swimUpward() {
		this.setMotion(this.getMotion().add(0, 0.04, 0));
	}

	public void travel(Vec3d movementInput) {
		// todo: support liquids

		double d = 0.08;

		Block block = this.getWorld().getBlock(this.getMotionAffectingPosition());

		float p = block.getFriction();
		float f = this.onGround ? p * 0.91f : 0.91f;


		Vec3d motion = this.moveWithHeading(movementInput, p);

		double q = motion.y;

		q -= d;

		this.setMotion(motion.x * f, q * 0.98f, motion.z * f);
	}

	private Vec3d applyClimbing(Vec3d motion) {
		if (this.isClimbing()) {
			float f = 0.15f;
			double d = MathHelper.clamp(motion.x, -0.15d, 0.15d);
			double e = MathHelper.clamp(motion.z, -0.15d, 0.15d);
			double g = Math.max(motion.y, 0.15d);

			motion = new Vec3d(d, g, e);
		}

		return motion;
	}

	private boolean isClimbing() {
		Block block = this.getWorld().getBlock(this.position);

		return block.hasAttributeFlag(BlockAttributeFlags.CLIMBABLE);
	}

	private float getVelocitySpeed(float friction) {
		if (this.onGround) {
			return this.getMovementSpeed() * (0.21600002f / (friction * friction * friction));
		}

		return this.flyingSpeed;
	}

	public float getMovementSpeed() {
		return this.movementSpeed;
	}

	public void moveFlying(float strafe, float forward, float friction) {
		float f = strafe * strafe + forward * forward;

		if (f >= 1.0E-4F) {
			f = MathHelper.sqrtFloat(f);

			if (f < 1.0F) {
				f = 1.0F;
			}

			f = friction / f;
			strafe = strafe * f;
			forward = forward * f;
			float f1 = (float) Math.sin(this.rot.yaw * (float) Math.PI / 180.0F);
			float f2 = (float) Math.cos(this.rot.yaw * (float) Math.PI / 180.0F);
			this.motion.x += strafe * f2 - forward * f1;
			this.motion.z += forward * f2 + strafe * f1;
		}
	}

	private void resetPositionToBoundingBox() {
		this.position.x = (this.boundingBox.minX + this.boundingBox.maxX) / 2d;
		this.position.y = this.boundingBox.minY;
		this.position.z = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2d;
	}

	public void moveEntity(double x, double y, double z) {
		double dx = this.position.x;
		double dy = this.position.y;
		double dz = this.position.z;

		if (this.flags.has(PlayerFlags.IN_WEB)) {
			this.flags.remove(PlayerFlags.IN_WEB);

			x *= 0.25d;
			y *= 0.05000000074505806d;
			z *= 0.25d;

			this.motion.x = 0d;
			this.motion.y = 0d;
			this.motion.z = 0d;
		}

		List<AxisAlignedBB> boxes = this.world.getCollisionBoxes(this.boundingBox.copy().addCoord(x, y, z));

		double bx = x;
		double by = y;
		double bz = z;

		AxisAlignedBB beforeBB = this.boundingBox.copy();

		for (AxisAlignedBB bbY : boxes) {
			y = bbY.calculateYOffset(this.boundingBox, y);
		}

		this.boundingBox = this.boundingBox.offsetCopy(0d, y, 0d);
		boolean wantedVertically = this.onGround || by != y && by < 0.0d;

		for (AxisAlignedBB bbX : boxes) {
			x = bbX.calculateXOffset(this.boundingBox, x);
		}

		this.boundingBox = this.boundingBox.offsetCopy(x, 0d, 0d);

		for (AxisAlignedBB bbZ : boxes) {
			z = bbZ.calculateZOffset(this.boundingBox, z);
		}

		this.boundingBox = this.boundingBox.offsetCopy(0d, 0d, z);

		if (this.stepHeight > 0.0f && wantedVertically && (bx != x || bz != z)) {
			double bx2 = x;
			double by2 = y;
			double bz2 = z;

			y = this.stepHeight;

			this.boundingBox = beforeBB;

			AxisAlignedBB beforeBB2 = this.boundingBox.copy();

			List<AxisAlignedBB> boxes2 = this.world.getCollisionBoxes(this.boundingBox.addCoord(bx, y, bz));

			AxisAlignedBB wbb = this.boundingBox.copy();
			AxisAlignedBB bbn = wbb.addCoord(bx, 0d, bz);

			double wy = y;

			for (AxisAlignedBB bbnY : boxes2) {
				wy = bbnY.calculateYOffset(bbn, wy);
			}

			wbb = wbb.offsetCopy(0d, wy, 0d);
			double wx = bx;

			for (AxisAlignedBB bbnX : boxes2) {
				wx = bbnX.calculateXOffset(wbb, wx);
			}

			wbb = wbb.offsetCopy(wx, 0d, 0d);
			double wz = bz;

			for (AxisAlignedBB bbnZ : boxes2) {
				wz = bbnZ.calculateZOffset(wbb, wz);
			}

			wbb = wbb.offsetCopy(0d, 0d, wz);

			AxisAlignedBB wbbf = this.boundingBox.copy();
			double wyf = y;

			for (AxisAlignedBB bbnfY : boxes2) {
				wyf = bbnfY.calculateYOffset(wbbf, wyf);
			}

			wbbf = wbbf.offsetCopy(0d, wyf, 0d);
			double wxf = bx;

			for (AxisAlignedBB bbnfX : boxes2) {
				wxf = bbnfX.calculateXOffset(wbbf, wxf);
			}

			wbbf = wbbf.offsetCopy(wxf, 0d, 0d);
			double wzf = bz;

			for (AxisAlignedBB bbnfZ : boxes2) {
				wzf = bbnfZ.calculateZOffset(wbbf, wzf);
			}

			wbbf = wbbf.offsetCopy(0d, 0d, wzf);
			double d2 = wx * wx + wz * wz;
			double d1 = wxf * wxf + wzf * wzf;

			if (d2 > d1) {
				x = wx;
				z = wz;
				y = -wy;
				this.boundingBox = wbb;
			} else {
				x = wxf;
				z = wzf;
				y = -wyf;
				this.boundingBox = wbbf;
			}

			for (AxisAlignedBB fbb : boxes2) {
				y = fbb.calculateYOffset(this.boundingBox, y);
			}

			this.boundingBox = this.boundingBox.offsetCopy(0d, y, 0d);

			if (bx2 * bx2 + bz2 * bz2 > x * x + z * z) {
				x = bx2;
				y = by2;
				z = bz2;
				this.boundingBox = beforeBB2;
			}
		}

		this.resetPositionToBoundingBox();

		this.isCollidedHorizontally = bx != x || bz != z;
		this.isCollidedVertically = by != y;
		this.onGround = this.isCollidedVertically && by < 0d;

		if (bx != x) {
			this.motion.x = 0d;
		}

		if (bz != z) {
			this.motion.z = 0d;
		}

		if (by != y) {
			this.motion.y = 0d; // fixme: is this right?
		}
	}
}
