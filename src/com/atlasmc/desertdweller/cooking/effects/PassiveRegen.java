package com.atlasmc.desertdweller.cooking.effects;

import org.bukkit.entity.Player;

public class PassiveRegen extends CustomEffect {
	private int ticksPassed = 0;
	private int secsPassed = 0;

	public PassiveRegen(int duration, int tier, Player p) {
		this.player = p;
		this.duration = duration;
		this.effect = CustomEffectType.PASSIVEREGEN;
		this.type = EffectType.BUFF;
		this.tier = tier;
		this.name = "passiveRegen";
		switch(tier){
		case 1:
			this.desc = "You regenerate 1 health per 5 seconds.";
			break;
		case 2:
			this.desc = "You regenerate 1 health per 4 seconds.";
			break;
		case 3:
			this.desc = "You regenerate 1 health per 3 seconds.";
			break;
		}
	}

	public PassiveRegen(int tier, Player p) {
		this.player = p;
		this.duration = 60;
		this.effect = CustomEffectType.PASSIVEREGEN;
		this.type = EffectType.BUFF;
		this.tier = tier;
		this.name = "passiveRegen";
		switch(tier){
		case 1:
			this.desc = "You regenerate 1 health per 5 seconds.";
			break;
		case 2:
			this.desc = "You regenerate 1 health per 4 seconds.";
			break;
		case 3:
			this.desc = "You regenerate 1 health per 3 seconds.";
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
				if(secsPassed >= 5) {
					this.player.setHealth(this.player.getHealth() + 1);
					secsPassed = 0;
				}
				break;
			case 2:
				if(secsPassed >= 4) {
					this.player.setHealth(this.player.getHealth() + 1);
					secsPassed = 0;
				}
				break;
			case 3:
				if(secsPassed >= 3) {
					this.player.setHealth(this.player.getHealth() + 1);
					secsPassed = 0;
				}
				break;
			}
		}else {
			ticksPassed += 1;
		}
	}
}
