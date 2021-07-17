package com.atlasmc.desertdweller.cooking.customfood;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
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
			campfire = (Lightable) b.getRelative(BlockFace.DOWN).getBlockData();
			campfire.setLit(true);
			cauldron = (Levelled) b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).getBlockData();
			cauldron.setLevel(1);
			b.getRelative(BlockFace.DOWN).setBlockData(campfire);
			b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).setBlockData(cauldron);
			break;
		case EAST:
			campfire = (Lightable) b.getRelative(BlockFace.DOWN).getBlockData();
			campfire.setLit(true);
			cauldron = (Levelled) b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).getBlockData();
			cauldron.setLevel(1);
			b.getRelative(BlockFace.DOWN).setBlockData(campfire);
			b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).setBlockData(cauldron);
			break;
		case SOUTH:
			campfire = (Lightable) b.getRelative(BlockFace.DOWN).getBlockData();
			campfire.setLit(true);
			cauldron = (Levelled) b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).getBlockData();
			cauldron.setLevel(1);
			b.getRelative(BlockFace.DOWN).setBlockData(campfire);
			b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).setBlockData(cauldron);
			break;
		case WEST:
			campfire = (Lightable) b.getRelative(BlockFace.DOWN).getBlockData();
			campfire.setLit(true);
			cauldron = (Levelled) b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).getBlockData();
			cauldron.setLevel(1);
			b.getRelative(BlockFace.DOWN).setBlockData(campfire);
			b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).setBlockData(cauldron);
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
			smoker = (Lightable) b.getBlockData();
			smoker.setLit(true);
			cauldron = (Levelled) b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).getBlockData();
			cauldron.setLevel(2);
			b.setBlockData(smoker);
			b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).setBlockData(cauldron);
			break;
		case EAST:
			smoker = (Lightable) b.getBlockData();
			smoker.setLit(true);
			cauldron = (Levelled) b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).getBlockData();
			cauldron.setLevel(2);
			b.setBlockData(smoker);
			b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).setBlockData(cauldron);
			break;
		case SOUTH:
			smoker = (Lightable) b.getBlockData();
			smoker.setLit(true);
			cauldron = (Levelled) b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).getBlockData();
			cauldron.setLevel(2);
			b.setBlockData(smoker);
			b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).setBlockData(cauldron);
			break;
		case WEST:
			smoker = (Lightable) b.getBlockData();
			smoker.setLit(true);
			cauldron = (Levelled) b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).getBlockData();
			cauldron.setLevel(2);
			b.setBlockData(smoker);
			b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).setBlockData(cauldron);
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
			cauldron = (Levelled) b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).getBlockData();
			cauldron.setLevel(3);
			b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).setBlockData(cauldron);
			break;
		case EAST:
			cauldron = (Levelled) b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).getBlockData();
			cauldron.setLevel(3);
			b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).setBlockData(cauldron);
			break;
		case SOUTH:
			cauldron = (Levelled) b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).getBlockData();
			cauldron.setLevel(3);
			b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).setBlockData(cauldron);
			break;
		case WEST:
			cauldron = (Levelled) b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).getBlockData();
			cauldron.setLevel(3);
			b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).setBlockData(cauldron);
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
			smoker = (Lightable) b.getBlockData();
			smoker.setLit(false);
			cauldron = (Levelled) b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).getBlockData();
			cauldron.setLevel(1);
			b.setBlockData(smoker);
			b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).setBlockData(cauldron);
			break;
		case EAST:
			smoker = (Lightable) b.getBlockData();
			smoker.setLit(false);
			cauldron = (Levelled) b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).getBlockData();
			cauldron.setLevel(1);
			b.setBlockData(smoker);
			b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).setBlockData(cauldron);
			break;
		case SOUTH:
			smoker = (Lightable) b.getBlockData();
			smoker.setLit(false);
			cauldron = (Levelled) b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).getBlockData();
			cauldron.setLevel(1);
			b.setBlockData(smoker);
			b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).setBlockData(cauldron);
			break;
		case WEST:
			smoker = (Lightable) b.getBlockData();
			smoker.setLit(false);
			cauldron = (Levelled) b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).getBlockData();
			cauldron.setLevel(1);
			b.setBlockData(smoker);
			b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).setBlockData(cauldron);
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
		ItemFrame frame;
		Block b = location.getBlock();

		BlockFace facing = Station.getStationFacing(b);
		
		if(facing == null) {
			breakStation();
			return;
		}
			Station.getActiveStation(location).getCookingItem().finish();
		
		switch(facing) {
		case NORTH:
			campfire = (Lightable) b.getRelative(BlockFace.DOWN).getBlockData();
			campfire.setLit(false);
			cauldron = (Levelled) b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).getBlockData();
			cauldron.setLevel(0);
			b.getRelative(BlockFace.DOWN).setBlockData(campfire);
			b.getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN).setBlockData(cauldron);
			frame = (ItemFrame) b.getWorld().spawnEntity(b.getRelative(BlockFace.WEST).getLocation(), EntityType.ITEM_FRAME);
			frame.setFacingDirection(BlockFace.UP);
			frame.setVisible(false);
			frame.setItem(Station.getActiveStation(location).getCookingItem().getItemStack());
			break;
		case EAST:
			campfire = (Lightable) b.getRelative(BlockFace.DOWN).getBlockData();
			campfire.setLit(false);
			cauldron = (Levelled) b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).getBlockData();
			cauldron.setLevel(0);
			b.getRelative(BlockFace.DOWN).setBlockData(campfire);
			b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN).setBlockData(cauldron);
			frame = (ItemFrame) b.getWorld().spawnEntity(b.getRelative(BlockFace.NORTH).getLocation(), EntityType.ITEM_FRAME);
			frame.setFacingDirection(BlockFace.UP);
			frame.setVisible(false);
			frame.setItem(Station.getActiveStation(location).getCookingItem().getItemStack());
			break;
		case SOUTH:
			campfire = (Lightable) b.getRelative(BlockFace.DOWN).getBlockData();
			campfire.setLit(false);
			cauldron = (Levelled) b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).getBlockData();
			cauldron.setLevel(0);
			b.getRelative(BlockFace.DOWN).setBlockData(campfire);
			b.getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN).setBlockData(cauldron);
			frame = (ItemFrame) b.getWorld().spawnEntity(b.getRelative(BlockFace.WEST).getLocation(), EntityType.ITEM_FRAME);
			frame.setFacingDirection(BlockFace.UP);
			frame.setVisible(false);
			frame.setItem(Station.getActiveStation(location).getCookingItem().getItemStack());
			break;
		case WEST:
			campfire = (Lightable) b.getRelative(BlockFace.DOWN).getBlockData();
			campfire.setLit(false);
			cauldron = (Levelled) b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).getBlockData();
			cauldron.setLevel(0);
			b.getRelative(BlockFace.DOWN).setBlockData(campfire);
			b.getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN).setBlockData(cauldron);
			frame = (ItemFrame) b.getWorld().spawnEntity(b.getRelative(BlockFace.SOUTH).getLocation(), EntityType.ITEM_FRAME);
			frame.setFacingDirection(BlockFace.UP);
			frame.setVisible(false);
			frame.setItem(Station.getActiveStation(location).getCookingItem().getItemStack());
			break;
		default:
			return;
		}
		
		Station.removeStation(location);
		
	}
	
	private void breakStation() {
		Station station = Station.getActiveStation(location);
		if(location.getBlock().getBlockData() instanceof Lightable)
			((Lightable) location.getBlock().getBlockData()).setLit(false);
		location.getWorld().dropItemNaturally(location, station.getCookingItem().getItemStack());
		Station.removeStation(location);
	}
}

