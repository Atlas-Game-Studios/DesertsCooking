package com.atlasmc.desertdweller.cooking.effects;

import org.bukkit.entity.Player;

public class ExpGainOverTime extends CustomEffect {
	private int ticksPassed = 0;
	private int secsPassed = 0;

	public ExpGainOverTime(int duration, int tier, Player p) {
		this.player = p;
		this.duration = duration;
		this.effect = CookingEffect.EXPGAINOVERTIME;
		this.type = EffectType.BUFF;
		this.tier = tier;
		this.name = "expGainOverTime";
		switch(tier){
		case 1:
			this.desc = "You gain 1 xp point per 2 seconds.";
			break;
		case 2:
			this.desc = "You gain 2 xp points per 3 seconds.";
			break;
		case 3:
			this.desc = "You gain 1 xp points per 1 second.";
			break;
		}
	}

	public ExpGainOverTime(int tier, Player p) {
		this.player = p;
		this.duration = 60;
		this.effect = CookingEffect.EXPGAINOVERTIME;
		this.type = EffectType.BUFF;
		this.tier = tier;
		this.name = "expGainOverTime";
		switch(tier){
		case 1:
			this.desc = "You gain 1 xp point per 2 seconds.";
			break;
		case 2:
			this.desc = "You gain 2 xp points per 3 seconds.";
			break;
		case 3:
			this.desc = "You gain 1 xp points per 1 second.";
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
				if(secsPassed >= 2) {
					this.player.setExp(this.player.getExp() + 1);
					secsPassed = 0;
				}
				break;
			case 2:
				if(secsPassed >= 3) {
					this.player.setExp(this.player.getExp() + 2);
					secsPassed = 0;
				}
				break;
			case 3:
				if(secsPassed >= 1) {
					this.player.setExp(this.player.getExp() + 1);
					secsPassed = 0;
				}
				break;
			}
		}else {
			ticksPassed += 1;
		}
	}
}
