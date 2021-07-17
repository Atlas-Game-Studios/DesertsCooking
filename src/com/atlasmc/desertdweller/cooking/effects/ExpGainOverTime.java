package com.atlasmc.desertdweller.cooking.effects;

public class ExpGainOverTime extends CustomEffect {

	public ExpGainOverTime(int duration, int tier) {
		this.duration = duration;
		this.effect = CookingEffect.EXPGAINOVERTIME;
		this.type = EffectType.BUFF;
		
	}
}
