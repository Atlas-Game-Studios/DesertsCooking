package com.atlasmc.desertdweller.cooking.effects;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DebuffRemoval extends CustomEffect {
	private int ticksPassed = 0;
	private int secsPassed = 0;

	public DebuffRemoval(int duration, int tier, Player p) {
		this.player = p;
		this.duration = duration;
		this.effect = CustomEffectType.DEBUFFREMOVAL;
		this.type = EffectType.BUFF;
		this.tier = tier;
		this.name = "debuffRemoval";
		switch(tier){
		case 1:
			this.desc = "Every 10 seconds, each debuff applied to you will have a 20% chance to be removed.";
			break;
		case 2:
			this.desc = "Every 6 seconds, each debuff applied to you will have a 20% chance to be removed.";
			break;
		case 3:
			this.desc = "Every 3 seconds, each debuff applied to you will have a 15% chance to be removed.";
			break;
		}
	}

	public DebuffRemoval(int tier, Player p) {
		this.player = p;
		this.duration = 60;
		this.effect = CustomEffectType.DEBUFFREMOVAL;
		this.type = EffectType.BUFF;
		this.tier = tier;
		this.name = "debuffRemoval";
		switch(tier){
		case 1:
			this.desc = "Every 10 seconds, each debuff applied to you will have a 20% chance to be removed.";
			break;
		case 2:
			this.desc = "Every 6 seconds, each debuff applied to you will have a 20% chance to be removed.";
			break;
		case 3:
			this.desc = "Every 3 seconds, each debuff applied to you will have a 15% chance to be removed.";
			break;
		}
	}
	
	@Override
	public void tickEffect() {
		if(ticksPassed >= 20) {
			duration -= 1;
			secsPassed += 1;
			switch(tier) {
			case 1:
				if(secsPassed >= 10) {
					tryToRemoveDebuffs(this.player);
					secsPassed = 0;
				}
				break;
			case 2:
				if(secsPassed >= 6) {
					tryToRemoveDebuffs(this.player);
					secsPassed = 0;
				}
				break;
			case 3:
				if(secsPassed >= 3) {
					tryToRemoveDebuffs(this.player);
					secsPassed = 0;
				}
				break;
			}
		}else {
			ticksPassed += 1;
		}
	}
	
	private void tryToRemoveDebuffs(Player p) {
		ArrayList<PotionEffect> effectsToRemove = new ArrayList<PotionEffect>();
		for(PotionEffect effect : p.getActivePotionEffects()) {
			if(effect.getType().equals(PotionEffectType.BLINDNESS) || effect.getType().equals(PotionEffectType.CONFUSION) || effect.getType().equals(PotionEffectType.GLOWING) 
					|| effect.getType().equals(PotionEffectType.HUNGER) || effect.getType().equals(PotionEffectType.POISON) || effect.getType().equals(PotionEffectType.SLOW) 
					|| effect.getType().equals(PotionEffectType.SLOW_DIGGING) || effect.getType().equals(PotionEffectType.UNLUCK) || effect.getType().equals(PotionEffectType.WEAKNESS) 
					|| effect.getType().equals(PotionEffectType.WITHER)) {
				if(Math.random()*5 == 1)
					effectsToRemove.add(effect);
			}
		}
		for(PotionEffect effect : effectsToRemove) {
			p.removePotionEffect(effect.getType());
		}
	}
}