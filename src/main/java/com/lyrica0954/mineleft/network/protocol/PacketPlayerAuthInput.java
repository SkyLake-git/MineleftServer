package com.lyrica0954.mineleft.network.protocol;

import com.lyrica0954.mineleft.mc.level.PaletteBlock;
import com.lyrica0954.mineleft.mc.math.Vec3f;
import com.lyrica0954.mineleft.network.protocol.types.InputData;
import com.lyrica0954.mineleft.utils.CodecHelper;
import com.lyrica0954.protocol.PacketBounds;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Map;
import java.util.UUID;

public class PacketPlayerAuthInput extends MineleftPacket {

	public UUID playerUuid;

	public int frame;

	public InputData inputData;

	public Vec3f requestedPosition;

	public Map<Long, PaletteBlock> nearbyBlocks;

	@Override
	public void encode(ByteBuf out) throws Exception {
		CodecHelper.writeUUID(out, this.playerUuid);
		out.writeInt(this.frame);
		this.inputData.write(out);
		CodecHelper.writeVec3f(out, this.requestedPosition);

		CodecHelper.consumeMap(out, this.nearbyBlocks, (entry) -> {
			out.writeLong(entry.getKey());
			out.writeInt(entry.getValue().getNetworkId());
		});
	}

	@Override
	public void decode(ByteBuf in) throws Exception {
		this.playerUuid = CodecHelper.readUUID(in);
		this.frame = in.readInt();
		InputData data = new InputData();
		data.read(in);

		this.inputData = data;
		this.requestedPosition = CodecHelper.readVec3f(in);

		this.nearbyBlocks = CodecHelper.produceMap(in, () -> new AbstractMap.SimpleEntry<>(in.readLong(), new PaletteBlock(in.readInt())));
	}

	@Override
	public @NotNull PacketBounds bounds() {
		return PacketBounds.SERVER;
	}

	@Override
	@NotNull
	public ProtocolIds getProtocolId() {
		return ProtocolIds.PLAYER_AUTH_INPUT;
	}
}
