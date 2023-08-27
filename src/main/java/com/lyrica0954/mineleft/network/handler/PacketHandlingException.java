package com.lyrica0954.mineleft.network.handler;

public class PacketHandlingException extends Exception {

	public PacketHandlingException() {
	}

	public PacketHandlingException(String message) {
		super(message);
	}

	public PacketHandlingException(String message, Throwable cause) {
		super(message, cause);
	}

	public PacketHandlingException(Throwable cause) {
		super(cause);
	}

	public PacketHandlingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public static PacketHandlingException wrap(Exception e, String prefix) {
		return new PacketHandlingException(prefix + ": " + e.getMessage(), e);
	}

	public static PacketHandlingException wrap(Exception e) {
		return new PacketHandlingException(e.getMessage(), e);
	}
}
