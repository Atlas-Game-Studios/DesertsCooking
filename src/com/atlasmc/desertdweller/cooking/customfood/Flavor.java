package com.atlasmc.desertdweller.cooking.customfood;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.atlasmc.desertdweller.cooking.Cooking;

import net.md_5.bungee.api.ChatColor;

public class Flavor implements Listener{
	private static Cooking plugin = Cooking.getPlugin(Cooking.class);
	public int[] flavorList = new int[6];
	private static final String[] FLAVORS = new String[6];
	
	public Flavor(int spicyness, int sweetness, int bitterness, int savoriness, int saltyness, int sourness) {
		FLAVORS[0] = "spicy";
		FLAVORS[1] = "sweet";
		FLAVORS[2] = "bitter";
		FLAVORS[3] = "savory";
		FLAVORS[4] = "salty";
		FLAVORS[5] = "sour";
		flavorList[0] = spicyness;
		flavorList[1] = sweetness;
		flavorList[2] = bitterness;
		flavorList[3] = savoriness;
		flavorList[4] = saltyness;
		flavorList[5] = sourness;
	}
	
	public Flavor(int[] list) {
		flavorList = list;
		FLAVORS[0] = "spicy";
		FLAVORS[1] = "sweet";
		FLAVORS[2] = "bitter";
		FLAVORS[3] = "savory";
		FLAVORS[4] = "salty";
		FLAVORS[5] = "sour";
	}
	
	public Flavor(String configKey) {
		String[] configFlavors = plugin.getConfig().getString(configKey + ".flavors").split("-");
		flavorList[0] = Integer.parseInt(configFlavors[0]);
		flavorList[1] = Integer.parseInt(configFlavors[1]);
		flavorList[2] = Integer.parseInt(configFlavors[2]);
		flavorList[3] = Integer.parseInt(configFlavors[3]);
		flavorList[4] = Integer.parseInt(configFlavors[4]);
		flavorList[5] = Integer.parseInt(configFlavors[5]);
		FLAVORS[0] = "spicy";
		FLAVORS[1] = "sweet";
		FLAVORS[2] = "bitter";
		FLAVORS[3] = "savory";
		FLAVORS[4] = "salty";
		FLAVORS[5] = "sour";
	}
	
	public Flavor() {
		for(int i = 0; i < 6; i++) {
			flavorList[i] = 0;
		}
		FLAVORS[0] = "spicy";
		FLAVORS[1] = "sweet";
		FLAVORS[2] = "bitter";
		FLAVORS[3] = "savory";
		FLAVORS[4] = "salty";
		FLAVORS[5] = "sour";
	}
	
	public int getFlavor(int i) {
		if(i < flavorList.length)
			return flavorList[i];
		return 0; 
	}
	
	public void addFlavor(Flavor addedFlavor) {
		flavorList[0] += addedFlavor.getFlavor(0);
		flavorList[1] += addedFlavor.getFlavor(1);
		flavorList[2] += addedFlavor.getFlavor(2);
		flavorList[3] += addedFlavor.getFlavor(3);
		flavorList[4] += addedFlavor.getFlavor(4);
		flavorList[5] += addedFlavor.getFlavor(5);
	}
	
	public float efficiency(FlavorPreference pref) {
		Float satisfaction = (float) 0.4;
		//Compares player prefs, to food tastes
		int pref1 = pref.getFlavor1();   
		int pref2 = pref.getFlavor2();
		int strongestTaste = 0;
		int[] strongestFlavorIds = new int[2];
		//finds strongest flavor
		for(int i = 0; i < 6; i++) {
			if(flavorList[i] > strongestTaste) {
				strongestTaste = flavorList[i];
				strongestFlavorIds[0] = i;
			}
		}
		//finds second strongest flavor
		strongestTaste = 0;
		for(int i = 0; i < 6; i++) {
			if(i != strongestFlavorIds[0] && flavorList[i] > strongestTaste) {
				strongestTaste = flavorList[i];
				strongestFlavorIds[1] = i;
			}
		}
		//If the main preference is one of the 2 strongest flavors add 40% efficiency
		if(pref1 == strongestFlavorIds[0] || pref1 == strongestFlavorIds[1]) {
			satisfaction += (float) 0.4;
		}
		//if the second preference is one of the 2 strongest flavors add 20% efficiency
		if(pref2 == strongestFlavorIds[0] || pref2 == strongestFlavorIds[1]) {
			satisfaction += (float) 0.2;
		}
		return satisfaction;
	}
	
	public void eatenPlayerMessage(Player p) {
		int pref1 = Cooking.preferences.get(p.getUniqueId()).getFlavor1();
		int pref2 = Cooking.preferences.get(p.getUniqueId()).getFlavor2(); 
		int strongestTaste = 0;
		int[] strongestFlavorIds = new int[2];
		for(int i = 0; i < 6; i++) {
			if(flavorList[i] > strongestTaste) {
				strongestTaste = flavorList[i];
				strongestFlavorIds[0] = i;
			}
		}
		strongestTaste = 0;
		for(int i = 0; i < 6; i++) {
			if(i != strongestFlavorIds[0] && flavorList[i] > strongestTaste) {
				strongestTaste = flavorList[i];
				strongestFlavorIds[1] = i;
			}
		}
		p.sendMessage(ChatColor.GRAY + "The item you just ate tasted " + ChatColor.GOLD + FLAVORS[strongestFlavorIds[0]] + ChatColor.GRAY + " and " + ChatColor.GOLD + FLAVORS[strongestFlavorIds[1]] + ChatColor.GRAY + ".");
		if(pref1 == strongestFlavorIds[0] || pref1 == strongestFlavorIds[1]) {
			if(pref2 == strongestFlavorIds[0] || pref2 == strongestFlavorIds[1]) {
				p.sendMessage(ChatColor.GRAY + "This matches both of your flavor preferences, giving it 100% effectiveness!");
			}else {
				p.sendMessage(ChatColor.GRAY + "This matches only your main flavor preference, giving it only 80% effectiveness.");
			}
		}else {
			if(pref2 == strongestFlavorIds[0] || pref2 == strongestFlavorIds[1]) {
				p.sendMessage(ChatColor.GRAY + "This matches only your secondary flavor preference, giving it only 60% effectiveness.");
			}else {
				p.sendMessage(ChatColor.GRAY + "This matches none of your flavor preferences, giving it 40% effectiveness!");
			}
		}
	}
	
	public String playerPreferencesMessage(Player p) {
		int pref1 = Cooking.preferences.get(p.getUniqueId()).getFlavor1();   
		int pref2 = Cooking.preferences.get(p.getUniqueId()).getFlavor2();   
		return ChatColor.GOLD + "Your main preference is for things to taste " + ChatColor.DARK_AQUA + FLAVORS[pref1] + ChatColor.GOLD + ", and your second preference is for things to taste " + ChatColor.DARK_AQUA + FLAVORS[pref2] + ChatColor.GOLD + ".";
	}

	@EventHandler
	private void generatePrefs(PlayerJoinEvent e) {
		Player p = e.getPlayer();
			if(!Cooking.preferences.keySet().contains(p.getUniqueId())) {
				int pref1 = randomIntInRange(0,5);
				int pref2 = pref1;
				while(pref2 == pref1) {
					pref2 = randomIntInRange(0,5);
				}
				Cooking.preferences.put(p.getUniqueId(), new FlavorPreference(pref1, pref2));
			}
	}
	
	private int randomIntInRange(int min, int max) {
		Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
	}
}
