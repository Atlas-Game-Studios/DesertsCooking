package com.atlasmc.desertdweller.cooking;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.atlasmc.desertdweller.cooking.customfood.Flavor;
import com.atlasmc.desertdweller.cooking.customfood.FlavorPreference;
import com.atlasmc.desertdweller.cooking.customfood.Station;
import com.atlasmc.desertdweller.cooking.customfood.StationGUI;
import com.atlasmc.desertdweller.cooking.effects.EffectTicker;
import com.atlasmc.desertdweller.cooking.listeners.FoodEating;
import com.atlasmc.desertdweller.cooking.listeners.PlayerInteraction;

public class Cooking extends JavaPlugin implements Listener{
	@SuppressWarnings("unused")
	private BukkitTask furnaceChecker;
	public static HashMap<UUID, FlavorPreference> preferences;
	
	@Override 
	public void onEnable() {
		saveDefaultConfig();
		getConfig();
		getCommand("tastes").setExecutor(new Commands());
		getCommand("foodstats").setExecutor(new Commands());
		saveDefaultConfig();
		getConfig();
		preferences = DataHandler.loadPreferences();
		getServer().getPluginManager().registerEvents(new FoodEating(), this);
		getServer().getPluginManager().registerEvents(new Flavor(), this); 
		getServer().getPluginManager().registerEvents(new StationGUI(), this); 
		getServer().getPluginManager().registerEvents(new PlayerInteraction(), this); 
		getServer().getPluginManager().registerEvents(new Station(null, 0), this);
		new EffectTicker().runTaskTimer(this, 1, 1);
	}
	
	@Override
	public void onDisable() {
		DataHandler.savePreferences(preferences);
	}
}
