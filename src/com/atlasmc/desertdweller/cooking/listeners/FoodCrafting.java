package com.atlasmc.desertdweller.cooking.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import com.atlasmc.desertdweller.cooking.customfood.Station;

public class FoodCrafting implements Listener {
	
	@EventHandler
	private void checkStations(PlayerInteractEvent e) { // If the player clicks on a station, and has relevant perms, then use station.
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getHand().equals(EquipmentSlot.HAND)) {
			Player p = e.getPlayer();
			
			if(Station.getStationFacing(e.getClickedBlock()) == null)
				return;
			e.setCancelled(true);
			
			Station.stationInteract(e.getClickedBlock(), e.getPlayer());
		}
	}
}
