package com.lyrica0954.mineleft.network.player;

import com.lyrica0954.mineleft.mc.VanillaBlockIdentifiers;
import com.lyrica0954.mineleft.mc.WorldBlock;
import com.lyrica0954.mineleft.mc.level.WorldInterface;
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

	protected WorldInterface world;

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

	protected float actuallyForwardSpeed;

	protected double sizeWidth;

	protected double sizeHeight;

	public MinecraftEntityLiving(WorldInterface world, Vec3d position, double sizeWidth, double sizeHeight) {
		this.position = position;
		this.motion = new Vec3d();
		this.rot = new EntityRot();
		this.sizeWidth = sizeWidth;
		this.sizeHeight = sizeHeight;
		this.recalculateBoundingBox();
		this.world = world;
		this.isCollidedHorizontally = false;
		this.isCollidedVertically = false;
		this.movementSpeed = 0.1f;
		this.stepHeight = 0.6f;
		this.jumpVelocity = 0.42f;
		this.onGround = false;
		this.sneaking = false;
		this.jumping = false;
		this.sprinting = false;
		this.jumpCoolDown = 0;
		this.sidewaysSpeed = 0.0f;
		this.forwardSpeed = 0.0f;
		this.upwardSpeed = 0.0f;
		this.flyingSpeed = 0.02f;
		this.actuallyForwardSpeed = 0.0f;
		this.touchingWater = false;
		this.fluidHeights = new HashMap<>();
	}

	protected void recalculateBoundingBox() {
		double w = this.sizeWidth;
		this.boundingBox = new AxisAlignedBB(
				this.position.x - w / 2d,
				this.position.y,
				this.position.z - w / 2d,
				this.position.x + w / 2d,
				this.position.y + this.sizeHeight,
				this.position.z + w / 2d
		);
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
		this.position = position;
		this.recalculateBoundingBox();
	}

	public WorldInterface getWorld() {
		return world;
	}

	public void setWorld(WorldInterface world) {
		this.world = world;
	}

	public Vec3d getMotion() {
		return motion;
	}

	public void setMotion(Vec3d motion) {
		this.motion = motion.copy();
	}

	public AxisAlignedBB getBoundingBox() {
		return boundingBox;
	}

	public void setMotion(double x, double y, double z) {
		this.motion.x = x;
		this.motion.y = y;
		this.motion.z = z;
	}

	public EntityRot getRot() {
		return rot;
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
		System.out.println("Internal move diff: " + this.motion.toString());
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

		double x = this.motion.x;
		double y = this.motion.y;
		double z = this.motion.z;

		if (Math.abs(x) < 0.00003) {
			x = 0.0;
		}

		if (Math.abs(y) < 0.00003) {
			y = 0.0;
		}

		if (Math.abs(z) < 0.00003) {
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
			} else if (this.onGround && this.jumpCoolDown == 0) {
				this.jump();
				this.jumpCoolDown = 10;
			}
		} else {
			this.jumpCoolDown = 0;
		}

		this.travel(new Vec3d(this.sidewaysSpeed * 0.98f, this.upwardSpeed, this.forwardSpeed * 0.98f));
	}

	protected void jump() {
		float f = this.jumpVelocity;

		// todo: jump boost

		Vec3d motion = this.getMotion();
		this.setMotion(motion.x, f, motion.z);

		if (this.isSprinting()) {
			float g = this.rot.yaw * ((float) Math.PI / 180);

			this.setMotion(this.getMotion().add(-Math.sin(g) * 0.2f, 0.0, Math.cos(g) * 0.2f));
		}
	}

	protected void swimUpward() {
		this.setMotion(this.getMotion().add(0, 0.04, 0));
	}

	public void travel(Vec3d movementInput) {
		// todo: support

		double d = 0.08;

		WorldBlock block = this.getWorld().getBlock(this.getMotionAffectingPosition());

		float p = block.getFriction();
		float f = this.onGround ? p * 0.91f : 0.91f;

		System.out.println("Block: " + block.getIdentifier() + " p: " + p);

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
		WorldBlock block = this.getWorld().getBlock(this.position);

		return block.isClimbable();
	}

	private float getVelocitySpeed(float friction) {
		if (this.onGround) {
			return this.getMovementSpeed() * (0.21600002f / (friction * friction * friction));
		}

		return this.flyingSpeed;
	}

	public boolean isSprinting() {
		return sprinting;
	}

	public void setSprinting(boolean sprinting) {
		this.sprinting = sprinting;
	}

	public boolean isSneaking() {
		return sneaking;
	}

	public float getMovementSpeed() {
		return this.movementSpeed;
	}

	private void resetPositionToBoundingBox() {
		this.position.x = (this.boundingBox.minX + this.boundingBox.maxX) / 2d;
		this.position.y = this.boundingBox.minY;
		this.position.z = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2d;
	}

	public void moveEntity(double x, double y, double z) {
		List<AxisAlignedBB> boxes = this.world.getCollisionBoxes(this.boundingBox.copy().addCoord(x, y, z));

		double bx = x;
		double by = y;
		double bz = z;

		AxisAlignedBB bb = this.boundingBox.copy();

		System.out.println(bb.toString());
		System.out.println("collides:");

		for (AxisAlignedBB bbY : boxes) {
			System.out.println(bbY.toString());
			y = bbY.calculateYOffset(bb, y);
		}

		bb.offset(0d, y, 0d);
		boolean wantedVertically = this.onGround || (by != y && by < 0.0d);

		for (AxisAlignedBB bbX : boxes) {
			x = bbX.calculateXOffset(bb, x);
		}

		bb.offset(x, 0d, 0d);

		for (AxisAlignedBB bbZ : boxes) {
			z = bbZ.calculateZOffset(bb, z);
		}

		bb.offset(0d, 0d, z);

		if (this.stepHeight > 0.0f && wantedVertically && (bx != x || bz != z)) {
			double cx = x;
			double cy = y;
			double cz = z;
			x = bx;
			y = this.stepHeight;
			z = bz;

			AxisAlignedBB stepBB = this.boundingBox.copy();

			List<AxisAlignedBB> stepList = this.getWorld().getCollisionBoxes(stepBB.copy().addCoord(x, y, z));

			for (AxisAlignedBB bbY : stepList) {
				y = bbY.calculateYOffset(stepBB, y);
			}

			stepBB.offset(0d, y, 0d);

			for (AxisAlignedBB bbX : stepList) {
				x = bbX.calculateXOffset(stepBB, x);
			}

			stepBB.offset(x, 0d, 0d);

			for (AxisAlignedBB bbZ : stepList) {
				z = bbZ.calculateZOffset(stepBB, z);
			}

			stepBB.offset(0d, 0d, z);

			double reverseY = -y;
			for (AxisAlignedBB bbRY : stepList) {
				reverseY = bbRY.calculateYOffset(stepBB, reverseY);
			}

			y += reverseY;

			stepBB.offset(0d, reverseY, 0d);

			if ((cx * cx + cz * cz) >= (x * x + z * z)) {
				x = cx;
				y = cy;
				z = cz;
			} else {
				bb = stepBB;
			}
		}

		this.boundingBox = bb;

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

		if (this.isCollidedVertically) {
			this.motion.y = 0d;
		}
	}
}
