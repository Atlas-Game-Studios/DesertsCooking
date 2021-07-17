package com.atlasmc.desertdweller.cooking.effects;

import org.bukkit.scheduler.BukkitRunnable;

public class EffectTicker extends BukkitRunnable{

	@Override
	public void run() {
		EffectsHandler.tickAllEffects();
	}

}
