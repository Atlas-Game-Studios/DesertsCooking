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
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.desertdweller.desertscooking.Main;
import me.desertdweller.desertscooking.events.FoodItemFinishedEvent;
import net.md_5.bungee.api.ChatColor;

@Deprecated
public class Stove implements Listener {
	private static Main plugin = Main.getPlugin(Main.class);
	public static final String STATION_TYPE_STOVE = "stove";
	
	public static void stoveInteract(Block b, CustomFoodItem foodItem, Player p) {
		try {
			PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.getConfig().getString("tablename1") + " WHERE location=?");
			statement.setString(1, b.getLocation().toString());
			ResultSet curStove = statement.executeQuery();
			if(foodItem.invalidItem) {
				if(curStove.next()) {
					long time = System.currentTimeMillis() - curStove.getLong(2);
					if(foodItem.item.getType().equals(Material.CLOCK)) {
						p.sendMessage(ChatColor.GREEN + "This stove has been cooking for " + Long.toString(time/60000) + " minutes.");
					}else if(foodItem.item.getType().equals(Material.AIR) && time > plugin.getConfig().getLong("stationTimes")){
						CustomFoodItem result = stoveGrab(b);
						if(result != null) {
							playTakeOutSound(p, b.getLocation());
							FoodItemFinishedEvent event = new FoodItemFinishedEvent(result, p, STATION_TYPE_STOVE);
							Bukkit.getServer().getPluginManager().callEvent(event);
							p.getInventory().addItem(result.getItemStack());
						}
					}else {
						p.sendMessage(ChatColor.RED + "This is a stove. Use this to complete your foods, and add food satisfaction to them.");
					}
				}
			}else if(foodItem.completed) {
				p.sendMessage(ChatColor.RED + "This item is already complete!");
			}else if(foodItem.mainIngredients == 1){
				if(!curStove.next()) {
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

	
	public static CustomFoodItem stoveGrab(Block b) {
		try {
			PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.getConfig().getString("tablename1") + " WHERE location=?");
			statement.setString(1, b.getLocation().toString());
			ResultSet curStove = statement.executeQuery();
			if(curStove.next()) {	
				long time = System.currentTimeMillis() - curStove.getLong(2);
				if(time > 1000){
					CustomFoodItem newItem = new CustomFoodItem(Material.getMaterial(curStove.getString(10)),curStove.getInt(4),curStove.getFloat(5),curStove.getInt(6),curStove.getInt(7),curStove.getInt(8),curStove.getInt(9),true, curStove.getBoolean(14), new Flavor(curStove.getInt(16),curStove.getInt(17),curStove.getInt(18),curStove.getInt(19),curStove.getInt(20),curStove.getInt(21)));
					newItem.getItemStack().setAmount(curStove.getInt(11));
					newItem.invalidItem = curStove.getBoolean(12);
					newItem.completed = true;
					newItem.food += 1;
					newItem.ingList = curStove.getString(15);
					newItem.customMaterial = curStove.getString(3);
					newItem.configVersion = curStove.getString(22);
					statement = plugin.getConnection().prepareStatement("DELETE FROM " + plugin.getConfig().getString("tablename1") + " WHERE location=?");
					statement.setString(1, b.getLocation().toString());
					statement.executeUpdate();
					newItem = newItem.findTexture("stove");
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
	
	public static Location isStoveShape(Block b) {
		if(b.getType().equals(Material.IRON_TRAPDOOR)){
			if(b.getRelative(BlockFace.DOWN).getType().equals(Material.FIRE) || b.getRelative(BlockFace.DOWN).getType().equals(Material.LAVA)){
				return b.getLocation();
			}
		}
		return null;
	}
	
	public static void playTakeOutSound(Player p, Location l) {
		p.playSound(l, Sound.BLOCK_CHORUS_FLOWER_GROW, 5, 1);
	}
	
	public static void playInputSound(Player p, Location l) {
		p.playSound(l, Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 4, 1);
	}
}

