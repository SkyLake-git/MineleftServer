package com.lyrica0954.mineleft.utils;

import com.lyrica0954.mineleft.mc.math.Vec3d;
import com.lyrica0954.mineleft.mc.math.Vec3i;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class CodecHelper {

	public static <T> List<T> produceList(ByteBuf buf, ThrowingSupplier<T> supplier) throws Exception {
		int count = buf.readInt();

		List<T> list = new ArrayList<>();

		for (int i = 0; i < count; i++) {
			list.add(supplier.get());
		}

		return list;
	}

	public static float simulateFloatError(float base) {
		ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
		buf.writeFloatLE(base);

		// release buffer?
		return buf.readFloatLE();
	}

	public static <T> void writeOptional(ByteBuf buf, @Nullable T obj, ThrowingConsumer<T> consumer) throws Exception {
		buf.writeBoolean(obj != null);

		if (obj != null) {
			consumer.accept(obj);
		}
	}

	public static <T> T readOptional(ByteBuf buf, ThrowingSupplier<T> supplier) throws Exception {
		if (buf.readBoolean()) {
			return supplier.get();
		}

		return null;
	}

	public static <K, V> AbstractMap<K, V> produceMap(ByteBuf buf, ThrowingSupplier<Map.Entry<K, V>> supplier) throws Exception {
		int count = buf.readInt();

		AbstractMap<K, V> map = new HashMap<>();

		for (int i = 0; i < count; i++) {
			Map.Entry<K, V> entry = supplier.get();
			map.put(entry.getKey(), entry.getValue());
		}

		return map;
	}

	public static <K, V> void consumeMap(ByteBuf buf, Map<K, V> map, ThrowingConsumer<Map.Entry<K, V>> consumer) throws Exception {
		buf.writeInt(map.size());

		for (Map.Entry<K, V> entry : map.entrySet()) {
			consumer.accept(entry);
		}
	}

	public static <T> void consumeList(ByteBuf buf, List<T> list, ThrowingConsumer<T> consumer) throws Exception {
		buf.writeInt(list.size());

		for (T i : list) {
			consumer.accept(i);
		}
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

	public static void writeUUID(ByteBuf buf, UUID uuid) throws Exception {
		writeUTFSequence(buf, uuid.toString());
	}

	public static UUID readUUID(ByteBuf buf) throws Exception {
		return UUID.fromString(readUTFSequence(buf));
	}

	public static void writeUTFSequence(ByteBuf buf, CharSequence charSequence) throws Exception {
		buf.writeInt(charSequence.length()); // unsigned int is better...
		buf.writeCharSequence(charSequence, StandardCharsets.UTF_8);
	}

	public static String readUTFSequence(ByteBuf buf) throws Exception {
		return buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8).toString();
	}
}
