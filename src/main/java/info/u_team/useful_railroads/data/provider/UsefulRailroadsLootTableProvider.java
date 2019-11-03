package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.*;

import java.util.function.BiConsumer;

import info.u_team.u_team_core.data.CommonLootTablesProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;

public class UsefulRailroadsLootTableProvider extends CommonLootTablesProvider {
	
	public UsefulRailroadsLootTableProvider(DataGenerator generator) {
		super(generator);
	}
	
	@Override
	protected void registerLootTables(BiConsumer<ResourceLocation, LootTable> consumer) {
		registerBlock(HIGHSPEED_RAIL, addBasicBlockLootTable(HIGHSPEED_RAIL), consumer);
		registerBlock(DIRECTION_RAIL, addBasicBlockLootTable(DIRECTION_RAIL), consumer);
		registerBlock(INTERSECTION_RAIL, addBasicBlockLootTable(INTERSECTION_RAIL), consumer);
		registerBlock(BUFFER_STOP, addBasicBlockLootTable(BUFFER_STOP), consumer);
	}
}
