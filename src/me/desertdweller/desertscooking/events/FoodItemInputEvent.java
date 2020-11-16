package me.desertdweller.desertscooking.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.desertdweller.desertscooking.customfood.CustomFoodItem;

public class FoodItemInputEvent extends Event{
	private CustomFoodItem foodItem;
	private Player player;
	private String stationType;
	private static final HandlerList handlers = new HandlerList();
	private Block block;

	public FoodItemInputEvent(CustomFoodItem item, Player p, String station, Block b) {
		foodItem = item;
		player = p;
		stationType = station;
		block = b;
	}

	public Block getBlock() {
		return block;
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
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
