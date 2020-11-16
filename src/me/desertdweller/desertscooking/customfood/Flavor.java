package me.desertdweller.desertscooking.customfood;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.desertdweller.desertscooking.Main;
import net.md_5.bungee.api.ChatColor;

public class Flavor implements Listener{
	private static Main plugin = Main.getPlugin(Main.class);
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
	
	public float efficiency(Player p) {
		try {
			//Grabs player pref data
			PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.getConfig().getString("tablename2") + " WHERE uuid=?");
			statement.setString(1, p.getUniqueId().toString());
			ResultSet playerData = statement.executeQuery();
			Float satisfaction = (float) 0.4;
			if(playerData.next()) {
				//Compares player prefs, to food tastes
				int pref1 = playerData.getInt(2);   
				int pref2 = playerData.getInt(3); 
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
			}
			return satisfaction;
		}catch(SQLException e){
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error getting player preferences from MySQL table!");
	       	e.printStackTrace();
		}
		return 0;
	}
	
	public void eatenPlayerMessage(Player p) {
		try {
			PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.getConfig().getString("tablename2") + " WHERE uuid=?");
			statement.setString(1, p.getUniqueId().toString());
			ResultSet playerData = statement.executeQuery();
			if(playerData.next()) {
				int pref1 = playerData.getInt(2);   
				int pref2 = playerData.getInt(3); 
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
		}catch(SQLException e){
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error getting player preferences from MySQL table!");
	       	e.printStackTrace();
		}
	}
	
	public String playerPreferencesMessage(Player p) {
		String output = "";
		try {
			PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.getConfig().getString("tablename2") + " WHERE uuid=?");
			statement.setString(1, p.getUniqueId().toString());
			ResultSet playerData = statement.executeQuery();
			if(playerData.next()) {
				int pref1 = playerData.getInt(2);   
				int pref2 = playerData.getInt(3); 
				return ChatColor.GOLD + "Your main preference is for things to taste " + ChatColor.DARK_AQUA + FLAVORS[pref1] + ChatColor.GOLD + ", and your second preference is for things to taste " + ChatColor.DARK_AQUA + FLAVORS[pref2] + ChatColor.GOLD + ".";
			}else {
				System.out.println("Did not find player asking for command!");
			}
		}catch(SQLException e){
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error getting player preferences from MySQL table!");
	       	e.printStackTrace();
		}
		return output;
	}

	@EventHandler
	private void generatePrefs(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		try {
			PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.getConfig().getString("tablename2") + " WHERE uuid=?");
			statement.setString(1, p.getUniqueId().toString());
			ResultSet playerData = statement.executeQuery();
			if(!playerData.next()) {
				int pref1 = randomIntInRange(0,5);
				int pref2 = pref1;
				while(pref2 == pref1) {
					pref2 = randomIntInRange(0,5);
				}
				statement = plugin.getConnection().prepareStatement("INSERT INTO " + plugin.getConfig().getString("tablename2") + " (uuid,preference1,preference2) VALUES (?,?,?)");
				statement.setString(1, p.getUniqueId().toString());
				statement.setInt(2, pref1);
				statement.setInt(3, pref2);
				statement.executeUpdate();
			}
		}catch(SQLException ex) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error getting player preferences from MySQL table!");
	       	ex.printStackTrace();
		}
	}
	
	private int randomIntInRange(int min, int max) {
		Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
	}
}
