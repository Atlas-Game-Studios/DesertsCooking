package com.atlasmc.desertdweller.cooking.customfood;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.atlasmc.desertdweller.cooking.Cooking;

import net.md_5.bungee.api.ChatColor;

public class StationGUI implements Listener{
	static ItemStack mainIngredientSlots;
	static ItemStack secondaryIngredientSlots;
	static ItemStack spiceSlots;
	static ItemStack reservedSlot;
	static ItemStack finalSlot;
	static ItemStack air = new ItemStack(Material.AIR);
	private static Plugin plugin = Cooking.getPlugin(Cooking.class);
	static HashMap<Inventory, Player> openGUIs = new HashMap<Inventory, Player>();
	
	
	public static void openIngredientGUI(Location l, Player p) {
		Inventory inv = plugin.getServer().createInventory(null, 45, "Cutting Board");
		
		setItemStacks();
		ItemStack[] contents = {reservedSlot, reservedSlot, reservedSlot, reservedSlot, reservedSlot, reservedSlot, reservedSlot, reservedSlot, reservedSlot,
								mainIngredientSlots, air, reservedSlot, secondaryIngredientSlots, air, air, air, reservedSlot, reservedSlot,
								reservedSlot, reservedSlot, reservedSlot, reservedSlot, reservedSlot, reservedSlot, reservedSlot, finalSlot, reservedSlot,
								spiceSlots, air, air, air, air, reservedSlot, reservedSlot, air, reservedSlot,
								reservedSlot, reservedSlot, reservedSlot, reservedSlot, reservedSlot, reservedSlot, reservedSlot, reservedSlot, reservedSlot}; //Creates the GUI
		inv.setContents(contents);
		
		p.openInventory(inv);
		openGUIs.put(inv, p);
	}
	
