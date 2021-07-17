package com.atlasmc.desertdweller.cooking.effects;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class MeleeDamageBoost extends CustomEffect implements Listener{

	public MeleeDamageBoost(int duration, int tier, Player p) {
		this.player = p;
		this.duration = duration;
		this.effect = CookingEffect.MELEEDAMAGEBOOST;
		this.type = EffectType.BUFF;
		this.tier = tier;
		this.name = "Champion's Might";
		switch(tier){
		case 1:
			this.desc = "General melee damage increased by 10%.";
			break;
		case 2:
			this.desc = "General melee damage increased by 15%.";
			break;
		case 3:
			this.desc = "General melee damage increased by 20%.";
			break;
		}
	}
	
	public MeleeDamageBoost(int tier, Player p) {
		this.player = p;
		this.duration = 20;
		this.effect = CookingEffect.MELEEDAMAGEBOOST;
		this.type = EffectType.BUFF;
		this.tier = tier;
		this.name = "Champion's Might";
		switch(tier){
		case 1:
			this.desc = "General melee damage increased by 10%.";
			break;
		case 2:
			this.desc = "General melee damage increased by 15%.";
			break;
		case 3:
			this.desc = "General melee damage increased by 20%.";
			break;
		}
	}
	
	@EventHandler
	public static void onPlayerDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if(!e.getCause().equals(DamageCause.ENTITY_ATTACK) && !e.getCause().equals(DamageCause.ENTITY_SWEEP_ATTACK))
				return;
			if(EffectsHandler.getEffect(p, CookingEffect.MELEEDAMAGEBOOST) != null) {
				switch(EffectsHandler.getEffect(p, CookingEffect.MELEEDAMAGEBOOST).tier){
				case 1:
					e.setDamage(e.getDamage()*1.10);
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
