package com.lyrica0954.mineleft.network.player;

public class PlayerFlags {

	public static final int IN_WEB = 1;

	public static final int IN_WATER = 2;

	public static final int IN_LAVA = 3;

	public static final int ON_LADDER = 4;

	protected int flags;

	public PlayerFlags() {
		this.flags = 0;
	}

	public void append(int flag) {
		this.flags |= (1 << flag);
	}

	public boolean has(int flag) {
		return (this.flags & (1 << flag)) != 0;
	}

	public void remove(int flag) {
		this.flags &= ~(1 << flag);
	}

	public void clear() {
		this.flags = 0;
	}
}
