package com.atlasmc.desertdweller.cooking.customfood;



import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.atlasmc.desertdweller.cooking.Cooking;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
import net.md_5.bungee.api.ChatColor;

public class CustomFoodItem{
	private static Plugin plugin = Cooking.getPlugin(Cooking.class);
	private int food;
	private float saturation;
	private int experience;
	private int mainIngredients;
	private int secondaryIngredients;
	private int spices;
	private ItemStack item;
	private ItemStack trashItem;
	private boolean invalidItem = true;
	private boolean completed;
	private boolean poisoned;
	private Flavor flavor;
	private String customMaterial;
	private String ingList = "";
	private String configVersion = null;
	
	
	
	public CustomFoodItem(Material item, int food, float saturation, int experience, int mainIngredients, int secondaryIngredients, int spices, boolean completed, boolean poisoned, Flavor flavor) {
		this.food = food;
		this.saturation = saturation;
		this.experience = experience;
		this.mainIngredients = mainIngredients;
		this.secondaryIngredients = secondaryIngredients;
		this.spices = spices;
		this.invalidItem = false;
		this.item = new ItemStack(item);
		this.completed = completed;
		this.poisoned = poisoned;
		this.flavor = flavor;
	}
	
	public CustomFoodItem(ItemStack item) {
		this.item = item;
		if(item.getType().equals(Material.AIR)) {
			invalidItem = true;
			flavor = new Flavor(0,0,0,0,0,0);
			return;
		}
		NBTItem nbti = new NBTItem(item);
		if(nbti.hasKey("Plugin") && nbti.getString("Plugin").equals("Cooking")) { //If it is part of this plugin.
			food = nbti.getInteger("Food");
			saturation = nbti.getFloat("Saturation");
			experience = nbti.getInteger("Experience");
			mainIngredients = nbti.getInteger("Main Ingredients");
			secondaryIngredients = nbti.getInteger("Secondary Ingredients");
			spices = nbti.getInteger("Spices");
			invalidItem = false;
			completed = nbti.getBoolean("Completed");
			poisoned = nbti.getBoolean("Poisoned");
			flavor = new Flavor(nbti.getIntArray("Flavor"));
			customMaterial = nbti.getString("Material");
			ingList = nbti.getString("Materials Used");
			if(nbti.hasKey("Trash Material"))
				trashItem = new ItemStack(Material.getMaterial(nbti.getString("Trash Material")));
			if(nbti.hasKey("Config Version")) {
				configVersion = nbti.getString("Config Version");
			}else {
				configVersion = plugin.getConfig().getString("configVersion");
			}
			if(configVersion != plugin.getConfig().getString("configVersion") && configVersion != null) {
				updateFoodItemStats();
			}
		}else {
			boolean found = false;
			if(plugin.getConfig().contains("customItems")) {
				for(String key : plugin.getConfig().getConfigurationSection("customItems").getKeys(false)) {
					if(nbti.hasKey(plugin.getConfig().getString("customItems." + key + ".NBTKey")) && nbti.getString(plugin.getConfig().getString("customItems." + key + ".NBTKey")).equalsIgnoreCase(plugin.getConfig().getString("customItems." + key + ".NBTString"))) {
						found = true;
						food = (int) plugin.getConfig().getDouble("customItems." + key + ".food");   //If this is a custom item in config.
						saturation = (float) plugin.getConfig().getDouble("customItems." + key + ".saturation");
						experience = (int) plugin.getConfig().getDouble("customItems." + key + ".experience");
						mainIngredients = Integer.parseInt(plugin.getConfig().getString("customItems." + key + ".types").split("-")[0]);
						secondaryIngredients = Integer.parseInt(plugin.getConfig().getString("customItems." + key + ".types").split("-")[1]);
						spices = Integer.parseInt(plugin.getConfig().getString("customItems." + key + ".types").split("-")[2]);
						invalidItem = false;
						completed = false;
						customMaterial = key;
						if(plugin.getConfig().getString("customItems." + key + ".poisoned") == "true") {
							poisoned = true;
						}
						flavor = new Flavor("customItems." + key);
						configVersion = plugin.getConfig().getString("configVersion");
					}
				}
			}
			if(!found) {
				if(plugin.getConfig().contains("vanillaItems")) {
					for(String key : plugin.getConfig().getConfigurationSection("vanillaItems").getKeys(false)) {
						if(plugin.getConfig().getString("vanillaItems." + key + ".material").equals(item.getType().toString())) { //If it is a vanilla item in config
							found = true;
							food = (int) plugin.getConfig().getDouble("vanillaItems." + key + ".food");
							saturation = (float) plugin.getConfig().getDouble("vanillaItems." + key + ".saturation");
							experience = (int) plugin.getConfig().getDouble("vanillaItems." + key + ".experience");
							mainIngredients = Integer.parseInt(plugin.getConfig().getString("vanillaItems." + key + ".types").split("-")[0]);
							secondaryIngredients = Integer.parseInt(plugin.getConfig().getString("vanillaItems." + key + ".types").split("-")[1]);
							spices = Integer.parseInt(plugin.getConfig().getString("vanillaItems." + key + ".types").split("-")[2]);
							invalidItem = false;
							completed = false;
							if(plugin.getConfig().getString("vanillaItems." + key + ".trashMaterial") != null)
								trashItem = new ItemStack(Material.getMaterial(plugin.getConfig().getString("vanillaItems." + key + ".trashMaterial")));
							customMaterial = key;
							configVersion = plugin.getConfig().getString("configVersion");
							if(plugin.getConfig().getString("vanillaItems." + key + ".poisoned") == "true") {
								poisoned = true;
							}
							flavor = new Flavor("vanillaItems." + key);
						}
					}
				}
			}
		}
	}
	
