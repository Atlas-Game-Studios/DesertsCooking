package com.atlasmc.desertdweller.cooking;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.atlasmc.desertdweller.cooking.customfood.FlavorPreference;

public class DataHandler {
	private static Cooking plugin = Cooking.getPlugin(Cooking.class); 
	private static File dataFile;
	private static FileConfiguration dataConfig;

	public static HashMap<UUID, FlavorPreference> loadPreferences(){
		HashMap<UUID, FlavorPreference> loadedPreferences = new HashMap<UUID, FlavorPreference>();
		
		dataFile = new File(plugin.getDataFolder(), "preferences.yml");
		if (!dataFile.exists()) {
			dataFile.getParentFile().mkdirs();
			plugin.saveResource("preferences.yml", false);
		}
		dataConfig = new YamlConfiguration();
		try {
			dataConfig.load(dataFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		for(String uuid : dataConfig.getKeys(false)) {
			loadedPreferences.put(UUID.fromString(uuid), new FlavorPreference(dataConfig.getInt(uuid + ".flavor1"), dataConfig.getInt(uuid + ".flavor2")));
		}
		return loadedPreferences;
	}
	
	public static void savePreferences(HashMap<UUID, FlavorPreference> preferences) {
		dataFile = new File(plugin.getDataFolder(), "preferences.yml");
		dataFile.delete();
		if (!dataFile.exists()) {
			dataFile.getParentFile().mkdirs();
			if(plugin.getResource("preferences.yml") != null)
				plugin.saveResource("preferences.yml", false);
		}
		dataConfig = new YamlConfiguration();
		try {
			dataConfig.load(dataFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		
		for(UUID uuid : preferences.keySet()) {
			dataConfig.createSection(uuid.toString());
			dataConfig.set(uuid.toString() + ".flavor1", preferences.get(uuid).getFlavor1());
			dataConfig.set(uuid.toString() + ".flavor2", preferences.get(uuid).getFlavor2());
		}
	}
	
}
