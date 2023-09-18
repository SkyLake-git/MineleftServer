package com.lyrica0954.mineleft.network.protocol;

import com.lyrica0954.mineleft.utils.CodecHelper;
import com.lyrica0954.protocol.PacketBounds;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PacketSetPlayerFlags extends MineleftPacket {

	public UUID playerUuid;

	public long flags;

	@Override
	@NotNull
	public ProtocolIds getProtocolId() {
		return ProtocolIds.SET_PLAYER_FLAGS;
	}

	@Override
	public void encode(ByteBuf out) throws Exception {
		CodecHelper.writeUUID(out, this.playerUuid);
		out.writeLong(this.flags);
	}

	@Override
	public void decode(ByteBuf in) throws Exception {
		this.playerUuid = CodecHelper.readUUID(in);
		this.flags = in.readLong();
	}

	@Override
	public @NotNull PacketBounds bounds() {
		return PacketBounds.SERVER;
	}
}
