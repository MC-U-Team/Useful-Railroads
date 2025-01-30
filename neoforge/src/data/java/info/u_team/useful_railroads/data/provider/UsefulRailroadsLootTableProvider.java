package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.BUFFER_STOP;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.DIRECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.HIGHSPEED_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.INTERSECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.SPEED_CLAMP_RAIL;

import info.u_team.u_team_core.data.CommonLootTableProvider;
import info.u_team.u_team_core.data.GenerationData;

public class UsefulRailroadsLootTableProvider extends CommonLootTableProvider {
	
	public UsefulRailroadsLootTableProvider(GenerationData generationData) {
		super(generationData);
	}
	
	@Override
	public void register(LootTableRegister register) {
		registerBlock(HIGHSPEED_RAIL, addBasicBlockLootTable(HIGHSPEED_RAIL.get()), register);
		registerBlock(SPEED_CLAMP_RAIL, addBasicBlockLootTable(SPEED_CLAMP_RAIL.get()), register);
		registerBlock(DIRECTION_RAIL, addBasicBlockLootTable(DIRECTION_RAIL.get()), register);
		registerBlock(INTERSECTION_RAIL, addBasicBlockLootTable(INTERSECTION_RAIL.get()), register);
		registerBlock(BUFFER_STOP, addBasicBlockLootTable(BUFFER_STOP.get()), register);
	}
}
