package com.lyrica0954.mineleft.utils;

import com.lyrica0954.mineleft.mc.math.Vec3d;
import com.lyrica0954.mineleft.mc.math.Vec3i;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ByteBufHelper {

	public static <T> List<T> produceList(ByteBuf buf, ThrowingSupplier<T> supplier) throws Exception {
		int count = buf.readInt();

		List<T> list = new ArrayList<>();

		for (int i = 0; i < count; i++) {
			list.add(supplier.get());
		}

		return list;
	}

	public static <T> List<T> consumeList(ByteBuf buf, List<T> list, ThrowingConsumer<T> consumer) throws Exception {
		buf.writeInt(list.size());

		for (T i : list) {
			consumer.accept(i);
		}
		return list;
	}

	public static Vec3d readVec3d(ByteBuf buf) throws Exception {
		return new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}

	public static Vec3i readVec3i(ByteBuf buf) throws Exception {
		return new Vec3i(buf.readInt(), buf.readInt(), buf.readInt());
	}

	public static void writeVec3d(ByteBuf buf, Vec3d vec) throws Exception {
		buf.writeDouble(vec.x);
		buf.writeDouble(vec.y);
		buf.writeDouble(vec.z);
	}

	public static void writeVec3i(ByteBuf buf, Vec3i vec) throws Exception {
		buf.writeInt(vec.x);
		buf.writeInt(vec.y);
		buf.writeInt(vec.z);
	}

	public static void writeStandardCharSequence(ByteBuf buf, CharSequence charSequence) throws Exception {
		buf.writeCharSequence(charSequence, StandardCharsets.UTF_8);
	}

	public static String readStandardCharSequence(ByteBuf buf) throws Exception {
		return buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8).toString();
	}
}
