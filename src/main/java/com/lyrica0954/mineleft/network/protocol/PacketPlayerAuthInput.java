package com.lyrica0954.mineleft.network.protocol;

import com.lyrica0954.mineleft.net.PacketBounds;
import com.lyrica0954.mineleft.network.protocol.types.InputData;
import com.lyrica0954.mineleft.utils.ByteBufHelper;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PacketPlayerAuthInput extends MineleftPacket {

	public UUID playerUuid;

	public InputData inputData;

	@Override
	public void encode(ByteBuf out) throws Exception {
		ByteBufHelper.writeStandardCharSequence(out, this.playerUuid.toString());
		this.inputData.write(out);
	}

	@Override
	public void decode(ByteBuf in) throws Exception {
		this.playerUuid = UUID.fromString(ByteBufHelper.readStandardCharSequence(in));
		InputData data = new InputData();
		data.read(in);

		this.inputData = data;
	}

	@Override
	public @NotNull PacketBounds bounds() {
		return PacketBounds.SERVER;
	}

	@Override
	@NotNull ProtocolIds getProtocolId() {
		return ProtocolIds.PLAYER_AUTH_INPUT;
	}
}
