package me.desertdweller.desertscooking;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.itemnbtapi.NBTItem;

public class CustomItems {
	
	public static CustomFoodItem getmcdb() {
		CustomFoodItem mcdonaldsBurger = new CustomFoodItem(Material.BREAD, 10, 1, 1000, 1, 3, 4, true, true, new Flavor(0,0,2,4,0,0));
		ItemStack newStack = CustomFoodItem.buildHead("187ab05d-1d27-450b-bea8-a723fd1d3b4a", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZiNDhlMmI5NjljNGMxYjg2YzVmODJhMmUyMzc5OWY0YTZmMzFjZTAwOWE1ZjkyYjM5ZjViMjUwNTdiMmRkMCJ9fX0=");
		mcdonaldsBurger.prevItem = newStack;
		return mcdonaldsBurger;
	}
	
	public static CustomFoodItem getCustomFood(String name) {
		switch (name.toLowerCase()) {
		case ("salt") : return createItem("Salt", Material.SUGAR);
		case ("butter") : return createItem("Butter", Material.GOLD_INGOT);
		case ("vinegar") : return createItem("Vinegar", Material.EXPERIENCE_BOTTLE);
		case ("oliveoil") : return createItem("OliveOil", Material.EXPERIENCE_BOTTLE);
		case ("pasta") : return createItem("Pasta", Material.WHEAT);
		case ("broth") : return createItem("Broth", Material.MUSHROOM_STEW);
		case ("flour") : return createItem("Flour", Material.SUGAR);
		case ("hotsauce") : return createItem("HotSauce", Material.BEETROOT_SOUP);
		case ("cheese") : return createItem("Cheese", Material.GOLD_INGOT);
		}
		return null;
	}
	
	private static CustomFoodItem createItem(String material, Material type) {
		ItemStack temp = new ItemStack(type);
		NBTItem nbtitem = new NBTItem(temp);
		nbtitem.setString("Material", material);
		temp = nbtitem.getItem();
		return new CustomFoodItem(temp);
	}
}
