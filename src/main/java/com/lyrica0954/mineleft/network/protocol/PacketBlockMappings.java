package com.lyrica0954.mineleft.network.protocol;

import com.lyrica0954.mineleft.mc.Block;
import com.lyrica0954.protocol.PacketBounds;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PacketBlockMappings extends MineleftPacket {

	public HashMap<Integer, Block> mapping;

	public int nullBlockNetworkId;

	@Override
	public void encode(ByteBuf out) throws Exception {
		out.writeInt(this.mapping.size());
		for (Map.Entry<Integer, Block> entry : mapping.entrySet()) {
			entry.getValue().write(out);
		}

		out.writeInt(this.nullBlockNetworkId);
	}

	@Override
	public void decode(ByteBuf in) throws Exception {
		int count = in.readInt();
		this.mapping = new HashMap<>();
		for (int i = 0; i < count; i++) {
			Block block = new Block();
			block.read(in);
			this.mapping.put(block.getNetworkId(), block);
		}

		this.nullBlockNetworkId = in.readInt();
	}

	@Override
	public @NotNull PacketBounds bounds() {
		return PacketBounds.SERVER;
	}

	@Override
	@NotNull
	public ProtocolIds getProtocolId() {
		return ProtocolIds.BLOCK_MAPPINGS;
	}
}
