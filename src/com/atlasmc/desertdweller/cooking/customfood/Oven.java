package com.atlasmc.desertdweller.cooking.customfood;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.atlasmc.desertdweller.cooking.Cooking;
import com.atlasmc.desertdweller.cooking.events.FoodItemFinishedEvent;

import net.md_5.bungee.api.ChatColor;

@Deprecated
public class Oven implements Listener {
	private static Cooking plugin = Cooking.getPlugin(Cooking.class);
	public static final String STATION_TYPE_OVEN = "oven";
	
	public static void ovenInteract(Block b, CustomFoodItem foodItem, Player p) {
		try {
			PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.getConfig().getString("tablename1") + " WHERE location=?");
			statement.setString(1, b.getLocation().toString());
			ResultSet curOven = statement.executeQuery();
			if(foodItem.invalidItem) {
				if(curOven.next()) {
					long time = System.currentTimeMillis() - curOven.getLong(2);
					if(foodItem.item.getType().equals(Material.CLOCK)) {
						p.sendMessage(ChatColor.GREEN + "This oven has been cooking for " + Long.toString(time/60000) + " minutes.");
					}else if(foodItem.item.getType().equals(Material.AIR) && time > plugin.getConfig().getLong("stationTimes")){
						CustomFoodItem result = ovenGrab(b);
						if(result != null) {
							playTakeOutSound(p, b.getLocation());
							FoodItemFinishedEvent event = new FoodItemFinishedEvent(result, p, STATION_TYPE_OVEN);
							Bukkit.getServer().getPluginManager().callEvent(event);
							p.getInventory().addItem(result.getItemStack());
						}
					}else {
						p.sendMessage(ChatColor.RED + "This is a oven. Use this to complete your foods, and add experience to them.");
					}
				}
			}else if(foodItem.completed) {
				p.sendMessage(ChatColor.RED + "This item is already complete!");
			}else if(foodItem.mainIngredients == 1){
				if(!curOven.next()) {
					playInputSound(p, b.getLocation());
					statement = (PreparedStatement) plugin.getConnection().prepareStatement("INSERT INTO " + plugin.getConfig().getString("tablename1") + "(location, timeplaced, customMaterial, food, saturation, experience, mainingredients, secondaryingredients, spices, material, amount, invaliditem, completed, poisoned, ingredients, spicyness, sweetness, bitterness, savoryness, saltyness, sourness, configVersion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					statement.setString(1, b.getLocation().toString());
					statement.setLong(2, System.currentTimeMillis());
					statement.setString(3, foodItem.customMaterial);
					statement.setInt(4, foodItem.food);
					statement.setFloat(5, foodItem.saturation);
					statement.setInt(6, foodItem.experience);
					statement.setInt(7, foodItem.mainIngredients);
					statement.setInt(8, foodItem.secondaryIngredients);
					statement.setInt(9, foodItem.spices);
					statement.setString(10, foodItem.getItemStack().getType().toString());
					statement.setInt(11, foodItem.getItemStack().getAmount());
					statement.setBoolean(12, foodItem.invalidItem);
					statement.setBoolean(13, foodItem.completed);
					statement.setBoolean(14, foodItem.poisoned);
					statement.setString(15, foodItem.ingList);
					statement.setString(22, foodItem.configVersion);
					for(int i = 0; i < foodItem.flavor.flavorList.length; i++) {
						statement.setInt(i+16, foodItem.flavor.flavorList[i]);
					}
					statement.executeUpdate();
					ItemStack changedAmount = p.getInventory().getItemInMainHand();
					changedAmount.setAmount(changedAmount.getAmount()-1);
					if(changedAmount.getAmount() == 0) {
						p.getInventory().setItemInMainHand(null);
					}
					p.getInventory().setItemInMainHand(changedAmount);
				}
			}else {
				p.sendMessage(ChatColor.RED + "The item must have 1 main ingredient added to it.");
			}
		}catch (SQLException e) {
	       	Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error adding item to database!");
	       	e.printStackTrace();
		}
	}

	
	public static CustomFoodItem ovenGrab(Block b) {
		try {
			PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.getConfig().getString("tablename1") + " WHERE location=?");
			statement.setString(1, b.getLocation().toString());
			ResultSet curOven = statement.executeQuery();
			if(curOven.next()) {	
				long time = System.currentTimeMillis() - curOven.getLong(2);
				if(time > 1000){
					CustomFoodItem newItem = new CustomFoodItem(Material.getMaterial(curOven.getString(10)),curOven.getInt(4),curOven.getFloat(5),curOven.getInt(6),curOven.getInt(7),curOven.getInt(8),curOven.getInt(9),true, curOven.getBoolean(14), new Flavor(curOven.getInt(16),curOven.getInt(17),curOven.getInt(18),curOven.getInt(19),curOven.getInt(20),curOven.getInt(21)));
					newItem.getItemStack().setAmount(curOven.getInt(11));
					newItem.invalidItem = curOven.getBoolean(12);
					newItem.completed = true;
					newItem.experience += 500;
					newItem.ingList = curOven.getString(15);
					newItem.customMaterial = curOven.getString(3);
					newItem.configVersion = curOven.getString(22);
					statement = plugin.getConnection().prepareStatement("DELETE FROM " + plugin.getConfig().getString("tablename1") + " WHERE location=?");
					statement.setString(1, b.getLocation().toString());
					statement.executeUpdate();
					newItem = newItem.findTexture("oven");
					return newItem;
				}
				return new CustomFoodItem(new ItemStack(Material.AIR));
			}
		}catch(SQLException e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error grabbing item from table!");
	       	e.printStackTrace();
		}
		return null;
	}
	
	public static Location isOvenShape(Block b) {
		if(b.getType().equals(Material.FURNACE) && b.getRelative(BlockFace.UP).getType().equals(Material.IRON_TRAPDOOR)) {
			if(b.getRelative(BlockFace.DOWN).getType().equals(Material.FIRE) || b.getRelative(BlockFace.DOWN).getType().equals(Material.LAVA)){
				return b.getLocation();
			}
		}
		return null;
	}
	
	public static void playTakeOutSound(Player p, Location l) {
		p.playSound(l, Sound.UI_TOAST_OUT, 5, 1);
	}
	
	public static void playInputSound(Player p, Location l) {
		p.playSound(l, Sound.UI_TOAST_IN, 4, 1);
	}
}
