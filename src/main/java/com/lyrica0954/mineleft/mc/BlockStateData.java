package com.lyrica0954.mineleft.mc;

import com.lyrica0954.mineleft.utils.CodecHelper;
import io.netty.buffer.ByteBuf;

import java.util.AbstractMap;
import java.util.HashMap;

public class BlockStateData implements Cloneable {

	protected AbstractMap<String, Integer> integerMap;

	public BlockStateData() {
		this.integerMap = new HashMap<>();
	}

	public void read(ByteBuf buf) throws Exception {
		this.integerMap = CodecHelper.produceMap(buf, () -> new AbstractMap.SimpleEntry<>(CodecHelper.readUTFSequence(buf), buf.readInt()));
	}

	@Override
	protected BlockStateData clone() throws CloneNotSupportedException {
		return (BlockStateData) super.clone();
	}

	public void write(ByteBuf buf) throws Exception {
		CodecHelper.consumeMap(buf, this.integerMap, (entry) -> {
			CodecHelper.writeUTFSequence(buf, entry.getKey());
			buf.writeInt(entry.getValue());
		});
	}

	public AbstractMap<String, Integer> getIntegerMap() {
		return integerMap;
	}
}
