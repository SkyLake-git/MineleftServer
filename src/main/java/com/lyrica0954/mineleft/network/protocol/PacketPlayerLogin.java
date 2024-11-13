package com.lyrica0954.mineleft.network.protocol;

import com.lyrica0954.mineleft.mc.math.Vec3f;
import com.lyrica0954.mineleft.network.protocol.types.PlayerInfo;
import com.lyrica0954.mineleft.utils.CodecHelper;
import com.lyrica0954.protocol.PacketBounds;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PacketPlayerLogin extends MineleftPacket {

	public PlayerInfo playerInfo;

	public String worldName;

	public Vec3f position;

	@Override
	public void encode(ByteBuf out) throws Exception {
		CodecHelper.writeUTFSequence(out, this.playerInfo.getName());
		CodecHelper.writeUUID(out, this.playerInfo.getUuid());
		CodecHelper.writeUTFSequence(out, this.worldName);
		CodecHelper.writeVec3f(out, this.position);
	}

	@Override
	public void decode(ByteBuf in) throws Exception {
		String name = CodecHelper.readUTFSequence(in);
		UUID uuid = CodecHelper.readUUID(in);
		this.playerInfo = new PlayerInfo(name, uuid);
		this.worldName = CodecHelper.readUTFSequence(in);
		this.position = CodecHelper.readVec3f(in);
	}

	@Override
	public @NotNull PacketBounds bounds() {
		return PacketBounds.SERVER;
	}

	@Override
	@NotNull
	public ProtocolIds getProtocolId() {
		return ProtocolIds.PLAYER_LOGIN;
	}
}
