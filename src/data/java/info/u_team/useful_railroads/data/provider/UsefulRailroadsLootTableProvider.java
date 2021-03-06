package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.BUFFER_STOP;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.DIRECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.HIGHSPEED_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.INTERSECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.SPEED_CLAMP_RAIL;

import java.util.function.BiConsumer;

import info.u_team.u_team_core.data.CommonLootTablesProvider;
import info.u_team.u_team_core.data.GenerationData;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;

public class UsefulRailroadsLootTableProvider extends CommonLootTablesProvider {
	
	public UsefulRailroadsLootTableProvider(GenerationData data) {
		super(data);
	}
	
	@Override
	protected void registerLootTables(BiConsumer<ResourceLocation, LootTable> consumer) {
		registerBlock(HIGHSPEED_RAIL, addBasicBlockLootTable(HIGHSPEED_RAIL.get()), consumer);
		registerBlock(SPEED_CLAMP_RAIL, addBasicBlockLootTable(SPEED_CLAMP_RAIL.get()), consumer);
		registerBlock(DIRECTION_RAIL, addBasicBlockLootTable(DIRECTION_RAIL.get()), consumer);
		registerBlock(INTERSECTION_RAIL, addBasicBlockLootTable(INTERSECTION_RAIL.get()), consumer);
		registerBlock(BUFFER_STOP, addBasicBlockLootTable(BUFFER_STOP.get()), consumer);
	}
}
