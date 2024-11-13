package com.lyrica0954.mineleft.network.player;

import com.lyrica0954.mineleft.mc.WorldBlock;
import com.lyrica0954.mineleft.mc.constants.BlockGroup;
import com.lyrica0954.mineleft.mc.level.WorldInterface;
import com.lyrica0954.mineleft.mc.math.AxisAlignedBB;
import com.lyrica0954.mineleft.mc.math.EntityRot;
import com.lyrica0954.mineleft.mc.math.Vec3f;
import com.lyrica0954.mineleft.network.player.logic.EntityPhysics;
import com.lyrica0954.mineleft.network.player.logic.FluidLogic;
import com.lyrica0954.mineleft.network.protocol.types.Effect;
import com.lyrica0954.mineleft.utils.MathHelper;

import java.util.HashMap;
import java.util.List;

public class MinecraftEntityLiving {

	public float sidewaysSpeed;

	public float forwardSpeed;

	public float upwardSpeed;

	protected Vec3f position;

	protected Vec3f motion;

	protected Vec3f delta;

	protected EntityRot rot;

	protected AxisAlignedBB boundingBox;

	protected WorldInterface world;

	protected float stepHeight;

	protected boolean onGround;

	protected boolean isCollidedHorizontally;

	protected boolean isCollidedVertically;

	protected float movementSpeed;

	protected float baseJumpVelocity;

	protected boolean sneaking;

	protected boolean jumping;

	protected boolean sprinting;

	protected boolean touchingWater;

	protected int jumpCoolDown;

	protected float flyingSpeed;

	protected HashMap<BlockGroup, Float> fluidHeights;

	protected float actuallyForwardSpeed;

	protected float actuallySidewaysSpeed;

	protected float sizeWidth;

	protected float sizeHeight;

	protected HashMap<Effect, Integer> effectAmplifiers;

	protected boolean lastSneaking;

	protected boolean lastSneakAdjustX;

	protected boolean lastSneakAdjustZ;

	public MinecraftEntityLiving(WorldInterface world, Vec3f position, float sizeWidth, float sizeHeight) {
		this.position = position;
		this.motion = new Vec3f();
		this.rot = new EntityRot();
		this.sizeWidth = sizeWidth;
		this.sizeHeight = sizeHeight;
		this.recalculateBoundingBox();
		this.world = world;
		this.isCollidedHorizontally = false;
		this.isCollidedVertically = false;
		this.movementSpeed = 0.1f;
		this.stepHeight = 0.6f;
		this.baseJumpVelocity = 0.42f;
		this.onGround = false;
		this.sneaking = false;
		this.jumping = false;
		this.sprinting = false;
		this.lastSneaking = false;
		this.jumpCoolDown = 0;
		this.sidewaysSpeed = 0.0f;
		this.forwardSpeed = 0.0f;
		this.upwardSpeed = 0.0f;
		this.flyingSpeed = 0.02f;
		this.actuallyForwardSpeed = 0.0f;
		this.actuallySidewaysSpeed = 0.0f;
		this.touchingWater = false;
		this.fluidHeights = new HashMap<>();
		this.effectAmplifiers = new HashMap<>();
		this.lastSneakAdjustX = false;
		this.lastSneakAdjustZ = false;
	}


	public boolean isTouchingWater() {
		return touchingWater;
	}

	public HashMap<Effect, Integer> getEffectAmplifiers() {
		return effectAmplifiers;
	}

