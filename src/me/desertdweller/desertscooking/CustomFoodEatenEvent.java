package me.desertdweller.desertscooking;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class CustomFoodEatenEvent extends Event implements Cancellable{
	ItemStack item;
	Player player;
	Boolean cancelled = false;
	private static final HandlerList HANDLERS = new HandlerList();

	public CustomFoodEatenEvent(ItemStack item, Player player) {
		this.item = item;
		this.player = player;
	}
	
	public HandlerList getHandlers() {
	    return HANDLERS;
	}

	public static HandlerList getHandlerList() {
	    return HANDLERS;
	}

	public ItemStack getItem() {
		return item;
	}


	public Player getPlayer() {
		return player;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	

	
}
