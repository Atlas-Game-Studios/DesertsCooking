package com.atlasmc.desertdweller.cooking.customfood;


import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import net.md_5.bungee.api.ChatColor;

public class FoodCrafting implements Listener {
	
	@EventHandler
	private void checkStations(PlayerInteractEvent e) { // If the player clicks on a station, and has relevant perms, then use station.
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getHand().equals(EquipmentSlot.HAND)) {
			Player p = e.getPlayer();
			String stationType = Station.getStationType(e.getClickedBlock());
			if(stationType != null) {
				if(stationType == Station.STATION_TYPE_BOILER && !p.hasPermission("cooking.cookingpot")) { //Stops the code if the player does not have permission
					return;
				}else if(stationType == Station.STATION_TYPE_OVEN && !p.hasPermission("cooking.oven")) {
					return;
				}else if(stationType == Station.STATION_TYPE_STOVE && !p.hasPermission("cooking.stove")) {
					return;
				}
				try {
					Station.stationInteract(e.getClickedBlock(), new CustomFoodItem(p.getInventory().getItemInMainHand()),p, stationType);
					e.setCancelled(true);
				} catch (SQLException exc) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error adding item to database!");
					exc.printStackTrace();
				}
			}else if(IngredientStation.isIngredientStation(e.getClickedBlock()) && p.hasPermission("cooking.cuttingboard")) {
				if(e.getClickedBlock().getType().equals(Material.OAK_PRESSURE_PLATE)){
					IngredientStation.openIngredientStation(e.getClickedBlock().getLocation().add(0, -1, 0), p);
				}else {
					IngredientStation.openIngredientStation(e.getClickedBlock().getLocation(), p);
				}
				e.setCancelled(true);
			}else if(e.getClickedBlock().getState() instanceof Furnace && Station.getStationType(e.getClickedBlock().getRelative(BlockFace.UP)) != null ) {
				e.setCancelled(true);
			}else if(e.getClickedBlock().getType().equals(Material.FURNACE)) { //Cancels the event if the click was on a furnace of a cooking station.
				if(e.getClickedBlock().getRelative(BlockFace.UP).getType().equals(Material.CAULDRON) && e.getClickedBlock().getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType().equals(Material.TRIPWIRE_HOOK)) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	private void checkBlockPlace(BlockPlaceEvent e) { //If the item is a placable head, then cancel place event.
		if(!new CustomFoodItem(e.getItemInHand()).invalidItem && new CustomFoodItem(e.getItemInHand()).completed) {
			e.setCancelled(true);
		}
	}
}
