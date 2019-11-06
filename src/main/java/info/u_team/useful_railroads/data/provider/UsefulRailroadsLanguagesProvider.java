package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.*;
import static info.u_team.useful_railroads.init.UsefulRailroadsItemGroups.GROUP;
import static info.u_team.useful_railroads.init.UsefulRailroadsItems.*;

import info.u_team.u_team_core.data.*;

public class UsefulRailroadsLanguagesProvider extends CommonLanguagesProvider {
	
	public UsefulRailroadsLanguagesProvider(GenerationData data) {
		super(data);
	}
	
	@Override
	public void addTranslations() {
		final String teleportRailBlockKey = "block.usefulrailroads.teleport_rail";
		final String trackBuilderItemKey = "item.usefulrailroads.track_builder";
		
		final String teleportRailContainerKey = "container.usefulrailroads.teleport_rail";
		final String trackBuilderContainerKey = "container.usefulrailroads.track_builder";
		
		// English
		
		add(GROUP, "Useful Railroads");
		
		add(HIGHSPEED_RAIL, "High Speed Rail (5 Blocks/Tick)");
		add(DIRECTION_RAIL, "Direction Rail");
		add(INTERSECTION_RAIL, "Intersection Rail");
		add(TELEPORT_RAIL, "Teleport Rail");
		
		add(BUFFER_STOP, "Buffer Stop");
		add(teleportRailBlockKey + ".missing_setup", "Cannot place. Setup the rail first.");
		add(teleportRailBlockKey + ".not_enough_fuel", "Not enough fuel. This teleportation costs %s fuel.");
		
		add(SINGLE_TRACK_BUILDER, "Single Track Builder");
		add(DOUBLE_TRACK_BUILDER, "Double Track Builder");
		add(trackBuilderItemKey + ".not_enough_fuel", "Not enough fuel. This operation costs %s fuel.");
		add(trackBuilderItemKey + ".not_enough_blocks", "Not enough blocks.");
		
		add(teleportRailContainerKey, "Teleport Rail");
		add(teleportRailContainerKey + ".dimension", "Dimension");
		add(teleportRailContainerKey + ".x", "X");
		add(teleportRailContainerKey + ".y", "Y");
		add(teleportRailContainerKey + ".z", "Z");
		add(teleportRailContainerKey + ".fuel", "Fuel");
		add(teleportRailContainerKey + ".consumption", "Consumption");
		add(trackBuilderContainerKey, "Track Builder");
		add(trackBuilderContainerKey + ".rails", "Rails");
		add(trackBuilderContainerKey + ".ground_blocks", "Ground Blocks");
		add(trackBuilderContainerKey + ".tunnel_blocks", "Tunnel Blocks");
		add(trackBuilderContainerKey + ".redstone_torches", "Redstone Torches");
		add(trackBuilderContainerKey + ".torches", "Torches (light)");
		add(trackBuilderContainerKey + ".fuel", "Fuel");
		add(trackBuilderContainerKey + ".mode", "Mode");
		add(trackBuilderContainerKey + ".mode.noair", "No extra tunnel");
		add(trackBuilderContainerKey + ".mode.3x3", "3x3 air tunnel");
		add(trackBuilderContainerKey + ".mode.5x5", "5x5 air tunnel");
		add(trackBuilderContainerKey + ".mode.tunnel", "Tunnel with blocks & light");
	}
	
}
