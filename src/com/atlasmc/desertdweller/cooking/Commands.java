package com.atlasmc.desertdweller.cooking;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.atlasmc.desertdweller.cooking.customfood.CustomFoodItem;
import com.atlasmc.desertdweller.cooking.customfood.Flavor;

import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("tastes") && sender instanceof Player) {
			Player p = (Player) sender;
			Flavor flavor = new Flavor();
			p.sendMessage(flavor.playerPreferencesMessage(p));
			return true;
		}else if(cmd.getName().equalsIgnoreCase("foodstats") && sender instanceof Player) {
			Player p = (Player) sender;
			if(sender.isOp()) {
				ItemStack item = p.getInventory().getItemInMainHand();
				p.sendMessage(ChatColor.GOLD + "Food value: " + new CustomFoodItem(item).food);
				p.sendMessage(ChatColor.GOLD + "Saturation value: " + new CustomFoodItem(item).saturation);
				p.sendMessage(ChatColor.GOLD + "Experience value: " + new CustomFoodItem(item).experience);
				p.sendMessage(ChatColor.GOLD + "Your efficiency: " + new CustomFoodItem(item).flavor.efficiency(Cooking.preferences.get(p.getUniqueId())));
				
				return true;
			}else {
				p.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			}
			
		}
		return false;
	}
}
	
