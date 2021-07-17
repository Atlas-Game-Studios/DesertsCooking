package com.atlasmc.desertdweller.cooking.effects;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class SickleDamageBoost extends CustomEffect implements Listener{

	public SickleDamageBoost(int duration, int tier, Player p) {
		this.player = p;
		this.duration = duration;
		this.effect = CustomEffectType.SICKLEDAMAGEBOOST;
		this.type = EffectType.BUFF;
		this.tier = tier;
		this.name = "sickleDamageBoost";
		switch(tier){
		case 1:
			this.desc = "Damage done by sickles is increased by 5%.";
			break;
		case 2:
			this.desc = "Damage done by sickles is increased by 15%.";
			break;
		case 3:
			this.desc = "Damage done by sickles is increased by 20%.";
			break;
		}
	}
	
	public SickleDamageBoost(int tier, Player p) {
		this.player = p;
		this.duration = 30;
		this.effect = CustomEffectType.SICKLEDAMAGEBOOST;
		this.type = EffectType.BUFF;
		this.tier = tier;
		this.name = "sickleDamageBoost";
		switch(tier){
		case 1:
			this.desc = "Damage done by sickles is increased by 5%.";
			break;
		case 2:
			this.desc = "Damage done by sickles is increased by 15%.";
			break;
		case 3:
			this.desc = "Damage done by sickles is increased by 20%.";
			break;
		}
	}
	
	@EventHandler
	public static void onPlayerDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if(!e.getCause().equals(DamageCause.ENTITY_ATTACK) && !e.getCause().equals(DamageCause.ENTITY_SWEEP_ATTACK))
				return;
			Material item = p.getInventory().getItemInMainHand().getType();
			if(!item.equals(Material.DIAMOND_HOE) && !item.equals(Material.IRON_HOE) &&
					!item.equals(Material.STONE_HOE) && !item.equals(Material.GOLDEN_HOE) &&
					!item.equals(Material.WOODEN_HOE) && !item.equals(Material.NETHERITE_HOE)) 
				return;
			if(EffectsHandler.getEffect(p, CustomEffectType.SICKLEDAMAGEBOOST) != null) {
				switch(EffectsHandler.getEffect(p, CustomEffectType.SICKLEDAMAGEBOOST).tier){
				case 1:
					e.setDamage(e.getDamage()*1.05);
					break;
				case 2:
					e.setDamage(e.getDamage()*1.15);
					break;
				case 3:
					e.setDamage(e.getDamage()*1.20);
					break;
				}
			}
		}
	}
}