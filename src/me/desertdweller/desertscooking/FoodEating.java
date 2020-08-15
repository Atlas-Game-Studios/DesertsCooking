package me.desertdweller.desertscooking;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.atlasmc.eatingapi.EatingAPI;

import de.tr7zw.nbtapi.NBTItem;





public class FoodEating implements Listener{
	
	@EventHandler
	private void onEat(PlayerItemConsumeEvent e) {
		ItemStack eatenItem = e.getItem();
		NBTItem nbti = new NBTItem(eatenItem);
		if(nbti.hasKey("Plugin") && nbti.getString("Plugin").equals("DesertsCooking") && e.getPlayer().hasPermission("cooking.eat")) {
			CustomFoodItem item = new CustomFoodItem(eatenItem);
			if(item.completed) {
				e.setCancelled(true);
				CustomFoodEatenEvent event = new CustomFoodEatenEvent(e.getItem(), e.getPlayer());
				Bukkit.getServer().getPluginManager().callEvent(event);
				if(event.isCancelled())
					return;
				float modifier = item.flavor.efficiency(e.getPlayer());
				if(item.experience < 20) {
					EatingAPI.consumeEdibleFood(e.getPlayer(), item.getItemStack(), (int) (item.food * modifier), item.saturation * modifier, (int) (item.experience * modifier)); 
				}else {
					EatingAPI.consumeEdibleFood(e.getPlayer(), item.getItemStack(), (int) (item.food * modifier), item.saturation * modifier, (int) (20 * modifier));
				}
				if(item.poisoned) {
					e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON,1200,4));
				}
				item.flavor.eatenPlayerMessage(e.getPlayer());
				if(item.prevItem.getAmount() <= 1) {
					ItemStack newItem = new ItemStack(Material.AIR);
					if(e.getPlayer().getInventory().getItemInMainHand().equals(eatenItem)) { 
						e.getPlayer().getInventory().setItemInMainHand(newItem);
					}else if(e.getPlayer().getInventory().getItemInOffHand().equals(eatenItem)) {
						e.getPlayer().getInventory().setItemInOffHand(newItem);
					}
				}else {
					if(e.getPlayer().getInventory().getItemInMainHand().equals(eatenItem)) { 
						eatenItem.setAmount(eatenItem.getAmount() - 1);
						e.getPlayer().getInventory().setItemInMainHand(eatenItem);
					}else if(e.getPlayer().getInventory().getItemInOffHand().equals(eatenItem)) {
						eatenItem.setAmount(eatenItem.getAmount() - 1);
						e.getPlayer().getInventory().setItemInOffHand(eatenItem);
					}
				}
			}else if(!item.invalidItem) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.RED + "That item is not completed! Put it into an oven, stove or cooking pot to finish it.");
			}else if(item.invalidItem) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.GOLD + "This item is invalid, please report this to an Admin.");
			}
		}
	}
	
	@EventHandler
	private void onRightClick(PlayerInteractEvent e) {
		if(e.hasItem() && !e.getItem().getType().equals(Material.AIR) && !e.getItem().getType().isEdible() && (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
			ItemStack usedItem = e.getItem();
			NBTItem nbti = new NBTItem(usedItem);
			if(nbti.hasKey("Plugin") && nbti.getString("Plugin").equals("DesertsCooking")) {
				CustomFoodItem item = new CustomFoodItem(usedItem);
				if(item.completed && e.getPlayer().getFoodLevel() != 20 ) {
					boolean mainhand = false;
					if(e.getPlayer().getInventory().getItemInMainHand().equals(usedItem))
						mainhand = true;
					
					float modifier = item.flavor.efficiency(e.getPlayer());
					e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW,30,3));
					e.setCancelled(true);
					CustomFoodEatenEvent event = new CustomFoodEatenEvent(e.getItem(), e.getPlayer());
					Bukkit.getServer().getPluginManager().callEvent(event);
					if(event.isCancelled())
						return;
					EatingAPI.consumeFood(e.getPlayer(), item.getItemStack(), (int) (item.food * modifier), item.saturation * modifier, (int) (item.experience * modifier)); 
					if(item.poisoned) {
						e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON,1200,4));
					}
					item.flavor.eatenPlayerMessage(e.getPlayer());
					if(usedItem.getAmount() <= 1) {
						ItemStack newItem = new ItemStack(Material.AIR);
						if(mainhand) { 
							e.getPlayer().getInventory().setItemInMainHand(newItem);
						}else {
							e.getPlayer().getInventory().setItemInOffHand(newItem);
						}
					}else {
						if(mainhand) { 
							usedItem.setAmount(usedItem.getAmount() - 1);
							e.getPlayer().getInventory().setItemInMainHand(usedItem);
						}else {
							usedItem.setAmount(usedItem.getAmount() - 1);
							e.getPlayer().getInventory().setItemInOffHand(usedItem);
						}
					}
				}else if(!item.invalidItem && !item.completed) {
					e.setCancelled(true);
					e.getPlayer().sendMessage(ChatColor.GRAY + "That item is not completed! Put it into an oven, stove or boiler to finish it.");
				}else if(item.invalidItem) {
					e.setCancelled(true);
					e.getPlayer().sendMessage(ChatColor.GOLD + "This item is invalid, please report this to an Admin.");
				}
			}
		}
	}
}
