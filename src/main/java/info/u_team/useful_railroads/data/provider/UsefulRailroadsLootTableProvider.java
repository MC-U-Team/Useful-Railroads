package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.BUFFER_STOP;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.DIRECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.HIGHSPEED_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.INTERSECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.SPEED_CLAMP_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.TRACK_BLOCK;

import java.util.function.BiConsumer;

import info.u_team.u_team_core.data.CommonLootTablesProvider;
import info.u_team.u_team_core.data.GenerationData;
import info.u_team.useful_railroads.block.StandardTrackBlock;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.conditions.BlockStateProperty;
import net.minecraft.world.storage.loot.conditions.SurvivesExplosion;
import net.minecraft.world.storage.loot.functions.SetCount;

public class UsefulRailroadsLootTableProvider extends CommonLootTablesProvider {

	public UsefulRailroadsLootTableProvider(GenerationData data) {
		super(data);
	}

	@Override
	protected void registerLootTables(BiConsumer<ResourceLocation, LootTable> consumer) {
		registerBlock(HIGHSPEED_RAIL, addBasicBlockLootTable(HIGHSPEED_RAIL), consumer);
		registerBlock(SPEED_CLAMP_RAIL, addBasicBlockLootTable(SPEED_CLAMP_RAIL), consumer);
		registerBlock(DIRECTION_RAIL, addBasicBlockLootTable(DIRECTION_RAIL), consumer);
		registerBlock(INTERSECTION_RAIL, addBasicBlockLootTable(INTERSECTION_RAIL), consumer);
		registerBlock(BUFFER_STOP, addBasicBlockLootTable(BUFFER_STOP), consumer);

		registerBlock(TRACK_BLOCK, LootTable.builder() //
				.setParameterSet(LootParameterSets.BLOCK) //
				.addLootPool(LootPool.builder() //
						.rolls(ConstantRange.of(1)) //
						.addEntry(ItemLootEntry.builder(Blocks.DARK_OAK_PLANKS)) //
						.acceptFunction(SetCount.builder(ConstantRange.of(6))) //
						.acceptCondition(SurvivesExplosion.builder())) //
				.addLootPool(LootPool.builder()
						.rolls(ConstantRange.of(1))//
						.addEntry(ItemLootEntry.builder(Items.IRON_INGOT))//
						.acceptFunction(SetCount.builder(ConstantRange.of(6))) //
						.acceptCondition(BlockStateProperty.builder(TRACK_BLOCK)//
								.fromProperties(StatePropertiesPredicate.Builder.newBuilder().withBoolProp(StandardTrackBlock.HAS_TRACKS, true)))//
						.acceptCondition(SurvivesExplosion.builder())) //
				.build(), consumer);
	}
}
