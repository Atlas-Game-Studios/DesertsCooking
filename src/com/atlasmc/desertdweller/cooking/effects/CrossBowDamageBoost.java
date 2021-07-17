package com.atlasmc.desertdweller.cooking.effects;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class CrossBowDamageBoost extends CustomEffect implements Listener{

	public CrossBowDamageBoost(int duration, int tier, Player p) {
		this.player = p;
		this.duration = duration;
		this.effect = CookingEffect.CROSSBOWDAMAGEBOOST;
		this.type = EffectType.BUFF;
		this.tier = tier;
		this.name = "crossBowDamageBoost";
		switch(tier){
		case 1:
			this.desc = "Damage done by crossbows is increased by 10%.";
			break;
		case 2:
			this.desc = "Damage done by crossbows is increased by 15%.";
			break;
		case 3:
			this.desc = "Damage done by crossbows is increased by 20%.";
			break;
		}
	}
	

	public CrossBowDamageBoost(int tier, Player p) {
		this.player = p;
		this.duration = 20;
		this.effect = CookingEffect.CROSSBOWDAMAGEBOOST;
		this.type = EffectType.BUFF;
		this.tier = tier;
		this.name = "bowDamageBoost";
		switch(tier){
		case 1:
			this.desc = "Damage done by crossbows is increased by 10%.";
			break;
		case 2:
			this.desc = "Damage done by crossbows is increased by 15%.";
			break;
		case 3:
			this.desc = "Damage done by crossbows is increased by 20%.";
			break;
		}
	}
	
	@EventHandler
	public static void onPlayerDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if(!e.getCause().equals(DamageCause.PROJECTILE))
				return;
			Material item = p.getInventory().getItemInMainHand().getType();
			if(!item.equals(Material.BOW))
				return;
			if(EffectsHandler.getEffect(p, CookingEffect.CROSSBOWDAMAGEBOOST) != null) {
				switch(EffectsHandler.getEffect(p, CookingEffect.CROSSBOWDAMAGEBOOST).tier){
				case 1:
					e.setDamage(e.getDamage()*1.1);
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