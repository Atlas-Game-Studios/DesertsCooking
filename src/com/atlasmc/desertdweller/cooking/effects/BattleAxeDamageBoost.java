package com.atlasmc.desertdweller.cooking.effects;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class BattleAxeDamageBoost extends CustomEffect implements Listener{

	public BattleAxeDamageBoost(int duration, int tier, Player p) {
		this.player = p;
		this.duration = duration;
		this.effect = CookingEffect.BATTLEAXEDAMAGEBOOST;
		this.type = EffectType.BUFF;
		this.tier = tier;
		this.name = "battleAxeDamageBoost";
		switch(tier){
		case 1:
			this.desc = "Damage done by axes is increased by 5%.";
			break;
		case 2:
			this.desc = "Damage done by axes is increased by 15%.";
			break;
		case 3:
			this.desc = "Damage done by axes is increased by 20%.";
			break;
		}
	}
	
	public BattleAxeDamageBoost(int tier, Player p) {
		this.player = p;
		this.duration = 30;
		this.effect = CookingEffect.BATTLEAXEDAMAGEBOOST;
		this.type = EffectType.BUFF;
		this.tier = tier;
		this.name = "battleAxeDamageBoost";
		switch(tier){
		case 1:
			this.desc = "Damage done by axes is increased by 5%.";
			break;
		case 2:
			this.desc = "Damage done by axes is increased by 15%.";
			break;
		case 3:
			this.desc = "Damage done by axes is increased by 20%.";
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
			if(!item.equals(Material.DIAMOND_AXE) && !item.equals(Material.IRON_AXE) &&
					!item.equals(Material.STONE_AXE) && !item.equals(Material.GOLDEN_AXE) &&
					!item.equals(Material.WOODEN_AXE) && !item.equals(Material.NETHERITE_AXE)) 
				return;
			if(EffectsHandler.getEffect(p, CookingEffect.BATTLEAXEDAMAGEBOOST) != null) {
				switch(EffectsHandler.getEffect(p, CookingEffect.BATTLEAXEDAMAGEBOOST).tier){
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
