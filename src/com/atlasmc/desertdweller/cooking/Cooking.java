package com.atlasmc.desertdweller.cooking;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.atlasmc.desertdweller.cooking.customfood.Flavor;
import com.atlasmc.desertdweller.cooking.customfood.FlavorPreference;
import com.atlasmc.desertdweller.cooking.customfood.IngredientStation;
import com.atlasmc.desertdweller.cooking.customfood.Station;
import com.atlasmc.desertdweller.cooking.listeners.FoodCrafting;
import com.atlasmc.desertdweller.cooking.listeners.FoodEating;

public class Cooking extends JavaPlugin implements Listener{
	@SuppressWarnings("unused")
	private BukkitTask furnaceChecker;
	public static HashMap<UUID, FlavorPreference> preferences;
	
	@Override 
	public void onEnable() {
		getCommand("tastes").setExecutor(new Commands());
		getCommand("foodstats").setExecutor(new Commands());
		saveDefaultConfig();
		getConfig();
		preferences = DataHandler.loadPreferences();
		getServer().getPluginManager().registerEvents(new FoodCrafting(),this);
		getServer().getPluginManager().registerEvents(new FoodEating(), this);
		getServer().getPluginManager().registerEvents(new Flavor(), this); 
		getServer().getPluginManager().registerEvents(new IngredientStation(), this); 
		getServer().getPluginManager().registerEvents(new Station(null, 0), this); 
	}
	
	@Override
	public void onDisable() {
		DataHandler.savePreferences(preferences);
	}
}
