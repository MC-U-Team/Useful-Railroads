package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.BUFFER_STOP;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.DIRECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.HIGHSPEED_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.INTERSECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.SPEED_CLAMP_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.TELEPORT_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsItems.DOUBLE_TRACK_BUILDER;
import static info.u_team.useful_railroads.init.UsefulRailroadsItems.SINGLE_TRACK_BUILDER;

import java.util.function.Consumer;

import info.u_team.u_team_core.data.CommonRecipeProvider;
import info.u_team.u_team_core.data.GenerationData;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.data.builder.FuelRecipeBuilder;
import info.u_team.useful_railroads.init.UsefulRailroadsRecipeSerializers;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

public class UsefulRailroadsRecipeProvider extends CommonRecipeProvider {
	
	public UsefulRailroadsRecipeProvider(GenerationData generationData) {
		super(generationData);
	}
	
	@Override
	public void register(Consumer<FinishedRecipe> consumer) {
		registerCraftingRecipes(consumer);
		registerFuelRecipes(consumer);
	}
	
	private void registerCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(HIGHSPEED_RAIL.get(), 24) //
				.pattern("IDI") //
				.pattern("LSL") //
				.pattern("IRI") //
				.define('R', getIngredientOfTag(Tags.Items.DUSTS_REDSTONE)) //
				.define('S', Items.STICK) //
				.define('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.define('D', getIngredientOfTag(Tags.Items.GEMS_DIAMOND)) //
				.define('L', getIngredientOfTag(Tags.Items.GEMS_LAPIS)) //
				.unlockedBy("has_minecart", has(Items.MINECART)) //
				.save(consumer);
		
		ShapedRecipeBuilder.shaped(SPEED_CLAMP_RAIL.get(), 24) //
				.pattern("IDI") //
				.pattern("LSL") //
				.pattern("IRI") //
				.define('R', getIngredientOfTag(Tags.Items.DUSTS_REDSTONE)) //
				.define('S', Items.STICK) //
				.define('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.define('D', getIngredientOfTag(Tags.Items.GEMS_EMERALD)) //
				.define('L', getIngredientOfTag(Tags.Items.SLIMEBALLS)) //
				.unlockedBy("has_minecart", has(Items.MINECART)) //
				.save(consumer);
		
		ShapedRecipeBuilder.shaped(DIRECTION_RAIL.get(), 16) //
				.pattern("IEI") //
				.pattern("IRI") //
				.pattern("ISI") //
				.define('S', Items.STICK) //
				.define('R', getIngredientOfTag(Tags.Items.DUSTS_REDSTONE)) //
				.define('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.define('E', Items.REPEATER) //
				.unlockedBy("has_minecart", has(Items.MINECART)) //
				.save(consumer);
		
		ShapedRecipeBuilder.shaped(TELEPORT_RAIL.get(), 1) //
				.pattern("IDI") //
				.pattern("ESE") //
				.pattern("IRI") //
				.define('R', getIngredientOfTag(Tags.Items.DUSTS_REDSTONE)) //
				.define('S', Items.STICK) //
				.define('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.define('D', getIngredientOfTag(Tags.Items.GEMS_DIAMOND)) //
				.define('E', Items.ENDER_PEARL) //
				.unlockedBy("has_minecart", has(Items.MINECART)) //
				.save(consumer);
		
		ShapedRecipeBuilder.shaped(INTERSECTION_RAIL.get(), 8) //
				.pattern("III") //
				.pattern("ISI") //
				.pattern("III") //
				.define('S', Items.STICK) //
				.define('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.unlockedBy("has_minecart", has(Items.MINECART)) //
				.save(consumer);
		
		ShapedRecipeBuilder.shaped(BUFFER_STOP.get(), 2) //
				.pattern("III") //
				.pattern(" B ") //
				.pattern("I I") //
				.define('B', getIngredientOfTag(Tags.Items.STORAGE_BLOCKS_IRON)) //
				.define('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.unlockedBy("has_minecart", has(Items.MINECART)) //
				.save(consumer);
		
		ShapedRecipeBuilder.shaped(SINGLE_TRACK_BUILDER.get(), 1) //
				.pattern("IBI") //
				.pattern("PRC") //
				.pattern("IAI") //
				.define('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.define('B', getIngredientOfTag(Tags.Items.STORAGE_BLOCKS_REDSTONE)) //
				.define('P', Items.REPEATER) //
				.define('R', getIngredientOfTag(Tags.Items.DUSTS_REDSTONE)) //
				.define('C', Items.COMPARATOR) //
				.define('A', Items.POWERED_RAIL) //
				.unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE)) //
				.unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON)) //
				.unlockedBy("has_rail", has(Items.POWERED_RAIL)) //
				.save(consumer);
		
		ShapedRecipeBuilder.shaped(DOUBLE_TRACK_BUILDER.get(), 1) //
				.pattern("IBI") //
				.pattern("PRC") //
				.pattern("AIA") //
				.define('I', getIngredientOfTag(Tags.Items.STORAGE_BLOCKS_IRON)) //
				.define('B', getIngredientOfTag(Tags.Items.STORAGE_BLOCKS_REDSTONE)) //
				.define('P', Items.REPEATER) //
				.define('R', getIngredientOfTag(Tags.Items.DUSTS_REDSTONE)) //
				.define('C', Items.COMPARATOR) //
				.define('A', Items.POWERED_RAIL) //
				.unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE)) //
				.unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON)) //
				.unlockedBy("has_rail", has(Items.POWERED_RAIL)) //
				.save(consumer);
		
		SpecialRecipeBuilder.special(UsefulRailroadsRecipeSerializers.CRAFTING_SPECIAL_TELEPORT_RAIL_REMOVE_LOCATION.get()).save(consumer, UsefulRailroadsMod.MODID + ":teleport_rail_remove_location");
	}
	
	private void registerFuelRecipes(Consumer<FinishedRecipe> consumer) {
		addTeleportRailFuel(Items.ENDER_PEARL, 100, consumer, "ender_pearl");
		addTeleportRailFuel(Items.ENDER_EYE, 150, consumer, "ender_eye");
		addTeleportRailFuel(Items.CHORUS_FLOWER, 250, consumer, "chorus_flower");
		addTeleportRailFuel(Items.CHORUS_FRUIT, 200, consumer, "chorus_fruit");
		addTeleportRailFuel(Items.POPPED_CHORUS_FRUIT, 210, consumer, "popped_chorus_fruit");
		addTeleportRailFuel(Tags.Items.DUSTS_REDSTONE, 5, consumer, "redstone_dusts");
		addTeleportRailFuel(Tags.Items.INGOTS_GOLD, 10, consumer, "gold_ingots");
		addTeleportRailFuel(Tags.Items.GEMS_DIAMOND, 50, consumer, "diamond_gems");
		
		addTrackBuilderFuel(ItemTags.COALS, 100, consumer, "coals");
		addTrackBuilderFuel(Tags.Items.STORAGE_BLOCKS_COAL, 900, consumer, "coal_blocks");
	}
	
	private void addTeleportRailFuel(Item item, int fuel, Consumer<FinishedRecipe> consumer, String name) {
		addTeleportRailFuel(Ingredient.of(item), has(item), fuel, consumer, name);
	}
	
	private void addTeleportRailFuel(TagKey<Item> tag, int fuel, Consumer<FinishedRecipe> consumer, String name) {
		addTeleportRailFuel(getIngredientOfTag(tag), has(tag), fuel, consumer, name);
	}
	
	private void addTeleportRailFuel(Ingredient ingredient, CriterionTriggerInstance trigger, int fuel, Consumer<FinishedRecipe> consumer, String name) {
		FuelRecipeBuilder.teleportRailFuel(ingredient, fuel).addCriterion("has_ingredient", trigger).save(consumer, new ResourceLocation(UsefulRailroadsMod.MODID, "fuel/teleport_rail/" + name));
	}
	
	private void addTrackBuilderFuel(TagKey<Item> tag, int fuel, Consumer<FinishedRecipe> consumer, String name) {
		addTrackBuilderFuel(getIngredientOfTag(tag), has(tag), fuel, consumer, name);
	}
	
	private void addTrackBuilderFuel(Ingredient ingredient, CriterionTriggerInstance trigger, int fuel, Consumer<FinishedRecipe> consumer, String name) {
		FuelRecipeBuilder.trackBuilderFuel(ingredient, fuel).addCriterion("has_ingredient", trigger).save(consumer, new ResourceLocation(UsefulRailroadsMod.MODID, "fuel/track_builder/" + name));
	}
}
