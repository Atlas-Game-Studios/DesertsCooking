package com.atlasmc.desertdweller.cooking.effects;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class TridentDamageBoost extends CustomEffect implements Listener{

	public TridentDamageBoost(int duration, int tier, Player p) {
		this.player = p;
		this.duration = duration;
		this.effect = CustomEffectType.TRIDENTDAMAGEBOOST;
		this.type = EffectType.BUFF;
		this.tier = tier;
		this.name = "tridentDamageBoost";
		switch(tier){
		case 1:
			this.desc = "Damage done by tridents is increased by 5%.";
			break;
		case 2:
			this.desc = "Damage done by tridents is increased by 15%.";
			break;
		case 3:
			this.desc = "Damage done by tridents is increased by 20%.";
			break;
		}
	}
	
	public TridentDamageBoost(int tier, Player p) {
		this.player = p;
		this.duration = 30;
		this.effect = CustomEffectType.TRIDENTDAMAGEBOOST;
		this.type = EffectType.BUFF;
		this.tier = tier;
		this.name = "tridentDamageBoost";
		switch(tier){
		case 1:
			this.desc = "Damage done by tridents is increased by 5%.";
			break;
		case 2:
			this.desc = "Damage done by tridents is increased by 15%.";
			break;
		case 3:
			this.desc = "Damage done by tridents is increased by 20%.";
			break;
		}
	}
	
	@EventHandler
	public static void onPlayerDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if(!e.getCause().equals(DamageCause.ENTITY_ATTACK) && !e.getCause().equals(DamageCause.ENTITY_SWEEP_ATTACK))
				return;
			if(!p.getInventory().getItemInMainHand().getType().equals(Material.TRIDENT)) 
				return;
			if(EffectsHandler.getEffect(p, CustomEffectType.TRIDENTDAMAGEBOOST) != null) {
				switch(EffectsHandler.getEffect(p, CustomEffectType.TRIDENTDAMAGEBOOST).tier){
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
