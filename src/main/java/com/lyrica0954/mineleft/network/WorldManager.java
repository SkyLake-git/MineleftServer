package com.lyrica0954.mineleft.network;

import com.lyrica0954.mineleft.mc.level.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class WorldManager {

	protected HashMap<String, World> worlds;

	protected @Nullable String defaultWorldName;

	public WorldManager() {
		this.worlds = new HashMap<>();
		this.defaultWorldName = null;
	}

	public @Nullable World getDefaultWorld() {
		return this.defaultWorldName != null ? this.getWorld(this.defaultWorldName) : null;
	}

	public @Nullable String getDefaultWorldName() {
		return defaultWorldName;
	}

	public void setDefaultWorldName(@Nullable String name) {
		this.defaultWorldName = name;
	}

	public void addWorld(String name, World world) {
		this.worlds.put(name, world);
	}

	public @Nullable World getWorld(String name) {
		return this.worlds.get(name);
	}

	public boolean existsWorld(String name) {
		return this.worlds.containsKey(name);
	}
}
