package com.lyrica0954.mineleft.mc;

import com.lyrica0954.mineleft.utils.ByteBufHelper;
import io.netty.buffer.ByteBuf;

import java.util.AbstractMap;
import java.util.HashMap;

public class BlockStateData {

	protected AbstractMap<String, Integer> integerMap;

	public BlockStateData() {
		this.integerMap = new HashMap<>();
	}

	public void read(ByteBuf buf) throws Exception {
		this.integerMap = ByteBufHelper.produceMap(buf, () -> new AbstractMap.SimpleEntry<>(ByteBufHelper.readStandardCharSequence(buf), buf.readInt()));
	}

	public void write(ByteBuf buf) throws Exception {
		ByteBufHelper.consumeMap(buf, this.integerMap, (entry) -> {
			ByteBufHelper.writeStandardCharSequence(buf, entry.getKey());
			buf.writeInt(entry.getValue());
		});
	}

	public AbstractMap<String, Integer> getIntegerMap() {
		return integerMap;
	}
}
