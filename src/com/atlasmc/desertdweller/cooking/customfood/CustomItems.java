package com.atlasmc.desertdweller.cooking.customfood;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;


public class CustomItems {
	
	private static CustomFoodItem createItem(String material, Material type) {
		ItemStack temp = new ItemStack(type);
		NBTItem nbtitem = new NBTItem(temp);
		nbtitem.setString("Material", material);
		temp = nbtitem.getItem();
		return new CustomFoodItem(temp);
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
}
