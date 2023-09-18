package com.lyrica0954.mineleft.network.protocol;

import com.lyrica0954.mineleft.utils.CodecHelper;
import com.lyrica0954.protocol.PacketBounds;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PacketPlayerViolation extends MineleftPacket {

	public UUID playerUuid;

	public String message;

	public int level;

	@Override
	@NotNull
	public ProtocolIds getProtocolId() {
		return ProtocolIds.PLAYER_VIOLATION;
	}

	@Override
	public void encode(ByteBuf out) throws Exception {
		CodecHelper.writeUUID(out, this.playerUuid);
		CodecHelper.writeUTFSequence(out, this.message);
		out.writeInt(this.level);
	}

	@Override
	public void decode(ByteBuf in) throws Exception {
		this.playerUuid = CodecHelper.readUUID(in);
		this.message = CodecHelper.readUTFSequence(in);
		this.level = in.readInt();
	}

	@Override
	public @NotNull PacketBounds bounds() {
		return PacketBounds.CLIENT;
	}
}
