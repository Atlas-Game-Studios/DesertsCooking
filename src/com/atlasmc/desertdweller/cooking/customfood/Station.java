package com.atlasmc.desertdweller.cooking.customfood;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.atlasmc.desertdweller.cooking.Cooking;

public class Station implements Listener {
	private static Cooking plugin = Cooking.getPlugin(Cooking.class);
	private static HashMap<Location, Station> activeStations = new HashMap<Location, Station>();
	
	private CustomFoodItem cookingItem;
	private long timeBegan;
	
	
	public Station(CustomFoodItem cookingItem, long timeBegan) {
		this.cookingItem = cookingItem;
		this.timeBegan = timeBegan;
	}
	
	public CustomFoodItem getCookingItem() {
		return cookingItem;
	}
	
	public long getTimeBegan() {
		return timeBegan;
	}
	
	public static void startNewStation(Location l, Station s) {
		activeStations.put(l, s);
		new StationUpdater(l, StationStage.IDLE).runTaskLater(plugin, 20);
	}
	
	public static void loadStation(Location l, Station s) {
		activeStations.put(l, s);
	}
	
	public static void stationInteract(Block b, Player p) {
		if(!activeStations.keySet().contains(b.getLocation())) {
			StationGUI.openIngredientGUI(b.getLocation(), p);
		}
	}
	
	public static void removeStation(Location l) {
		activeStations.remove(l);
	}
	
	public static Station getActiveStation(Location l) {
		return activeStations.get(l);
	}
	
	public static BlockFace getStationFacing(Block b) {
		if(b.getType().equals(Material.SMOKER)) {
			if(!b.getRelative(BlockFace.UP).getType().equals(Material.COBBLESTONE_WALL) || !b.getRelative(BlockFace.DOWN).getType().equals(Material.CAMPFIRE)) {
				System.out.println("First");
				return null;
			}
			if(b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON)) {
				if(b.getRelative(BlockFace.WEST).getType().equals(Material.TRIPWIRE_HOOK) && b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
					return BlockFace.SOUTH;
				}
			}else if(b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON)) {
				if(b.getRelative(BlockFace.NORTH).getType().equals(Material.TRIPWIRE_HOOK) && b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
					return BlockFace.WEST;
				}
			}else if(b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON)) {
				if(b.getRelative(BlockFace.EAST).getType().equals(Material.TRIPWIRE_HOOK) && b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
					return BlockFace.NORTH;
				}
			}else if(b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON)) {
				if(b.getRelative(BlockFace.SOUTH).getType().equals(Material.TRIPWIRE_HOOK) && b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
					return BlockFace.EAST;
				}
			}
		}
		return null;
	}
	
	public static String serializeLocation(Location l) {
		return new String(l.getWorld().getName() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ());
	}
	
	public static Location deserializeLocation(String s) {
		String[] stringArray = s.split(",");
		return new Location(Bukkit.getWorld(stringArray[0]), Double.parseDouble(stringArray[1]), Double.parseDouble(stringArray[2]), Double.parseDouble(stringArray[3]));
	}
}
