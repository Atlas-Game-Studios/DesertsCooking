package com.atlasmc.desertdweller.cooking.effects;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.ags.atlasessentials.AtlasEssentials;
import com.ags.atlasessentials.systems.playermenu.PlayerStatus;

public class EffectsHandler {
	private static HashMap<Player, ArrayList<CustomEffect>> effectedPlayers = new HashMap<Player, ArrayList<CustomEffect>>();
	
	//Adds the effect, as well as updates it if it is already applied.
	public static void addEffect(Player p, CustomEffect e) {
		ArrayList<CustomEffect> effectList = new ArrayList<CustomEffect>();
		if(effectedPlayers.keySet().contains(p)) {
			effectList = effectedPlayers.get(p);
		}
		for(CustomEffect curEffect : effectList){
			if(curEffect.getEffect().equals(e.getEffect())) {
				curEffect = e;
				AtlasEssentials.instance().updateStatusEffect(p, e.getEffect().toString(), new PlayerStatus(e.getName(), e.getDesc(), e.getDuration(), e.getTier(), e.getIcon()));
				return;
			}
		}
		effectList.add(e);
		AtlasEssentials.instance().updateStatusEffect(p, e.getEffect().toString(), new PlayerStatus(e.getName(), e.getDesc(), e.getDuration(), e.getTier(), e.getIcon()));
	}
	
	public static void removeEffect(Player p, CustomEffectType e) {
		ArrayList<CustomEffect> effectList = new ArrayList<CustomEffect>();
		if(effectedPlayers.keySet().contains(p)) {
			effectList = effectedPlayers.get(p);
		}else return;
		CustomEffect targetEffect = null;
		for(CustomEffect curEffect : effectList){
			if(curEffect.getEffect().equals(e)) {
				targetEffect = curEffect;
			}
		}
		if(targetEffect != null)
			AtlasEssentials.instance().updateStatusEffect(p, targetEffect.getEffect().toString(), new PlayerStatus(targetEffect.getName(), targetEffect.getDesc(), 0, targetEffect.getTier(), targetEffect.getIcon()));
			effectList.remove(targetEffect);
	}
	
	public static CustomEffect getEffect(Player p, CustomEffectType e) {
		ArrayList<CustomEffect> effectList = new ArrayList<CustomEffect>();
		if(effectedPlayers.keySet().contains(p)) {
			effectList = effectedPlayers.get(p);
		}else return null;
		for(CustomEffect curEffect : effectList){
			if(curEffect.getEffect().equals(e)) {
				return curEffect;
			}
		}
		return null;
	}
	
	public static void tickAllEffects() {
		//updatedEffects helps remove all expired effects and lists from effectedPlayers.
		HashMap<Player, ArrayList<CustomEffect>> updatedEffects = new HashMap<Player, ArrayList<CustomEffect>>();
		for(Player p : effectedPlayers.keySet()) {
			ArrayList<CustomEffect> updatedList = new ArrayList<CustomEffect>();
			if(!p.isOnline())
				return;
			for(CustomEffect e : effectedPlayers.get(p)) {
				e.tickEffect();
				AtlasEssentials.instance().updateStatusEffect(p, e.getEffect().toString(), new PlayerStatus(e.getName(), e.getDesc(), e.getDuration(), e.getTier(), e.getIcon()));
				if(e.duration != 0)
					updatedList.add(e);
			}
			if(updatedList.size() != 0)
				updatedEffects.put(p, updatedList);
		}
		
		effectedPlayers = updatedEffects;
	}
}
