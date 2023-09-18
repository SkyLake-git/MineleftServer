package com.lyrica0954.mineleft.network.protocol.types;

public class ChunkSendingMethod {

	/**
	 * Send chunk before server started
	 * <p>
	 * High memory usage
	 */
	public static final int PRE = 0;

	/**
	 * Send chunk when chunk loaded
	 * <p>
	 * Medium memory usage
	 */
	public static final int REALTIME = 1;

	/**
	 * Send nearby blocks in PacketPlayerAuthInput
	 * <p>
	 * Very low memory usage
	 * <p>
	 * Increases client cpu usage
	 */
	public static final int ALTERNATE = 2;
}