	@EventHandler
	private void onPlayerInventoryInteract(InventoryClickEvent e) {
		Inventory curInv = e.getClickedInventory();
		// If the inventory clicked is a crafting inventory.
		if(curInv != null && openGUIs.containsKey(curInv)) {
			// Checks if it is one of the crafting slots.
			if(e.getSlot() == 10 || e.getSlot() == 13 || e.getSlot() == 14 || e.getSlot() ==  15 || e.getSlot() == 28 || e.getSlot() == 29 || e.getSlot() == 30 || e.getSlot() == 31) {
				if(e.getCurrentItem() != null) {
					//If the slot clicked has an item, give it back to the player, and make the slot empty.
					e.getWhoClicked().getInventory().addItem(e.getCurrentItem());
					e.getClickedInventory().setItem(e.getSlot(), air);
					curInv = updateResult(curInv);
					Player p = (Player) e.getWhoClicked();
					p.playSound(p.getLocation(), Sound.BLOCK_WOOL_BREAK, 1, 1.8f);
				}
			//Checks to see if it is the result slot.
			}else if(e.getSlot() == 34) {
				if(e.getCurrentItem() != air) { //If the result slot is not empty, clear cutting board and give the result item.
					resetIngredients((Player) e.getWhoClicked(), e.getClickedInventory());
					
					e.getWhoClicked().getInventory().addItem(e.getCurrentItem());
					curInv = updateResult(curInv);
					Player p = (Player) e.getWhoClicked();
					p.playSound(p.getLocation(), Sound.UI_TOAST_OUT, 1, 2f);
					
//					for(int i = 0; i <360; i+=10){
//						Location loc = cuttingBoardLocations.get(p).clone();
//						loc.add(0.5f + Math.cos(i)*0.7f, 1, 0.5f + Math.sin(i)*0.7f);
//						p.getWorld().spawnParticle(Particle.CRIMSON_SPORE, loc, 3, 0, 1, 0, 0.005f);
//					}
				}
			}
			e.setCancelled(true);
		}
		// If the inventory clicked is a player inventory, and the player has cutting board open.
		if(e.getClickedInventory() != null && e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
			Inventory topInv = e.getWhoClicked().getOpenInventory().getTopInventory();
			if(topInv != null && openGUIs.containsKey(topInv) && e.getCurrentItem() != null) {
				CustomFoodItem foodItem = new CustomFoodItem(e.getCurrentItem());
				//If the food item is not invalid, or already completed.
				if(!foodItem.invalidItem && !foodItem.completed) {
					//If the food item has any main ingredients, and the main slot is open.
					if(foodItem.mainIngredients == 1 && topInv.getItem(10) == null) {
						ItemStack movedItem = e.getCurrentItem().clone();
						movedItem.setAmount(1);
						topInv.setItem(10, movedItem);
						e.getClickedInventory().setItem(e.getSlot(), subtractFromItemStack(e.getCurrentItem(), 1));
						topInv = updateResult(topInv);
						Player p = (Player) e.getWhoClicked();
						p.playSound(p.getLocation(), Sound.BLOCK_BAMBOO_FALL, 1, 1);
					//If the food item has any secondary ingredients, but not main.
					}else if(foodItem.mainIngredients == 0 && foodItem.secondaryIngredients > 0) {
						//checks to see if the available slots are open.
						if(topInv.getItem(13) == null) {
							ItemStack movedItem = e.getCurrentItem().clone();
							movedItem.setAmount(1);
							topInv.setItem(13, movedItem);
							e.getClickedInventory().setItem(e.getSlot(), subtractFromItemStack(e.getCurrentItem(), 1));
							topInv = updateResult(topInv);
							Player p = (Player) e.getWhoClicked();
							p.playSound(p.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 1, 1);
						}else if(topInv.getItem(14) == null) {
							ItemStack movedItem = e.getCurrentItem().clone();
							movedItem.setAmount(1);
							topInv.setItem(14, movedItem);
							e.getClickedInventory().setItem(e.getSlot(), subtractFromItemStack(e.getCurrentItem(), 1));
							topInv = updateResult(topInv);
							Player p = (Player) e.getWhoClicked();
							p.playSound(p.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 1, 1);
						}else if(topInv.getItem(15) == null) {
							ItemStack movedItem = e.getCurrentItem().clone();
							movedItem.setAmount(1);
							topInv.setItem(15, movedItem);
							e.getClickedInventory().setItem(e.getSlot(), subtractFromItemStack(e.getCurrentItem(), 1));
							topInv = updateResult(topInv);
							Player p = (Player) e.getWhoClicked();
							p.playSound(p.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 1, 1);
						}
					//Checks to see if the food item has any spices, but no secondary or main ingredients.
					}else if(foodItem.mainIngredients == 0 && foodItem.secondaryIngredients == 0 && foodItem.spices > 0) {
						//Finds next open spice slot, if any.
						if(topInv.getItem(28) == null) {
							ItemStack movedItem = e.getCurrentItem().clone();
							movedItem.setAmount(1);
							topInv.setItem(28, movedItem);
							e.getClickedInventory().setItem(e.getSlot(), subtractFromItemStack(e.getCurrentItem(), 1));
							topInv = updateResult(topInv);
							Player p = (Player) e.getWhoClicked();
							p.playSound(p.getLocation(), Sound.BLOCK_BAMBOO_FALL, 1, 1.5f);
						}else if(topInv.getItem(29) == null) {
							ItemStack movedItem = e.getCurrentItem().clone();
							movedItem.setAmount(1);
							topInv.setItem(29, movedItem);
							e.getClickedInventory().setItem(e.getSlot(), subtractFromItemStack(e.getCurrentItem(), 1));
							topInv = updateResult(topInv);
							Player p = (Player) e.getWhoClicked();
							p.playSound(p.getLocation(), Sound.BLOCK_BAMBOO_FALL, 1, 1.5f);
						}else if(topInv.getItem(30) == null) {
							ItemStack movedItem = e.getCurrentItem().clone();
							movedItem.setAmount(1);
							topInv.setItem(30, movedItem);
							e.getClickedInventory().setItem(e.getSlot(), subtractFromItemStack(e.getCurrentItem(), 1));
							topInv = updateResult(topInv);
							Player p = (Player) e.getWhoClicked();
							p.playSound(p.getLocation(), Sound.BLOCK_BAMBOO_FALL, 1, 1.5f);
						}else if(topInv.getItem(31) == null) {
							ItemStack movedItem = e.getCurrentItem().clone();
							movedItem.setAmount(1);
							topInv.setItem(31, movedItem);
							e.getClickedInventory().setItem(e.getSlot(), subtractFromItemStack(e.getCurrentItem(), 1));
							topInv = updateResult(topInv);
							Player p = (Player) e.getWhoClicked();
							p.playSound(p.getLocation(), Sound.BLOCK_BAMBOO_FALL, 1, 1.5f);
						}
					}
				}else {
					Player p = (Player) e.getWhoClicked();
					p.playSound(p.getLocation(), Sound.ENTITY_VEX_HURT, 1, 2f);
				}
				e.setCancelled(true);
			}
		}
	}
	
