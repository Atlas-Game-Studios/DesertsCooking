package com.atlasmc.desertdweller.cooking.listeners;

import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import com.atlasmc.desertdweller.cooking.customfood.Station;

import de.tr7zw.nbtapi.NBTItem;

public class PlayerInteraction implements Listener{


	@EventHandler
	static void onInteract(PlayerInteractEntityEvent e) {
		if(e.getRightClicked() instanceof ItemFrame) {
			ItemFrame frame = (ItemFrame) (e.getRightClicked());
			NBTItem nbti = new NBTItem(frame.getItem());
			if(nbti.getString("Plugin").equals("Cooking")) {
				e.setCancelled(true);
				e.getRightClicked().getWorld().dropItemNaturally(e.getRightClicked().getLocation(), frame.getItem());
				frame.remove();
			}
		}
	}

	@EventHandler
	static void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof ItemFrame) {
			ItemFrame frame = (ItemFrame) (e.getEntity());
			NBTItem nbti = new NBTItem(frame.getItem());
			if(nbti.getString("Plugin").equals("Cooking")) {
				e.setCancelled(true);
				e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), frame.getItem());
				frame.remove();
			}
		}
	}
	
	@EventHandler
	private void checkStations(PlayerInteractEvent e) { // If the player clicks on a station, and has relevant perms, then use station.
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getHand().equals(EquipmentSlot.HAND)) {
			Player p = e.getPlayer();
			
			if(Station.getStationFacing(e.getClickedBlock()) == null)
				return;
			e.setCancelled(true);
			
			Station.stationInteract(e.getClickedBlock(), p);
		}
	}
}
