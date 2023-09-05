package com.lyrica0954.mineleft.network.protocol;

import com.lyrica0954.mineleft.mc.math.Vec3d;
import com.lyrica0954.mineleft.network.protocol.types.PlayerInfo;
import com.lyrica0954.mineleft.utils.ByteBufHelper;
import com.lyrica0954.protocol.PacketBounds;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PacketPlayerLogin extends MineleftPacket {

	public PlayerInfo playerInfo;

	public String worldName;

	public Vec3d position;

	@Override
	public void encode(ByteBuf out) throws Exception {
		ByteBufHelper.writeStandardCharSequence(out, this.playerInfo.getName());
		ByteBufHelper.writeStandardCharSequence(out, this.playerInfo.getUuid().toString());
		ByteBufHelper.writeStandardCharSequence(out, this.worldName);
		ByteBufHelper.writeVec3d(out, this.position);
	}

	@Override
	public void decode(ByteBuf in) throws Exception {
		String name = ByteBufHelper.readStandardCharSequence(in);
		UUID uuid = UUID.fromString(ByteBufHelper.readStandardCharSequence(in));
		this.playerInfo = new PlayerInfo(name, uuid);
		this.worldName = ByteBufHelper.readStandardCharSequence(in);
		this.position = ByteBufHelper.readVec3d(in);
	}

	@Override
	public @NotNull PacketBounds bounds() {
		return PacketBounds.SERVER;
	}

	@Override
	@NotNull ProtocolIds getProtocolId() {
		return ProtocolIds.PLAYER_LOGIN;
	}
}