	private void resetIngredients(Player p, Inventory inv) {
		boolean resultTrashAccountedFor = false;
		
		resultTrashAccountedFor = readdToBoard(p, inv, 10, resultTrashAccountedFor);
		resultTrashAccountedFor = readdToBoard(p, inv, 13, resultTrashAccountedFor);
		resultTrashAccountedFor = readdToBoard(p, inv, 14, resultTrashAccountedFor);
		resultTrashAccountedFor = readdToBoard(p, inv, 15, resultTrashAccountedFor);
		resultTrashAccountedFor = readdToBoard(p, inv, 28, resultTrashAccountedFor);
		resultTrashAccountedFor = readdToBoard(p, inv, 29, resultTrashAccountedFor);
		resultTrashAccountedFor = readdToBoard(p, inv, 30, resultTrashAccountedFor);
		resultTrashAccountedFor = readdToBoard(p, inv, 31, resultTrashAccountedFor);
	}
	
	private boolean readdToBoard(Player p, Inventory inv, int id, boolean resultTrashAccountedFor) {
		ItemStack item = inv.getItem(id);
		if(item == null)
			return resultTrashAccountedFor;
		ItemStack extraItem = playerHasItems(p, item);
		if(extraItem != null) {
			extraItem.setAmount(extraItem.getAmount() - 1);
		}else {
			inv.setItem(id, air);
		}
		
		CustomFoodItem resultItemClear = new CustomFoodItem(new ItemStack(inv.getItem(34).getType()));
		CustomFoodItem food = new CustomFoodItem(item);
		
		if(food.trashItem == null)
			return resultTrashAccountedFor;
		
		if(resultItemClear.trashItem != null && resultItemClear.trashItem.equals(food.trashItem) && !resultTrashAccountedFor) {
			return true;
		}
		
		p.getInventory().addItem(food.trashItem);
		return resultTrashAccountedFor;
	}
	
	private static void setItemStacks() {
		mainIngredientSlots = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "This slot is for the main");
		lore.add(ChatColor.GRAY + "ingredient you intend to");
		lore.add(ChatColor.GRAY + "use.");
		lore.add(ChatColor.BOLD + "" + ChatColor.GRAY + "This is required in your");
		lore.add(ChatColor.BOLD + "" + ChatColor.GRAY +  "final food item to be able");
		lore.add(ChatColor.BOLD + "" + ChatColor.GRAY + "to cook it.");
		ItemMeta temp = mainIngredientSlots.getItemMeta();
		temp.setLore(lore);
		temp.setDisplayName(ChatColor.RED + "Main Ingredients");
		mainIngredientSlots.setItemMeta(temp);
		
		secondaryIngredientSlots = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		lore.clear();
		lore.add(ChatColor.GRAY + "This slot is for the secondary");
		lore.add(ChatColor.GRAY + "ingredient you intend to use.");
		temp = secondaryIngredientSlots.getItemMeta();
		temp.setLore(lore);
		temp.setDisplayName(ChatColor.RED + "Secondary Ingredients");
		secondaryIngredientSlots.setItemMeta(temp);
		
		spiceSlots = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		lore.clear();
		lore.add(ChatColor.GRAY + "This slot is for the spices you");
		lore.add(ChatColor.GRAY + "intend to use.");
		temp = spiceSlots.getItemMeta();
		temp.setLore(lore);
		temp.setDisplayName(ChatColor.RED + "Spices");
		spiceSlots.setItemMeta(temp);
		
		finalSlot = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		lore.clear();
		lore.add(ChatColor.GRAY + "This is the item you create");
		lore.add(ChatColor.GRAY + "from combining the ingredients.");
		temp = finalSlot.getItemMeta();
		temp.setLore(lore);
		temp.setDisplayName(ChatColor.RED + "Result");
		finalSlot.setItemMeta(temp);
		
