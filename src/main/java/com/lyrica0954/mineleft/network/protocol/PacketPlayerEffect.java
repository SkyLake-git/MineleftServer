package com.lyrica0954.mineleft.network.protocol;

import com.lyrica0954.mineleft.network.PacketProcessingError;
import com.lyrica0954.mineleft.network.protocol.types.Effect;
import com.lyrica0954.mineleft.utils.CodecHelper;
import com.lyrica0954.protocol.PacketBounds;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PacketPlayerEffect extends MineleftPacket {

	public static final int MODE_ADD = 0;

	public static final int MODE_MODIFY = 1;

	public static final int MODE_REMOVE = 2;

	public Effect effect;

	public int amplifier;

	public UUID playerUuid;

	public int mode;

	@Override
	public @NotNull ProtocolIds getProtocolId() {
		return ProtocolIds.PLAYER_EFFECT;
	}

	@Override
	public void encode(ByteBuf out) throws Exception {
		CodecHelper.writeUUID(out, this.playerUuid);
		out.writeInt(this.effect.getEffectId());
		out.writeInt(this.amplifier);
		out.writeInt(this.mode);
	}

	@Override
	public void decode(ByteBuf in) throws Exception {
		this.playerUuid = CodecHelper.readUUID(in);
		int effectId = in.readInt();
		this.effect = Effect.idOf(effectId);
		if (this.effect == null) {
			throw new PacketProcessingError("Effect id \"%d\" not found".formatted(effectId));
		}
		this.amplifier = in.readInt();
		this.mode = in.readInt();
	}

	@Override
	public @NotNull PacketBounds bounds() {
		return PacketBounds.SERVER;
	}
}
