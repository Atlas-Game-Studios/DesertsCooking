package com.atlasmc.desertdweller.cooking;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import com.atlasmc.desertdweller.cooking.customfood.Station;

public class FurnaceLighter extends BukkitRunnable{
	private static Cooking plugin = Cooking.getPlugin(Cooking.class);
	
	@Override
	public void run() {
		try {
		PreparedStatement statement = (PreparedStatement) plugin.getConnection().prepareStatement("SELECT location FROM " + plugin.getConfig().getString("tablename1"));
		ResultSet station;
			station = statement.executeQuery();
			while(station.next()) {
				Block b = Station.deserializeLocation(station.getString(1)).getBlock();
				Location loc = b.getLocation();
				if(loc.getWorld().isChunkLoaded(loc.getBlockX()/16, loc.getBlockY()/16)) {
					Station.furnaceLight(b, Station.getStationType(b));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
