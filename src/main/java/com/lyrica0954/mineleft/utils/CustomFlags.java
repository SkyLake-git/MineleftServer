package com.lyrica0954.mineleft.utils;

public class CustomFlags implements FlagInterface {

	private long flags;

	public CustomFlags(long flags) {
		this.flags = flags;
	}


	public CustomFlags copy() {
		return new CustomFlags(this.flags);
	}

	@Override
	public long getFlags() {
		return flags;
	}

	@Override
	public void setFlags(long flags) {
		this.flags = flags;
	}
}
