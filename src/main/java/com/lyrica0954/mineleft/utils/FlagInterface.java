package com.lyrica0954.mineleft.utils;

public interface FlagInterface {

	long getFlags();

	void setFlags(long flags);

	default boolean hasFlag(int flag) {
		return (this.getFlags() & (1L << flag)) != 0;
	}

	default void appendFlag(int flag) {
		this.setFlags(this.getFlags() | (1L << flag));
	}

	default void removeFlag(int flag) {
		this.setFlags(this.getFlags() & ~(1L << flag));
	}
}
