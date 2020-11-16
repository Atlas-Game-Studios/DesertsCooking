package me.desertdweller.desertscooking.customfood;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.desertdweller.desertscooking.Main;
import me.desertdweller.desertscooking.events.FoodItemFinishedEvent;
import net.md_5.bungee.api.ChatColor;

@Deprecated
public class Boiler implements Listener {
	private static Main plugin = Main.getPlugin(Main.class);
	public static final String STATION_TYPE_BOILER = "boiler";
	
	public static void boilerInteract(Block b, CustomFoodItem foodItem, Player p) {
		try {
			PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.getConfig().getString("tablename1") + " WHERE location=?");
			statement.setString(1, b.getLocation().toString());
			ResultSet curBoiler = statement.executeQuery();
			if(foodItem.invalidItem) {
				if(curBoiler.next()) {
					long time = System.currentTimeMillis() - curBoiler.getLong(2);
					if(foodItem.item.getType().equals(Material.CLOCK)) {
						p.sendMessage(ChatColor.GREEN + "This boiler has been cooking for " + Long.toString(time/60000) + " minutes.");
					}else if(foodItem.item.getType().equals(Material.AIR) && time > plugin.getConfig().getLong("stationTimes")){
						CustomFoodItem result = boilerGrab(b);
						if(result != null) {
							playTakeOutSound(p, b.getLocation());
							FoodItemFinishedEvent event = new FoodItemFinishedEvent(result, p, STATION_TYPE_BOILER);
							Bukkit.getServer().getPluginManager().callEvent(event);
							p.getInventory().addItem(result.getItemStack());
						}
					}else {
						p.sendMessage(ChatColor.RED + "This is a boiler. Use this to complete your foods, and add saturation to them.");
					}
				}
			}else if(foodItem.completed) {
				p.sendMessage(ChatColor.RED + "This item is already complete!");
			}else if(foodItem.mainIngredients == 1){
				if(!curBoiler.next()) {
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

	
	public static CustomFoodItem boilerGrab(Block b) {
		try {
			PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.getConfig().getString("tablename1") + " WHERE location=?");
			statement.setString(1, b.getLocation().toString());
			ResultSet curBoiler = statement.executeQuery();
			if(curBoiler.next()) {	
				long time = System.currentTimeMillis() - curBoiler.getLong(2);
				if(time > 1000){
					CustomFoodItem newItem = new CustomFoodItem(Material.getMaterial(curBoiler.getString(10)),curBoiler.getInt(4),curBoiler.getFloat(5),curBoiler.getInt(6),curBoiler.getInt(7),curBoiler.getInt(8),curBoiler.getInt(9),true, curBoiler.getBoolean(14), new Flavor(curBoiler.getInt(16),curBoiler.getInt(17),curBoiler.getInt(18),curBoiler.getInt(19),curBoiler.getInt(20),curBoiler.getInt(21)));
					newItem.getItemStack().setAmount(curBoiler.getInt(11));
					newItem.invalidItem = curBoiler.getBoolean(12);
					newItem.completed = true;
					newItem.saturation += 1;
					newItem.ingList = curBoiler.getString(15);
					newItem.customMaterial = curBoiler.getString(3);
					newItem.configVersion = curBoiler.getString(22);
					statement = plugin.getConnection().prepareStatement("DELETE FROM " + plugin.getConfig().getString("tablename1") + " WHERE location=?");
					statement.setString(1, b.getLocation().toString());
					statement.executeUpdate();
					newItem = newItem.findTexture("boiler");
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
	
	public static Location isBoilerShape(Block b) {
		if(b.getType().equals(Material.CAULDRON) && b.getRelative(BlockFace.UP).getType().equals(Material.TRIPWIRE_HOOK)) {
			if(b.getRelative(BlockFace.DOWN).getType().equals(Material.FURNACE)){
				Levelled caul = (Levelled) b.getBlockData();
				if(caul.getLevel() == caul.getMaximumLevel()) {
					return b.getLocation();
				}
			}
		}
		return null;
	}
	
	public static void playTakeOutSound(Player p, Location l) {
		p.playSound(l, Sound.BLOCK_BUBBLE_COLUMN_UPWARDS_INSIDE, 5, 1);
	}
	
	public static void playInputSound(Player p, Location l) {
		p.playSound(l, Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 4, 1);
	}
}
