package com.lyrica0954.mineleft.network;

public class PacketProcessingError extends Exception {
	public PacketProcessingError() {
	}

	public PacketProcessingError(String message) {
		super(message);
	}

	public PacketProcessingError(String message, Throwable cause) {
		super(message, cause);
	}

	public PacketProcessingError(Throwable cause) {
		super(cause);
	}

	public PacketProcessingError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
