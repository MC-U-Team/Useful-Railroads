package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.*;

import java.io.IOException;
import java.nio.file.Path;

import com.google.gson.JsonElement;

import info.u_team.u_team_core.data.CommonProvider;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import net.minecraft.data.*;
import net.minecraft.util.IItemProvider;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.SurvivesExplosion;

public class UsefulRailroadsLootTableProvider extends CommonProvider {
	
	public UsefulRailroadsLootTableProvider(DataGenerator generator) {
		super("Loot-Tables", generator);
	}
	
	@Override
	public void act(DirectoryCache cache) throws IOException {
		writeBasicBlockLootTable(cache, HIGHSPEED_RAIL);
		writeBasicBlockLootTable(cache, DIRECTION_RAIL);
		writeBasicBlockLootTable(cache, INTERSECTION_RAIL);
		writeBasicBlockLootTable(cache, BUFFER_STOP);
	}
	
	@Override
	protected Path resolvePath(Path outputFolder) {
		return resolveData(outputFolder, UsefulRailroadsMod.MODID).resolve("loot_tables");
	}
	
	private void writeBasicBlockLootTable(DirectoryCache cache, IItemProvider itemProvider) throws IOException {
		write(cache, getBasicBlockLootTable(itemProvider), path.resolve("blocks").resolve(itemProvider.asItem().getRegistryName().getPath() + ".json"));
	}
	
	private JsonElement getBasicBlockLootTable(IItemProvider itemProvider) {
		return LootTableManager.toJson(LootTable.builder().setParameterSet(LootParameterSets.BLOCK).addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(itemProvider)).acceptCondition(SurvivesExplosion.builder())).build());
	}
	
}
