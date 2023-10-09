package com.lyrica0954.mineleft.network.protocol.types;

public class ChunkSendingMethod {

	/**
	 * Send chunk before server started
	 * <p>
	 * Server: High memory usage
	 * <p>
	 * Client: Very low cpu usage (High before server startup)
	 * <p>
	 * Recommended for servers that use fewer chunks, such as PvP servers
	 */
	public static final int PRE = 0;

	/**
	 * Send chunk when chunk loaded
	 * <p>
	 * Server: Medium memory usage
	 * <p>
	 * Client: Medium cpu usage
	 */
	public static final int REALTIME = 1;

	/**
	 * Send nearby blocks in PacketPlayerAuthInput
	 * <p>
	 * Server: Very low memory usage
	 * <p>
	 * Client: High cpu usage
	 */
	public static final int ALTERNATE = 2;
}
