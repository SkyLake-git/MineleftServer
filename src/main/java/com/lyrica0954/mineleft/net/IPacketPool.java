package com.lyrica0954.mineleft.net;

import org.jetbrains.annotations.Nullable;

public interface IPacketPool {

	@Nullable Packet get(short id);
}