	public ItemStack getItemStack() {
		ItemStack item = this.item;
		NBTItem nbti = new NBTItem(item);
		nbti.setString("Plugin", "Cooking");
		nbti.setInteger("Food", food);
		nbti.setFloat("Saturation", saturation);
		nbti.setInteger("Experience", experience);
		nbti.setInteger("Main Ingredients", mainIngredients);
		nbti.setInteger("Secondary Ingredients", secondaryIngredients);
		nbti.setInteger("Spices", spices);
		nbti.setBoolean("Completed", completed);
		nbti.setBoolean("Poisoned", poisoned);
		nbti.setString("Material", customMaterial);
		nbti.setString("Materials Used", ingList);
		nbti.setString("Config Version", configVersion);
		if(this.trashItem != null)
			nbti.setString("Trash Material", this.trashItem.getType().name());
		if(flavor != null) {
			nbti.setIntArray("Flavor", flavor.flavorList);
		}else {
			nbti.setIntArray("Flavor", null);
		}
		item = nbti.getItem();
		
		ArrayList<String> lore = new ArrayList<String>();
		if(completed == true) {
			lore.add(ChatColor.GREEN + "Cooked Food");
			lore.add(ChatColor.GRAY + "I wonder how it tastes?");
		}else {
			lore.add(ChatColor.GREEN + "Prepared Food Ingredients");
			for(String ingredient : ingList.split(",")) {
				lore.add(ChatColor.GRAY + " - " + ingredient);
			}
		}
		if(plugin.getConfig().getBoolean("foodInfoLore")) {
			lore.add("Food: " + food);
			lore.add("Saturation: " + saturation);
			lore.add("Experience: " + experience);
		}
		ItemMeta meta = item.getItemMeta();
		if(meta != null) {
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
		
		return item;
	}
	
	public NBTItem getNBTItem() {
		ItemStack item = this.item;
		NBTItem nbti = new NBTItem(item);
		nbti.setString("Plugin", "Cooking");
		nbti.setInteger("Food", food);
		nbti.setFloat("Saturation", saturation);
		nbti.setInteger("Experience", experience);
		nbti.setInteger("Main Ingredients", mainIngredients);
		nbti.setInteger("Secondary Ingredients", secondaryIngredients);
		nbti.setInteger("Spices", spices);
		nbti.setBoolean("Completed", completed);
		nbti.setBoolean("Poisoned", poisoned);
		nbti.setIntArray("Flavor", flavor.flavorList);
		nbti.setString("Material", customMaterial);
		nbti.setString("Materials Used", ingList);
		nbti.setString("Config Version", configVersion);
		return nbti;
	}
	
	public void combine(CustomFoodItem item) {
		if(!invalidItem && !item.invalidItem) {
			if((mainIngredients == 1 && item.mainIngredients == 0) || (mainIngredients == 0 && item.mainIngredients == 0)) {
				if(customMaterial != "null" && customMaterial != null) {
					food += item.food;
					saturation += item.saturation;
					experience += item.experience;
					secondaryIngredients += item.secondaryIngredients;
					spices += item.spices;	
					flavor.addFlavor(item.flavor);
					configVersion = plugin.getConfig().getString("configVersion");
					if(ingList != "") {
						if(item.ingList != "") {
							ingList = ingList.concat("," + item.ingList);
						}else {
							ingList = ingList.concat("," + item.customMaterial);
						}
					}else {
						if(item.ingList != "") {
							ingList = customMaterial + "," + item.ingList;
						}else {
							ingList = customMaterial + "," + item.customMaterial;
						}
					}
				}else {
					this.item = item.item;
					food += item.food;
					saturation += item.saturation;
					experience += item.experience;
					mainIngredients = item.mainIngredients;
					secondaryIngredients += item.secondaryIngredients;
					spices += item.spices;
					trashItem = item.trashItem;
					flavor.addFlavor(item.flavor);
					configVersion = plugin.getConfig().getString("configVersion");
					customMaterial = item.customMaterial;
					if(item.ingList != "") {
						ingList = item.ingList;
					}else {
						ingList = item.customMaterial;
					}
				}
			}else if(mainIngredients == 0 && item.mainIngredients == 1) {
				if(customMaterial != "null" && customMaterial != null) {
					this.item = item.item;
					food += item.food;
					saturation += item.saturation;
					experience += item.experience;
					secondaryIngredients += item.secondaryIngredients;
					spices += item.spices;
					mainIngredients = 1;
					flavor.addFlavor(item.flavor);
					trashItem = item.trashItem;
					configVersion = plugin.getConfig().getString("configVersion");
					if(ingList != "") {
						if(item.ingList != "") {
							ingList = ingList.concat("," + item.ingList);
						}else {
							ingList = ingList.concat("," + item.customMaterial);
						}
					}else {
						if(item.ingList != "") {
							ingList = customMaterial  + ","  + item.ingList;
						}else {
							ingList = customMaterial  + "," +  item.customMaterial;
						}
					}
					customMaterial = item.customMaterial;
				}else {
					food += item.food;
					saturation += item.saturation;
					experience += item.experience;
					mainIngredients = item.mainIngredients;
					secondaryIngredients += item.secondaryIngredients;
					spices += item.spices;
					this.item = item.item;
					flavor.addFlavor(item.flavor);
					trashItem = item.trashItem;
					configVersion = plugin.getConfig().getString("configVersion");
					customMaterial = item.customMaterial;
					if(item.ingList != "") {
						ingList = item.ingList;
					}else {
						ingList = item.customMaterial;
					}
				}
			}
			if(item.poisoned) {
				poisoned = true;
			}
		}
	}
	
	public boolean canCombine(CustomFoodItem item) {
		return (mainIngredients + item.mainIngredients <= 1) && !invalidItem && !item.invalidItem && !completed && !item.completed && (secondaryIngredients + item.secondaryIngredients <= 3) && (spices + item.spices <= 4);
	}
	
	public boolean canUseWithPerms(Player p) {
		if(!p.hasPermission("cooking.passedtutorial")) {
			for(String ing : ingList.split(",")) {
				if(!ing.equalsIgnoreCase("seeds") && !ing.equalsIgnoreCase("porkchop") && !ing.equalsIgnoreCase("carrot")) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void finish() {
		this.findTexture();
		this.completed = true;
	}
	
	public void findTexture() {
		
	}
	
	@Deprecated
	public CustomFoodItem findTexture(String stationType) {
		CustomFoodItem newItem = this;
		if(plugin.getConfig().contains("vanillaItems." + customMaterial)) {
			String[] keys = plugin.getConfig().getString("vanillaItems." + customMaterial + "." + stationType + "Textures.KEYS").split(",");
			String textureKey = "";
			if(keys[0] != "none") {
				String[] ingredientsUsed = ingList.split(",");
				for(String key : plugin.getConfig().getString("vanillaItems." + customMaterial + "." + stationType + "Textures.KEYS").split(",")) {
					for(String ing : ingredientsUsed) {
						if(key.equals(ing) && textureKey == "") {
							textureKey = plugin.getConfig().getString("vanillaItems." + customMaterial + "." + stationType + "Textures." + key);
						}
					}
				}
			}
			if(textureKey == "") {
				textureKey = plugin.getConfig().getString("vanillaItems." + customMaterial + "." + stationType + "Textures.DEFAULT");
			}
			if(plugin.getConfig().getString("textures." + textureKey + ".head") == "false") {
				newItem.customMaterial = textureKey;
				ItemStack newStack = newItem.item;
				newStack.setType(Material.getMaterial(plugin.getConfig().getString("textures." + textureKey + ".material")));
				ItemMeta meta = newStack.getItemMeta();
				meta.setDisplayName(ChatColor.GREEN + plugin.getConfig().getString("textures." + textureKey + ".lorename"));
				newStack.setItemMeta(meta);
			}else {
				newItem.customMaterial = textureKey;
				String id = plugin.getConfig().getString("textures." + textureKey + ".id");
				String value = plugin.getConfig().getString("textures." + textureKey + ".value");
				ItemStack newStack = buildHead(id, value);
				ItemMeta meta = newStack.getItemMeta();
				meta.setDisplayName(ChatColor.GREEN + plugin.getConfig().getString("textures." + textureKey + ".lorename"));
				newStack.setItemMeta(meta);
				newItem.item = newStack;
			}
		}
		return newItem;
	}
	
	public static ItemStack buildHead(String id, String value) {
		ItemStack head = new ItemStack(Material.PLAYER_HEAD);
		NBTItem nbtHead = new NBTItem(head);
		NBTCompound skull = nbtHead.addCompound("SkullOwner");
		skull.setString("Id", id);
		NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
		texture.setString("Value", value);
		return nbtHead.getItem();
	}
	
	private void updateFoodItemStats() { //todo
		
	}

	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		this.food = food;
	}

	public float getSaturation() {
		return saturation;
	}

	public void setSaturation(float saturation) {
		this.saturation = saturation;
	}

	public int getMainIngredients() {
		return mainIngredients;
	}

	public void setMainIngredients(int mainIngredients) {
		this.mainIngredients = mainIngredients;
	}

	public int getSecondaryIngredients() {
		return secondaryIngredients;
	}

	public void setSecondaryIngredients(int secondaryIngredients) {
		this.secondaryIngredients = secondaryIngredients;
	}

	public int getSpices() {
		return spices;
	}

	public void setSpices(int spices) {
		this.spices = spices;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public ItemStack getTrashItem() {
		return trashItem;
	}

	public void setTrashItem(ItemStack trashItem) {
		this.trashItem = trashItem;
	}

	public boolean isInvalidItem() {
		return invalidItem;
	}

	public void setInvalidItem(boolean invalidItem) {
		this.invalidItem = invalidItem;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public boolean isPoisoned() {
		return poisoned;
	}

	public void setPoisoned(boolean poisoned) {
		this.poisoned = poisoned;
	}

	public Flavor getFlavor() {
		return flavor;
	}

	public void setFlavor(Flavor flavor) {
		this.flavor = flavor;
	}

	public String getCustomMaterial() {
		return customMaterial;
	}

	public void setCustomMaterial(String customMaterial) {
		this.customMaterial = customMaterial;
	}

	public String getIngList() {
		return ingList;
	}

	public void setIngList(String ingList) {
		this.ingList = ingList;
	}

	public String getConfigVersion() {
		return configVersion;
	}

	public void setConfigVersion(String configVersion) {
		this.configVersion = configVersion;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}
}
