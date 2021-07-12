package com.atlasmc.desertdweller.cooking.customfood;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Lightable;
import org.bukkit.scheduler.BukkitRunnable;

import com.atlasmc.desertdweller.cooking.Cooking;


public class StationUpdater extends BukkitRunnable{
	private static Cooking plugin = Cooking.getPlugin(Cooking.class);
	private Location location;
	private StationStage stage;

	public StationUpdater(Location location, StationStage stage) {
		this.location = location;
		this.stage = stage;
	}
	
	
	
	@Override
	public void run() {
		switch(stage) {
		case IDLE:
			updateToCooking1();
			break;
		case COOKING1:
			updateToCooking2();
			break;
		case COOKING2:
			updateToCooking3();
			break;
		case COOKING3:
			updateToCooling();
			break;
		case COOLING:
			updateToIdle();
			break;
		}
	}

	private void updateToCooking1() {
		Lightable campfire;
		Levelled cauldron;
		Block b = location.getBlock();

		BlockFace facing = Station.getStationFacing(b);
		
		if(facing == null) {
			breakStation();
			return;
		}
		
		switch(facing) {
		case NORTH: 
			if(!b.getRelative(BlockFace.EAST).getType().equals(Material.TRIPWIRE_HOOK) || !b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON) || !b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
				breakStation();
				return;
			}
			campfire = (Lightable) b.getRelative(BlockFace.DOWN);
			campfire.setLit(true);
			cauldron = (Levelled) b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN);
			cauldron.setLevel(1);
			break;
		case EAST:
			if(!b.getRelative(BlockFace.SOUTH).getType().equals(Material.TRIPWIRE_HOOK) || !b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON) || !b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
				breakStation();
				return;
			}
			campfire = (Lightable) b.getRelative(BlockFace.DOWN);
			campfire.setLit(true);
			cauldron = (Levelled) b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN);
			cauldron.setLevel(1);
			break;
		case SOUTH:
			if(!b.getRelative(BlockFace.WEST).getType().equals(Material.TRIPWIRE_HOOK) || !b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON) || !b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
				breakStation();
				return;
			}
			campfire = (Lightable) b.getRelative(BlockFace.DOWN);
			campfire.setLit(true);
			cauldron = (Levelled) b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN);
			cauldron.setLevel(1);
			break;
		case WEST:
			if(!b.getRelative(BlockFace.NORTH).getType().equals(Material.TRIPWIRE_HOOK) || !b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON) || !b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
				breakStation();
				return;
			}
			campfire = (Lightable) b.getRelative(BlockFace.DOWN);
			campfire.setLit(true);
			cauldron = (Levelled) b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN);
			cauldron.setLevel(1);
			break;
		default:
			return;
		}
		

		StationUpdater updater = new StationUpdater(location, StationStage.COOKING1);
		updater.runTaskLater(plugin, 20);
		return;
	}
	
	private void updateToCooking2() {
		Lightable smoker;
		Levelled cauldron;
		Block b = location.getBlock();

		BlockFace facing = Station.getStationFacing(b);
		
		if(facing == null) {
			breakStation();
			return;
		}
		
		switch(facing) {
		case NORTH: 
			if(!b.getRelative(BlockFace.EAST).getType().equals(Material.TRIPWIRE_HOOK) || !b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON) || !b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
				breakStation();
				return;
			}
			smoker = (Lightable) b;
			smoker.setLit(true);
			cauldron = (Levelled) b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN);
			cauldron.setLevel(2);
			break;
		case EAST:
			if(!b.getRelative(BlockFace.SOUTH).getType().equals(Material.TRIPWIRE_HOOK) || !b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON) || !b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
				breakStation();
				return;
			}
			smoker = (Lightable) b;
			smoker.setLit(true);
			cauldron = (Levelled) b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN);
			cauldron.setLevel(2);
			break;
		case SOUTH:
			if(!b.getRelative(BlockFace.WEST).getType().equals(Material.TRIPWIRE_HOOK) || !b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON) || !b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
				breakStation();
				return;
			}
			smoker = (Lightable) b;
			smoker.setLit(true);
			cauldron = (Levelled) b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN);
			cauldron.setLevel(2);
			break;
		case WEST:
			if(!b.getRelative(BlockFace.NORTH).getType().equals(Material.TRIPWIRE_HOOK) || !b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON) || !b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
				breakStation();
				return;
			}
			smoker = (Lightable) b;
			smoker.setLit(true);
			cauldron = (Levelled) b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN);
			cauldron.setLevel(2);
			break;
		default:
			return;
		}
		
		
		StationUpdater updater = new StationUpdater(location, StationStage.COOKING2);
		updater.runTaskLater(plugin, 20);
	}
	
	private void updateToCooking3() {
		Levelled cauldron;
		Block b = location.getBlock();

		BlockFace facing = Station.getStationFacing(b);
		
		if(facing == null) {
			breakStation();
			return;
		}
		
		switch(facing) {
		case NORTH: 
			if(!b.getRelative(BlockFace.EAST).getType().equals(Material.TRIPWIRE_HOOK) || !b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON) || !b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
				breakStation();
				return;
			}
			cauldron = (Levelled) b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN);
			cauldron.setLevel(2);
			break;
		case EAST:
			if(!b.getRelative(BlockFace.SOUTH).getType().equals(Material.TRIPWIRE_HOOK) || !b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON) || !b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
				breakStation();
				return;
			}
			cauldron = (Levelled) b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN);
			cauldron.setLevel(2);
			break;
		case SOUTH:
			if(!b.getRelative(BlockFace.WEST).getType().equals(Material.TRIPWIRE_HOOK) || !b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON) || !b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
				breakStation();
				return;
			}
			cauldron = (Levelled) b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN);
			cauldron.setLevel(2);
			break;
		case WEST:
			if(!b.getRelative(BlockFace.NORTH).getType().equals(Material.TRIPWIRE_HOOK) || !b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON) || !b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
				breakStation();
				return;
			}
			cauldron = (Levelled) b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN);
			cauldron.setLevel(3);
			break;
		default:
			return;
		}
		
		
		StationUpdater updater = new StationUpdater(location, StationStage.COOKING3);
		updater.runTaskLater(plugin, 320);
	}
	
	private void updateToCooling() {
		Lightable smoker;
		Levelled cauldron;
		Block b = location.getBlock();

		BlockFace facing = Station.getStationFacing(b);
		
		if(facing == null) {
			breakStation();
			return;
		}
		
		switch(facing) {
		case NORTH: 
			if(!b.getRelative(BlockFace.EAST).getType().equals(Material.TRIPWIRE_HOOK) || !b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON) || !b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
				breakStation();
				return;
			}
			smoker = (Lightable) b;
			smoker.setLit(false);
			cauldron = (Levelled) b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN);
			cauldron.setLevel(1);
			break;
		case EAST:
			if(!b.getRelative(BlockFace.SOUTH).getType().equals(Material.TRIPWIRE_HOOK) || !b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON) || !b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
				breakStation();
				return;
			}
			smoker = (Lightable) b;
			smoker.setLit(false);
			cauldron = (Levelled) b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN);
			cauldron.setLevel(1);
			break;
		case SOUTH:
			if(!b.getRelative(BlockFace.WEST).getType().equals(Material.TRIPWIRE_HOOK) || !b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON) || !b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
				breakStation();
				return;
			}
			smoker = (Lightable) b;
			smoker.setLit(false);
			cauldron = (Levelled) b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN);
			cauldron.setLevel(1);
			break;
		case WEST:
			if(!b.getRelative(BlockFace.NORTH).getType().equals(Material.TRIPWIRE_HOOK) || !b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON) || !b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
				breakStation();
				return;
			}
			smoker = (Lightable) b;
			smoker.setLit(false);
			cauldron = (Levelled) b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN);
			cauldron.setLevel(1);
			break;
		default:
			return;
		}
		
		
		StationUpdater updater = new StationUpdater(location, StationStage.COOLING);
		updater.runTaskLater(plugin, 20);
	}
	
	private void updateToIdle() {
		Lightable campfire;
		Levelled cauldron;
		Block b = location.getBlock();

		BlockFace facing = Station.getStationFacing(b);
		
		if(facing == null) {
			breakStation();
			return;
		}
		
		switch(facing) {
		case NORTH: 
			if(!b.getRelative(BlockFace.EAST).getType().equals(Material.TRIPWIRE_HOOK) || !b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON) || !b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
				breakStation();
				return;
			}
			campfire = (Lightable) b.getRelative(BlockFace.DOWN);
			campfire.setLit(false);
			cauldron = (Levelled) b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN);
			cauldron.setLevel(0);
			break;
		case EAST:
			if(!b.getRelative(BlockFace.SOUTH).getType().equals(Material.TRIPWIRE_HOOK) || !b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON) || !b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
				breakStation();
				return;
			}
			campfire = (Lightable) b.getRelative(BlockFace.DOWN);
			campfire.setLit(false);
			cauldron = (Levelled) b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN);
			cauldron.setLevel(0);
			break;
		case SOUTH:
			if(!b.getRelative(BlockFace.WEST).getType().equals(Material.TRIPWIRE_HOOK) || !b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON) || !b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
				breakStation();
				return;
			}
			campfire = (Lightable) b.getRelative(BlockFace.DOWN);
			campfire.setLit(false);
			cauldron = (Levelled) b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN);
			cauldron.setLevel(0);
			break;
		case WEST:
			if(!b.getRelative(BlockFace.NORTH).getType().equals(Material.TRIPWIRE_HOOK) || !b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).getType().equals(Material.CAULDRON) || !b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).getType().equals(Material.BARREL)) {
				breakStation();
				return;
			}
			campfire = (Lightable) b.getRelative(BlockFace.DOWN);
			campfire.setLit(false);
			cauldron = (Levelled) b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN);
			cauldron.setLevel(0);
			break;
		default:
			return;
		}
		
		Station.removeStation(location);
		
	}
	
	private void breakStation() {
		Station station = Station.getActiveStation(location);
		location.getWorld().dropItemNaturally(location, station.getCookingItem().getItemStack());
		Station.removeStation(location);
	}
}

