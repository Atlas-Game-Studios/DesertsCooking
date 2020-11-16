package me.desertdweller.desertscooking.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.desertdweller.desertscooking.customfood.CustomFoodItem;

public class FoodItemFinishedEvent extends Event implements Cancellable{
	private CustomFoodItem foodItem;
	private Player player;
	private String stationType;
	private static final HandlerList HANDLERS = new HandlerList();
	private boolean cancelled;

	public FoodItemFinishedEvent(CustomFoodItem item, Player p, String station) {
		foodItem = item;
		player = p;
		stationType = station;
	}

	public CustomFoodItem getFoodItem() {
		return foodItem;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public String getStationType() {
		return stationType;
	}

	public HandlerList getHandlers() {
	    return HANDLERS;
	}

	public static HandlerList getHandlerList() {
	    return HANDLERS;
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