	protected void recalculateBoundingBox() {
		float w = this.sizeWidth;
		this.boundingBox = new AxisAlignedBB(
				this.position.x - w / 2f,
				this.position.y,
				this.position.z - w / 2f,
				this.position.x + w / 2f,
				this.position.y + this.sizeHeight,
				this.position.z + w / 2f
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

	public Vec3f getPosition() {
		return position;
	}

	public void setPosition(Vec3f position) {
		this.position = position;
		this.recalculateBoundingBox();
	}

	public WorldInterface getWorld() {
		return world;
	}

	public void setWorld(WorldInterface world) {
		this.world = world;
	}

	public Vec3f getMotion() {
		return motion;
	}

	public void setMotion(Vec3f motion) {
		this.motion = motion.copy();
	}

	public AxisAlignedBB getBoundingBox() {
		return boundingBox;
	}

	public void setMotion(float x, float y, float z) {
		this.motion.x = x;
		this.motion.y = y;
		this.motion.z = z;
	}

	public EntityRot getRot() {
		return rot;
	}

	protected boolean updateLiquidState() {
		this.fluidHeights.clear();
		this.checkWaterState();
		// todo: ultrawarm lava speed modification (nether dimension)
		float d = 0.0023335f;
		boolean touchingLava = this.updateMovementInFluid(BlockGroup.LAVA, d);

		return this.touchingWater || touchingLava;
	}

	protected void checkWaterState() {
		this.touchingWater = this.updateMovementInFluid(BlockGroup.WATER, 0.014f);
	}


	public void updateVelocity(float speed, Vec3f movementInput) {
		Vec3f move = EntityPhysics.movementInputToVelocity(movementInput, speed, this.rot.yaw);

		this.motion = this.motion.addVector(move);
	}

	public Vec3f moveWithHeading(Vec3f movementInput, float friction) {
		this.updateVelocity(this.getVelocitySpeed(friction), movementInput);
		this.setMotion(this.applyClimbing(this.getMotion()));
		System.out.println("Internal move diff: " + this.motion.toString());

		Vec3f mv = this.getMotion();

		if (this.sneaking && this.onGround) {
			System.out.println("Normalized diff: " + mv.normalize());
			this.motion = this.adjustMovementForSneaking(mv, EntityPhysics.movementInputToVelocity(new Vec3f(this.actuallySidewaysSpeed, 0f, this.actuallyForwardSpeed), 1f, this.rot.yaw));
		}

		// code #1
		if ((this.isCollidedHorizontally || this.jumping) && this.isClimbing()) {
			motion = new Vec3f(motion.x, 0.2f, motion.z);
		}

		EntityMoveResult result = this.moveEntity(this.motion.x, this.motion.y, this.motion.z);

		Vec3f motion = this.getMotion();

		// code #1: should be here

		return motion;
	}

	protected Vec3f getMotionAffectingPosition() {
		return new Vec3f(this.position.x, this.boundingBox.minY - 0.5000001f, this.position.z);
	}

	public float getSwimHeight() {
		return 0.4f;
	}

	public float getFluidHeight(BlockGroup group) {
		return this.fluidHeights.getOrDefault(group, 0f);
	}

	public void updateMovement() {
		if (this.jumpCoolDown > 0) {
			--this.jumpCoolDown;
		}

		if (Math.abs(this.motion.x) < 0.00000003) {
			this.motion.x = 0.0f;
		}

		if (Math.abs(this.motion.y) < 0.00000003) {
			this.motion.y = 0.0f;
		}

		if (Math.abs(this.motion.z) < 0.00000003) {
			this.motion.z = 0.0f;
		}

		if (false) { //todo: immobile
			this.jumping = false;
			this.sidewaysSpeed = 0.0f;
			this.forwardSpeed = 0.0f;
		}

		this.updateLiquidState(); // fixme: should i put here?

		if (this.jumping) {
			float height = this.touchingWater ? this.fluidHeights.getOrDefault(BlockGroup.WATER, 0f) : this.fluidHeights.getOrDefault(BlockGroup.LAVA, 0f);

			// 1 tick diff
			// bedrockのfluid movementは height 無視してるかも？

			boolean bl = this.touchingWater && height > 0.0f;
			// todo: swimUpward lava
			if (bl && Math.floor(this.position.y) + height > this.boundingBox.minY) {
				this.swimUpward();
			} else if (this.onGround && this.jumpCoolDown == 0) {
				this.jump();
				this.jumpCoolDown = 10;
			}
		} else {
			this.jumpCoolDown = 0;
		}

		this.travel(new Vec3f(this.sidewaysSpeed * 0.98f, this.upwardSpeed, this.forwardSpeed * 0.98f));

		this.lastSneaking = this.sneaking;
	}

	protected float getJumpVelocity() {
		int amplifier = this.effectAmplifiers.getOrDefault(Effect.JUMP_BOOST, -1);
		return this.baseJumpVelocity + (amplifier >= 0 ? 0.1f * (amplifier + 1) : 0f);
	}

	protected void jump() {
		float f = this.getJumpVelocity();

		this.motion.y = f;

		if (this.isSprinting()) {
			float g = this.rot.yaw * ((float) Math.PI / 180);

			this.motion.x += (float) (-Math.sin(g) * 0.2f);
			this.motion.z += (float) (Math.cos(g) * 0.2f);
		}
	}

	protected void swimUpward() {
		this.motion.y += 0.04f;
	}

	public Vec3f applyFluidMovingSpeed(float gravity, boolean falling, Vec3f motion) {
		if (!MathHelper.equals(gravity, 0.0f) && !this.isSprinting()) {
			float d = falling && Math.abs(motion.y - 0.005f) >= 0.003 && Math.abs(motion.y - gravity / 16.0f) < 0.003f ? -0.003f : motion.y - gravity / 16.0f;

			return new Vec3f(motion.x, d, motion.z);
		}

		return motion;
	}

	public void travel(Vec3f movementInput) {
		// todo: support

		float d = 0.08f;
		boolean wantedVertically = this.motion.y <= 0.0f;

		if (this.touchingWater) {
			float e = this.position.y;
			float f = this.isSprinting() ? 0.9f : 0.8f;
			float g = 0.02f;
			// todo: depth strider

			this.updateVelocity(g, movementInput);
			this.moveEntity(this.motion.x, this.motion.y, this.motion.z);

			// climb

			this.motion.x *= f;
			this.motion.y *= 0.8f;
			this.motion.z *= f;

			this.setMotion(this.applyFluidMovingSpeed(d, wantedVertically, this.motion));

			// idk code here (horizontal collision)
		} else {

			WorldBlock block = this.getWorld().getBlock(this.getMotionAffectingPosition());

			float p = block.getFriction();
			float f = this.onGround ? p * 0.91f : 0.91f;

			System.out.println("Block: " + block.getIdentifier() + " p: " + p);

			this.setMotion(this.moveWithHeading(movementInput, p));

			this.motion.x *= f;
			this.motion.y -= d;
			this.motion.y *= 0.98f;
			this.motion.z *= f;
		}

		if (this.isCollidedHorizontally && this.isClimbing()) {
			this.motion.y = 0.2f; // mojang sh*t code?
			// should inside of if(this.touchingWater)
		}
	}

	private Vec3f applyClimbing(Vec3f motion) {
		if (this.isClimbing()) {
			float f = 0.15f;
			float d = motion.x;
			float e = motion.z;
			float g = Math.max(motion.y, -0.2f); // bedrock is -0.2, java is -0.15

			if (g < 0.0f && this.sneaking) {
				g = 0.0f;
			}

			if (g > 0.2f) {
				g = 0.2f; // fixme: patch for climb-jump, not in the original code
			}

			motion = new Vec3f(d, g, e);
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
		this.position.x = (this.boundingBox.minX + this.boundingBox.maxX) / 2f;
		this.position.y = this.boundingBox.minY;
		this.position.z = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2f;
	}

	public Vec3f adjustMovementForSneaking(Vec3f movement, Vec3f movementInput) {
		return movement;
	}

	public boolean updateMovementInFluid(BlockGroup fluidGroup, float speed) {
		AxisAlignedBB bb = this.getBoundingBox().copy().contract(0.001f, 0.001f, 0.001f);
		int i = MathHelper.floor(bb.minX);
		int j = MathHelper.ceil(bb.maxX);
		int k = MathHelper.floor(bb.minY);
		int l = MathHelper.ceil(bb.maxY);
		int m = MathHelper.floor(bb.minZ);
		int n = MathHelper.ceil(bb.maxZ);

		float d = 0.0f;
		int o = 0;
		boolean result = false;

		Vec3f vel = new Vec3f();

		for (int p = i; p < j; ++p) {
			for (int q = k; q < l; ++q) {
				for (int r = m; r < n; ++r) {
					WorldBlock block = this.world.getBlockAt(p, q, r);

					if (!block.isLiquid() || BlockGroup.get(block.getIdentifier()) != fluidGroup) {
						continue;
					}

					float e = q + FluidLogic.getFluidHeight(this.world, block);

					if (e < bb.minY) {
						continue;
					}

					result = true;

					d = Math.max(e - bb.minY, d);

					Vec3f v = FluidLogic.getVelocity(this.world, block.getPosition());

					if (v.length() <= 1e-7f) { // java wasn't ignore still water
						continue;
					}

					vel = vel.addVector(v);
					++o;
				}
			}
		}

		if (vel.length() > 1e-7f) {
			if (o > 0) {
				vel = vel.multiply(1.0f / o);
			}

			vel = vel.normalize().multiply(speed); // java wasn't normalize

			if (Math.abs(vel.x) < 0.003f && Math.abs(vel.z) < 0.003f && vel.length() < 0.0045) {
				vel = vel.normalize().multiply(0.0045f);
			}

			this.setMotion(this.getMotion().addVector(vel));
		}
		this.fluidHeights.put(fluidGroup, d);

		return result;
	}

	public EntityMoveResult moveEntity(float x, float y, float z) {
		float bx = x;
		float by = y;
		float bz = z;

		List<AxisAlignedBB> boxes = this.world.getCollisionBoxes(this.boundingBox.copy().addCoord(x, y, z));
		AxisAlignedBB bb = this.boundingBox.copy();

		System.out.printf("before: minY: %.10f", bb.minY);

		System.out.println("player bb: " + bb);
		System.out.println("collides:");

		for (AxisAlignedBB bbY : boxes) {
			if (Math.abs(y) < 1.0E-7) {
				y = 0.0f;
			}
			System.out.println(bbY.toString());
			y = bbY.calculateYOffset(bb, y);
		}

		System.out.println("offset y : " + y);
		bb.offset(0f, y, 0f);
		boolean wantedVertically = this.onGround || (!MathHelper.equals(by, y) && by < 0.0f);

		for (AxisAlignedBB bbX : boxes) {
			if (Math.abs(x) < 1.0E-7) {
				x = 0.0f;
			}
			x = bbX.calculateXOffset(bb, x);
		}

		System.out.println("offset x : " + x);
		bb.offset(x, 0f, 0f);

		for (AxisAlignedBB bbZ : boxes) {
			if (Math.abs(z) < 1.0E-7) {
				z = 0.0f;
			}
			System.out.println("current z : " + z);
			z = bbZ.calculateZOffset(bb, z);
		}

		System.out.println("offset z : " + z);
		bb.offset(0f, 0f, z);

		if (this.stepHeight > 0.0f && wantedVertically && (!MathHelper.equals(bx, x) || !MathHelper.equals(bz, z))) {
			float cx = x;
			float cy = y;
			float cdy = y;
			float cz = z;
			y = this.stepHeight;

			AxisAlignedBB stepBB = this.boundingBox.copy();

			List<AxisAlignedBB> stepList = this.getWorld().getCollisionBoxes(stepBB.copy().addCoord(x, y, z));

			for (AxisAlignedBB bbY : stepList) {
				if (Math.abs(cy) < 1.0E-7) {
					cy = 0.0f;
				}
				cy = bbY.calculateYOffset(stepBB, cy);
			}

			stepBB.offset(0f, y, 0f);

			for (AxisAlignedBB bbX : stepList) {
				if (Math.abs(cx) < 1.0E-7) {
					cx = 0.0f;
				}
				cx = bbX.calculateXOffset(stepBB, cx);
			}

			stepBB.offset(x, 0f, 0f);

			for (AxisAlignedBB bbZ : stepList) {
				if (Math.abs(cz) < 1.0E-7) {
					cz = 0.0f;
				}
				cz = bbZ.calculateZOffset(stepBB, cz);
			}

			stepBB.offset(0f, 0f, z);

			float reverseY = -y;
			for (AxisAlignedBB bbRY : stepList) {
				if (Math.abs(reverseY) < 1.0E-7) {
					reverseY = 0.0f;
				}
				reverseY = bbRY.calculateYOffset(stepBB, reverseY);
			}

			y += reverseY;

			stepBB.offset(0f, reverseY, 0f);

			System.out.println("Step");

			if ((cx * cx + cz * cz) >= (x * x + z * z)) {
				x = cx;
				y = cy;
				z = cz;
			} else {
				bb = stepBB;
			}
		}

		System.out.println("after bb: " + bb);
		this.boundingBox = bb; // fixme:

		this.resetPositionToBoundingBox();

		this.isCollidedHorizontally = !MathHelper.equals(bx, x) || !MathHelper.equals(bz, z);
		this.isCollidedVertically = !MathHelper.equals(by, y);
		this.onGround = this.isCollidedVertically && by < 0;

		System.out.println("before x: " + bx + " x: " + x);

		if (!MathHelper.equals(bx, x)) {
			this.motion.x = 0f;
		}

		if (!MathHelper.equals(bz, z)) {
			this.motion.z = 0f;
		}

		if (this.isCollidedVertically) {
			this.motion.y = 0f;
		}

		return new EntityMoveResult(
				this.motion,
				!MathHelper.equals(bx, x),
				this.isCollidedVertically,
				!MathHelper.equals(bz, bz)
		);
	}
}
