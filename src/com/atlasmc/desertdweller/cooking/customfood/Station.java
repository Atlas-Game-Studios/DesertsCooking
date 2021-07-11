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
import org.bukkit.block.Furnace;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.atlasmc.desertdweller.cooking.Cooking;
import com.atlasmc.desertdweller.cooking.events.FoodItemFinishedEvent;
import com.atlasmc.desertdweller.cooking.events.FoodItemInputEvent;
import com.atlasmc.desertdweller.cooking.events.PlayerCheckStationEvent;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Station implements Listener {
	private static Cooking plugin = Cooking.getPlugin(Cooking.class);
	public static final String STATION_TYPE_STOVE = "stove";        
	public static final String STATION_TYPE_OVEN = "oven";
	public static final String STATION_TYPE_BOILER = "boiler";
	
	public static void stationInteract(Block b, CustomFoodItem foodItem, Player p, String stationType) throws SQLException {
		PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.getConfig().getString("tablename1") + " WHERE location=?");
		statement.setString(1, serializeLocation(b.getLocation()));
		ResultSet curStation = statement.executeQuery();
		if(foodItem.invalidItem) { //If the item used was not a food item.
			if(curStation.next()) {
				long time = System.currentTimeMillis() - curStation.getLong(2);
				PlayerCheckStationEvent event = new PlayerCheckStationEvent(foodItem, p, stationType, time, b);
				Bukkit.getServer().getPluginManager().callEvent(event);
			}else {
				sendStationExplain(p, stationType);
			}
		}else if(!foodItem.completed && foodItem.mainIngredients == 1) { //If the item being input is not completed and has a main ingredient.
			if(foodItem.canUseWithPerms(p)) {
				if(foodItem.secondaryIngredients == 0 && foodItem.spices == 0) {
					p.sendMessage(ChatColor.GRAY + "Use the Cutting Board to prepare this food item. (oak pressure plate on a crafting table.");
				}else if(!curStation.next()) {
					FoodItemInputEvent event = new FoodItemInputEvent(foodItem, p, stationType, b);
					Bukkit.getServer().getPluginManager().callEvent(event);
				}
			}else {
				p.sendMessage(ChatColor.RED + "Your permissions do not allow you to use this item.");
			}
		}else {
			sendFoodItemAlreadyCompleted(p);
		}
	}
	
	public static CustomFoodItem stationGrab(Block b, String stationType) throws SQLException {
		PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.getConfig().getString("tablename1") + " WHERE location=?");
		statement.setString(1, serializeLocation(b.getLocation()));
		ResultSet curStation = statement.executeQuery();
		if(curStation.next()) {	
			CustomFoodItem newItem = new CustomFoodItem(Material.getMaterial(curStation.getString(10)),curStation.getInt(4),curStation.getFloat(5),curStation.getInt(6),curStation.getInt(7),curStation.getInt(8),curStation.getInt(9),true, curStation.getBoolean(14), new Flavor(curStation.getInt(16),curStation.getInt(17),curStation.getInt(18),curStation.getInt(19),curStation.getInt(20),curStation.getInt(21)));
			newItem.getItemStack().setAmount(curStation.getInt(11));
			newItem.invalidItem = curStation.getBoolean(12);
			newItem.completed = true;
			newItem.ingList = curStation.getString(15);
			newItem.customMaterial = curStation.getString(3);
			newItem.configVersion = curStation.getString(22);
			statement = plugin.getConnection().prepareStatement("DELETE FROM " + plugin.getConfig().getString("tablename1") + " WHERE location=?");
			statement.setString(1, serializeLocation(b.getLocation()));
			statement.executeUpdate();
			if(stationType == STATION_TYPE_STOVE) {
				newItem.food += 1;
				newItem = newItem.findTexture("stove");
			}else if(stationType == STATION_TYPE_OVEN) {
				newItem.experience += 5;
				newItem = newItem.findTexture("oven");
			}else if(stationType == STATION_TYPE_BOILER) {
				newItem.saturation += 1;
				newItem = newItem.findTexture("boiler");
			}
			return newItem;
		}
		return null;
	}
	
	public static String getStationType(Block b) {
		if(b.getType().equals(Material.IRON_TRAPDOOR)){
			if(b.getRelative(BlockFace.DOWN).getType().equals(Material.FIRE) || b.getRelative(BlockFace.DOWN).getType().equals(Material.LAVA)){
				
				return STATION_TYPE_STOVE;
			}
		}else if(b.getType().equals(Material.SMOKER) && b.getRelative(BlockFace.UP).getType().equals(Material.IRON_TRAPDOOR)) {
			if(b.getRelative(BlockFace.DOWN).getType().equals(Material.FIRE) || b.getRelative(BlockFace.DOWN).getType().equals(Material.LAVA)){
				return STATION_TYPE_OVEN;
			}
		}else if(b.getType().equals(Material.CAULDRON) && b.getRelative(BlockFace.UP).getType().equals(Material.TRIPWIRE_HOOK)) {
			if(b.getRelative(BlockFace.DOWN).getType().equals(Material.BLAST_FURNACE)){
				Levelled caul = (Levelled) b.getBlockData();
				if(caul.getLevel() == caul.getMaximumLevel()) {
					return STATION_TYPE_BOILER;
				}
			}
		}
		return null;
	}
	
	@EventHandler
	private void onPlayerCheckStation(PlayerCheckStationEvent e) {
		if(e.getFoodItem().item.getType().equals(Material.AIR) && e.getTime() > plugin.getConfig().getLong("stationTimes") && e.getTime() < plugin.getConfig().getLong("maxCookTime")){
			CustomFoodItem result;
			try {
				result = stationGrab(e.getBlock(), e.getStationType());
				if(result != null) {
					furnaceExtinguish(e.getBlock(), e.getStationType());
					playTakeOutSound(e.getPlayer(), e.getBlock().getLocation(), e.getStationType());
					sendSuccessfulTakeout(e.getPlayer(), result);
					FoodItemFinishedEvent event = new FoodItemFinishedEvent(result, e.getPlayer(), STATION_TYPE_STOVE);
					Bukkit.getServer().getPluginManager().callEvent(event);
					if(!event.isCancelled()) {
						e.getPlayer().getInventory().addItem(result.getItemStack());
					}
				}
			} catch (SQLException exc) {
				exc.printStackTrace();
			}
		}else if(e.getFoodItem().item.getType().equals(Material.CLOCK)) {
			sendTimeCooking(e.getPlayer(),Long.toString(e.getTime()/60000));
		}else if(e.getFoodItem().item.getType().equals(Material.AIR) && e.getTime() > plugin.getConfig().getLong("maxCookTime")){
			try {
				stationGrab(e.getBlock(), e.getStationType());
			} catch (SQLException exc) {
				exc.printStackTrace();
			}
			sendFailedTakout(e.getPlayer());
		}else {
			sendCookingStationActive(e.getPlayer());
		}
	}
	
	@EventHandler
	private static void onFoodItemInput(FoodItemInputEvent e) throws SQLException {
		playInputSound(e.getPlayer(), e.getBlock().getLocation(), e.getStationType());
		sendSuccessfulInput(e.getPlayer());
		furnaceLight(e.getBlock(), e.getStationType());
		PreparedStatement statement = (PreparedStatement) plugin.getConnection().prepareStatement("INSERT INTO " + plugin.getConfig().getString("tablename1") + "(location, timeplaced, customMaterial, food, saturation, experience, mainingredients, secondaryingredients, spices, material, amount, invaliditem, completed, poisoned, ingredients, spicyness, sweetness, bitterness, savoryness, saltyness, sourness, configVersion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		statement.setString(1, serializeLocation(e.getBlock().getLocation()));
		statement.setLong(2, System.currentTimeMillis());
		CustomFoodItem foodItem = e.getFoodItem();
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
		ItemStack changedAmount = e.getPlayer().getInventory().getItemInMainHand();
		changedAmount.setAmount(changedAmount.getAmount()-1);
		if(changedAmount.getAmount() == 0) {
			e.getPlayer().getInventory().setItemInMainHand(null);
		}
		e.getPlayer().getInventory().setItemInMainHand(changedAmount);
	}
	
	public static void furnaceLight(Block block, String stationType) {
		//Need to check if the chunk is loaded here
		if(stationType == STATION_TYPE_BOILER && block.getRelative(BlockFace.DOWN).getState() instanceof Furnace) {
			Furnace furnace = (Furnace) block.getRelative(BlockFace.DOWN).getState();
			Lightable furnaceData = (Lightable) furnace.getBlockData();
            furnaceData.setLit(true);
            furnace.setBlockData(furnaceData);
            furnace.setBurnTime((short) 1200);
            furnace.update();
		}else if(stationType == STATION_TYPE_OVEN &&  block.getState() instanceof Furnace) {
			Furnace furnace = (Furnace) block.getState();
			Lightable furnaceData = (Lightable) furnace.getBlockData();
            furnaceData.setLit(true);
            furnace.setBlockData(furnaceData);
            furnace.setBurnTime((short) 1200);
            furnace.update();
		}
	}
	
	public static void furnaceExtinguish(Block block, String stationType) {
		if(stationType == STATION_TYPE_BOILER && block.getRelative(BlockFace.DOWN).getState() instanceof Furnace) {
			Furnace furnace = (Furnace) block.getRelative(BlockFace.DOWN).getState();
			Lightable furnaceData = (Lightable) furnace.getBlockData();
            furnaceData.setLit(false);
            furnace.setBlockData(furnaceData);
            furnace.setBurnTime((short) 0);
            furnace.update();
		}else if(stationType == STATION_TYPE_OVEN &&  block.getState() instanceof Furnace) {
			Furnace furnace = (Furnace) block.getState();
			Lightable furnaceData = (Lightable) furnace.getBlockData();
            furnaceData.setLit(false);
            furnace.setBlockData(furnaceData);
            furnace.setBurnTime((short) 0);
            furnace.update();
		}
	}
	
	
	private static void playTakeOutSound(Player p, Location l, String stationType) {
		if(stationType == STATION_TYPE_STOVE) {
			p.playSound(l, Sound.BLOCK_CHORUS_FLOWER_GROW, 5, 1);
		}else if(stationType == STATION_TYPE_OVEN) {
			p.playSound(l, Sound.UI_TOAST_OUT, 5, 1);
		}else if(stationType == STATION_TYPE_BOILER) {
			p.playSound(l, Sound.BLOCK_BUBBLE_COLUMN_UPWARDS_INSIDE, 5, 1);
		}
	}
	
	private static void playInputSound(Player p, Location l, String stationType) {
		if(stationType == STATION_TYPE_STOVE) {
			p.playSound(l, Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 4, 1);
		}else if(stationType == STATION_TYPE_OVEN) {
			p.playSound(l, Sound.UI_TOAST_IN, 4, 1);
		}else if(stationType == STATION_TYPE_BOILER) {
			p.playSound(l, Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 4, 1);
		}
	}
	
	private static void sendStationExplain(Player p, String stationType) {
		if(stationType == STATION_TYPE_STOVE)
			p.sendMessage(ChatColor.GRAY + "This is a stove, use it to finalize your foods, and to add more hunger satisfaction to them.");
		if(stationType == STATION_TYPE_OVEN)
			p.sendMessage(ChatColor.GRAY + "This is an oven, use it to finalize your foods, and to add more experience to them.");
		if(stationType == STATION_TYPE_BOILER)
			p.sendMessage(ChatColor.GRAY + "This is a cooking pot, use it to finalize your foods, and to add more saturation value to them");
	}
	
	private static void sendSuccessfulInput(Player p) {
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "You placed a food item into the station!"));
	}
	
	private static void sendSuccessfulTakeout(Player p, CustomFoodItem foodItem) {
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "You have taken out a " + foodItem.getItemStack().getItemMeta().getDisplayName()));
	}
	
	private static void sendCookingStationActive(Player p) {
		p.sendMessage(ChatColor.GRAY + "This station is cooking! Use a clock to see for how long.");
	}
	
	private static void sendTimeCooking(Player p, String time) {
		if(time != "1" && time != "0") {
			p.sendMessage(ChatColor.GRAY + "This station has been cooking for " + time + " minutes.");
		}else if(time == "1") {
			p.sendMessage(ChatColor.GRAY + "This station has been cooking for only 1 minute.");
		}else {
			p.sendMessage(ChatColor.GRAY + "This station has just begun cooking.");
		}
	}
	
	private static void sendFailedTakout(Player p) {
		p.sendMessage(ChatColor.GRAY + "You left this item in for too long, and it has now become mush.");
	}
	
	private static void sendFoodItemAlreadyCompleted(Player p) {
		p.sendMessage(ChatColor.GRAY + "This item is either already completed or has no main ingredient.");
	}
	
	public static String serializeLocation(Location l) {
		return new String(l.getWorld().getName() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ());
	}
	
	public static Location deserializeLocation(String s) {
		String[] stringArray = s.split(",");
		return new Location(Bukkit.getWorld(stringArray[0]), Double.parseDouble(stringArray[1]), Double.parseDouble(stringArray[2]), Double.parseDouble(stringArray[3]));
	}
}