		reservedSlot = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
	}
	
	public Inventory updateResult(Inventory inv) {
		boolean invalidMix = false;
		boolean allAir = true;
		CustomFoodItem mainItem = new CustomFoodItem(Material.AIR,0,0,0,0,0,0,false, false, new Flavor(0,0,0,0,0,0));
		for(int i = 1; i <= 8; i++) {
			if(i == 1) {
				if(inv.getItem(10) != null) {
					CustomFoodItem curItem = new CustomFoodItem(inv.getItem(10));
					allAir = false;
					if(mainItem.canCombine(curItem)) {
						mainItem.combine(curItem);
					}else {
						invalidMix = true;
					}
				}
			}else if(i == 2) {
				if(inv.getItem(13) != null) {
					CustomFoodItem curItem = new CustomFoodItem(inv.getItem(13));
					allAir = false;
					if(mainItem.canCombine(curItem)) {
						mainItem.combine(curItem);
					}else {
						invalidMix = true;
					}
				}
			}else if(i == 3) {
				if(inv.getItem(14) != null) {
					CustomFoodItem curItem = new CustomFoodItem(inv.getItem(14));
					allAir = false;
					if(mainItem.canCombine(curItem)) {
						mainItem.combine(curItem);
					}else {
						invalidMix = true;
					}
				}
			}else if(i == 4) {
				if(inv.getItem(15) != null) {
					CustomFoodItem curItem = new CustomFoodItem(inv.getItem(15));
					allAir = false;
					if(mainItem.canCombine(curItem)) {
						mainItem.combine(curItem);
					}else {
						invalidMix = true;
					}
				}
			}else if(i == 5) {
				if(inv.getItem(28) != null) {
					CustomFoodItem curItem = new CustomFoodItem(inv.getItem(28));
					allAir = false;
					if(mainItem.canCombine(curItem)) {
						mainItem.combine(curItem);
					}else {
						invalidMix = true;
					}
				}
			}else if(i == 6) {
				if(inv.getItem(29) != null) {
					CustomFoodItem curItem = new CustomFoodItem(inv.getItem(29));
					allAir = false;
					if(mainItem.canCombine(curItem)) {
						mainItem.combine(curItem);
					}else {
						invalidMix = true;
					}
				}
			}else if(i == 7) {
				if(inv.getItem(30) != null) {
					CustomFoodItem curItem = new CustomFoodItem(inv.getItem(30));
					allAir = false;
					if(mainItem.canCombine(curItem)) {
						mainItem.combine(curItem);
					}else {
						invalidMix = true;
					}
				}
			}else if(i == 8) {
				if(inv.getItem(31) != null) {
					CustomFoodItem curItem = new CustomFoodItem(inv.getItem(31));
					allAir = false;
					if(mainItem.canCombine(curItem)) {
						mainItem.combine(curItem);
					}else {
						invalidMix = true;
					}
				}
			}
		}
		if(!invalidMix && !allAir) {
			inv.setItem(34, mainItem.getItemStack());
		}else {
			inv.setItem(34, air);
		}
		return inv;
	}
	
	private ItemStack subtractFromItemStack(ItemStack item, int number) {
		if(item.getAmount() - number < 1) {
			return air;
		}else {
			item.setAmount(item.getAmount() - number);
			return item;
		}
	}
	
	@EventHandler
	private void onInventoryClose(InventoryCloseEvent e) {
		Inventory curInv = e.getInventory();
		// If the inventory clicked is a crafting inventory.
		if(openGUIs.containsKey(curInv)) {
			openGUIs.remove(curInv);
			HumanEntity p = e.getPlayer();
			if(curInv.getItem(10) != null)
				p.getInventory().addItem(curInv.getItem(10));
			if(curInv.getItem(13) != null)
				p.getInventory().addItem(curInv.getItem(13));
			if(curInv.getItem(14) != null)
				p.getInventory().addItem(curInv.getItem(14));
			if(curInv.getItem(15) != null)
				p.getInventory().addItem(curInv.getItem(15));
			if(curInv.getItem(28) != null)
				p.getInventory().addItem(curInv.getItem(28));
			if(curInv.getItem(29) != null)
				p.getInventory().addItem(curInv.getItem(29));
			if(curInv.getItem(30) != null)
				p.getInventory().addItem(curInv.getItem(30));
			if(curInv.getItem(31) != null)
				p.getInventory().addItem(curInv.getItem(31));
		}
	}
	
	private ItemStack playerHasItems(Player p, ItemStack item) {
		ItemStack compItem = item.clone();
		compItem.setAmount(1);
		Inventory inv = p.getInventory();
		for(int i = 0; i < inv.getSize(); i++) {
			if(inv.getItem(i) != null) {
				ItemStack curItem = inv.getItem(i).clone();
				curItem.setAmount(1);
				if(curItem.equals(compItem)) {
					return inv.getItem(i);
				}
			}
		}
		return null;
	}
}
