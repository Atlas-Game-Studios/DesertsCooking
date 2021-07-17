package com.atlasmc.desertdweller.cooking;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.ags.atlasessentials.api.AtlasEssentialsService;
import com.atlasmc.desertdweller.cooking.customfood.Flavor;
import com.atlasmc.desertdweller.cooking.customfood.FlavorPreference;
import com.atlasmc.desertdweller.cooking.customfood.Station;
import com.atlasmc.desertdweller.cooking.customfood.StationGUI;
import com.atlasmc.desertdweller.cooking.effects.BattleAxeDamageBoost;
import com.atlasmc.desertdweller.cooking.effects.BowDamageBoost;
import com.atlasmc.desertdweller.cooking.effects.CrossBowDamageBoost;
import com.atlasmc.desertdweller.cooking.effects.EffectTicker;
import com.atlasmc.desertdweller.cooking.effects.MeleeDamageBoost;
import com.atlasmc.desertdweller.cooking.effects.SwordDamageBoost;
import com.atlasmc.desertdweller.cooking.effects.TridentDamageBoost;
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
		getServer().getPluginManager().registerEvents(new BattleAxeDamageBoost(1, null), this);
		getServer().getPluginManager().registerEvents(new CrossBowDamageBoost(1, null), this);
		getServer().getPluginManager().registerEvents(new BowDamageBoost(1, null), this);
		getServer().getPluginManager().registerEvents(new MeleeDamageBoost(1, null), this);
		getServer().getPluginManager().registerEvents(new SwordDamageBoost(1, null), this);
		getServer().getPluginManager().registerEvents(new TridentDamageBoost(1, null), this);
		new EffectTicker().runTaskTimer(this, 1, 1);
	}
	
	@Override
	public void onDisable() {
		DataHandler.savePreferences(preferences);
	}
	
	public static AtlasEssentialsService atlasEssentials() {
        return Bukkit.getServer().getServicesManager().load(AtlasEssentialsService.class);
    }
}
