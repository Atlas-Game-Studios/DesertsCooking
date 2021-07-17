package com.atlasmc.desertdweller.cooking;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.atlasmc.desertdweller.cooking.customfood.CustomFoodItem;
import com.atlasmc.desertdweller.cooking.customfood.Flavor;
import com.atlasmc.desertdweller.cooking.effects.BattleAxeDamageBoost;
import com.atlasmc.desertdweller.cooking.effects.BowDamageBoost;
import com.atlasmc.desertdweller.cooking.effects.CrossBowDamageBoost;
import com.atlasmc.desertdweller.cooking.effects.CustomEffectType;
import com.atlasmc.desertdweller.cooking.effects.DebuffRemoval;
import com.atlasmc.desertdweller.cooking.effects.EffectsHandler;
import com.atlasmc.desertdweller.cooking.effects.ExpGainOverTime;
import com.atlasmc.desertdweller.cooking.effects.MeleeDamageBoost;
import com.atlasmc.desertdweller.cooking.effects.PassiveRegen;
import com.atlasmc.desertdweller.cooking.effects.SickleDamageBoost;
import com.atlasmc.desertdweller.cooking.effects.SwordDamageBoost;
import com.atlasmc.desertdweller.cooking.effects.TridentDamageBoost;

import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor {
	private static Plugin plugin = Cooking.getPlugin(Cooking.class);

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
				p.sendMessage(ChatColor.GOLD + "Food value: " + new CustomFoodItem(item).getFood());
				p.sendMessage(ChatColor.GOLD + "Saturation value: " + new CustomFoodItem(item).getSaturation());
				p.sendMessage(ChatColor.GOLD + "Experience value: " + new CustomFoodItem(item).getExperience());
				p.sendMessage(ChatColor.GOLD + "Your efficiency: " + new CustomFoodItem(item).getFlavor().efficiency(Cooking.preferences.get(p.getUniqueId())));
				
				return true;
			}else {
				p.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			}
			
		}else if(cmd.getName().equalsIgnoreCase("customeffect") && sender.isOp()){
			if(args.length < 2)
				return false;
			Player p = plugin.getServer().getPlayer(args[0]);
			if(CustomEffectType.valueOf(args[1].toUpperCase()) != null) {
				CustomEffectType type = CustomEffectType.valueOf(args[1].toUpperCase());
				switch(type) {
				case EXPGAINOVERTIME:
					if(args.length == 4) {
						EffectsHandler.addEffect(p, new ExpGainOverTime(Integer.parseInt(args[3]), Integer.parseInt(args[2]), p));
					}else {
						EffectsHandler.addEffect(p, new ExpGainOverTime(Integer.parseInt(args[2]), p));
					}
					break;
				case MELEEDAMAGEBOOST:
					if(args.length == 4) {
						EffectsHandler.addEffect(p, new MeleeDamageBoost(Integer.parseInt(args[3]), Integer.parseInt(args[2]), p));
					}else {
						EffectsHandler.addEffect(p, new MeleeDamageBoost(Integer.parseInt(args[2]), p));
					}
					break;
				case TRIDENTDAMAGEBOOST:
					if(args.length == 4) {
						EffectsHandler.addEffect(p, new TridentDamageBoost(Integer.parseInt(args[3]), Integer.parseInt(args[2]), p));
					}else {
						EffectsHandler.addEffect(p, new TridentDamageBoost(Integer.parseInt(args[2]), p));
					}
					break;
				case SWORDDAMAGEBOOST:
					if(args.length == 4) {
						EffectsHandler.addEffect(p, new SwordDamageBoost(Integer.parseInt(args[3]), Integer.parseInt(args[2]), p));
					}else {
						EffectsHandler.addEffect(p, new SwordDamageBoost(Integer.parseInt(args[2]), p));
					}
					break;
				case BATTLEAXEDAMAGEBOOST:
					if(args.length == 4) {
						EffectsHandler.addEffect(p, new BattleAxeDamageBoost(Integer.parseInt(args[3]), Integer.parseInt(args[2]), p));
					}else {
						EffectsHandler.addEffect(p, new BattleAxeDamageBoost(Integer.parseInt(args[2]), p));
					}
					break;
				case SICKLEDAMAGEBOOST:
					if(args.length == 4) {
						EffectsHandler.addEffect(p, new SickleDamageBoost(Integer.parseInt(args[3]), Integer.parseInt(args[2]), p));
					}else {
						EffectsHandler.addEffect(p, new SickleDamageBoost(Integer.parseInt(args[2]), p));
					}
					break;
				case BOWDAMAGEBOOST:
					if(args.length == 4) {
						EffectsHandler.addEffect(p, new BowDamageBoost(Integer.parseInt(args[3]), Integer.parseInt(args[2]), p));
					}else {
						EffectsHandler.addEffect(p, new BowDamageBoost(Integer.parseInt(args[2]), p));
					}
					break;
				case PASSIVEREGEN:
					if(args.length == 4) {
						EffectsHandler.addEffect(p, new PassiveRegen(Integer.parseInt(args[3]), Integer.parseInt(args[2]), p));
					}else {
						EffectsHandler.addEffect(p, new PassiveRegen(Integer.parseInt(args[2]), p));
					}
					break;
				case DEBUFFREMOVAL:
					if(args.length == 4) {
						EffectsHandler.addEffect(p, new DebuffRemoval(Integer.parseInt(args[3]), Integer.parseInt(args[2]), p));
					}else {
						EffectsHandler.addEffect(p, new DebuffRemoval(Integer.parseInt(args[2]), p));
					}
					break;
				case CROSSBOWDAMAGEBOOST:
					if(args.length == 4) {
						EffectsHandler.addEffect(p, new CrossBowDamageBoost(Integer.parseInt(args[3]), Integer.parseInt(args[2]), p));
					}else {
						EffectsHandler.addEffect(p, new CrossBowDamageBoost(Integer.parseInt(args[2]), p));
					}
					break;
				default:
					break;
				}
				return true;
			}
		}
		return false;
	}
}
	
