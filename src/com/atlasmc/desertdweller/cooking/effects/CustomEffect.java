package com.atlasmc.desertdweller.cooking.effects;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CustomEffect {
	protected Player player;
	protected int duration;
	protected EffectType type;
	protected CustomEffectType effect;
	protected int tier;
	protected String name;
	protected String desc;
	protected Material icon;
	
	public void tickEffect() {
		
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public EffectType getType() {
		return type;
	}
	
	public void setType(EffectType type) {
		this.type = type;
	}
	
	public int getTier() {
		return tier;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}

	public CustomEffectType getEffect() {
		return effect;
	}
	
	public String getDesc() {
		return desc;
	}

	public String getName() {
		return name;
	}
	
	public Material getIcon() {
		return icon;
	}
	
	public Player getPlayer() {
		return player;
	}
}
