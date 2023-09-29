package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.BUFFER_STOP;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.DIRECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.HIGHSPEED_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.INTERSECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.SPEED_CLAMP_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.TELEPORT_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsItems.DOUBLE_TRACK_BUILDER;
import static info.u_team.useful_railroads.init.UsefulRailroadsItems.SINGLE_TRACK_BUILDER;

import info.u_team.u_team_core.data.CommonRecipeProvider;
import info.u_team.u_team_core.data.GenerationData;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.data.builder.FuelRecipeBuilder;
import info.u_team.useful_railroads.init.UsefulRailroadsRecipeSerializers;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
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
	public void register(RecipeOutput output) {
		registerCraftingRecipes(output);
		registerFuelRecipes(output);
	}
	
	private void registerCraftingRecipes(RecipeOutput output) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, HIGHSPEED_RAIL.get(), 24) //
				.pattern("IDI") //
				.pattern("LSL") //
				.pattern("IRI") //
				.define('R', getIngredientOfTag(Tags.Items.DUSTS_REDSTONE)) //
				.define('S', Items.STICK) //
				.define('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.define('D', getIngredientOfTag(Tags.Items.GEMS_DIAMOND)) //
				.define('L', getIngredientOfTag(Tags.Items.GEMS_LAPIS)) //
				.unlockedBy("has_minecart", has(Items.MINECART)) //
				.save(output);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, SPEED_CLAMP_RAIL.get(), 24) //
				.pattern("IDI") //
				.pattern("LSL") //
				.pattern("IRI") //
				.define('R', getIngredientOfTag(Tags.Items.DUSTS_REDSTONE)) //
				.define('S', Items.STICK) //
				.define('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.define('D', getIngredientOfTag(Tags.Items.GEMS_EMERALD)) //
				.define('L', getIngredientOfTag(Tags.Items.SLIMEBALLS)) //
				.unlockedBy("has_minecart", has(Items.MINECART)) //
				.save(output);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, DIRECTION_RAIL.get(), 16) //
				.pattern("IEI") //
				.pattern("IRI") //
				.pattern("ISI") //
				.define('S', Items.STICK) //
				.define('R', getIngredientOfTag(Tags.Items.DUSTS_REDSTONE)) //
				.define('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.define('E', Items.REPEATER) //
				.unlockedBy("has_minecart", has(Items.MINECART)) //
				.save(output);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, TELEPORT_RAIL.get(), 1) //
				.pattern("IDI") //
				.pattern("ESE") //
				.pattern("IRI") //
				.define('R', getIngredientOfTag(Tags.Items.DUSTS_REDSTONE)) //
				.define('S', Items.STICK) //
				.define('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.define('D', getIngredientOfTag(Tags.Items.GEMS_DIAMOND)) //
				.define('E', Items.ENDER_PEARL) //
				.unlockedBy("has_minecart", has(Items.MINECART)) //
				.save(output);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, INTERSECTION_RAIL.get(), 8) //
				.pattern("III") //
				.pattern("ISI") //
				.pattern("III") //
				.define('S', Items.STICK) //
				.define('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.unlockedBy("has_minecart", has(Items.MINECART)) //
				.save(output);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, BUFFER_STOP.get(), 2) //
				.pattern("III") //
				.pattern(" B ") //
				.pattern("I I") //
				.define('B', getIngredientOfTag(Tags.Items.STORAGE_BLOCKS_IRON)) //
				.define('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.unlockedBy("has_minecart", has(Items.MINECART)) //
				.save(output);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, SINGLE_TRACK_BUILDER.get(), 1) //
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
				.save(output);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, DOUBLE_TRACK_BUILDER.get(), 1) //
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
				.save(output);
		
		SpecialRecipeBuilder.special(UsefulRailroadsRecipeSerializers.CRAFTING_SPECIAL_TELEPORT_RAIL_REMOVE_LOCATION.get()).save(output, UsefulRailroadsMod.MODID + ":teleport_rail_remove_location");
	}
	
	private void registerFuelRecipes(RecipeOutput output) {
		addTeleportRailFuel(Items.ENDER_PEARL, 100, output, "ender_pearl");
		addTeleportRailFuel(Items.ENDER_EYE, 150, output, "ender_eye");
		addTeleportRailFuel(Items.CHORUS_FLOWER, 250, output, "chorus_flower");
		addTeleportRailFuel(Items.CHORUS_FRUIT, 200, output, "chorus_fruit");
		addTeleportRailFuel(Items.POPPED_CHORUS_FRUIT, 210, output, "popped_chorus_fruit");
		addTeleportRailFuel(Tags.Items.DUSTS_REDSTONE, 5, output, "redstone_dusts");
		addTeleportRailFuel(Tags.Items.INGOTS_GOLD, 10, output, "gold_ingots");
		addTeleportRailFuel(Tags.Items.GEMS_DIAMOND, 50, output, "diamond_gems");
		
		addTrackBuilderFuel(ItemTags.COALS, 100, output, "coals");
		addTrackBuilderFuel(Tags.Items.STORAGE_BLOCKS_COAL, 900, output, "coal_blocks");
	}
	
	private void addTeleportRailFuel(Item item, int fuel, RecipeOutput output, String name) {
		addTeleportRailFuel(Ingredient.of(item), has(item), fuel, output, name);
	}
	
	private void addTeleportRailFuel(TagKey<Item> tag, int fuel, RecipeOutput output, String name) {
		addTeleportRailFuel(getIngredientOfTag(tag), has(tag), fuel, output, name);
	}
	
	private void addTeleportRailFuel(Ingredient ingredient, Criterion<?> trigger, int fuel, RecipeOutput output, String name) {
		FuelRecipeBuilder.teleportRailFuel(ingredient, fuel).unlockedBy("has_ingredient", trigger).save(output, new ResourceLocation(UsefulRailroadsMod.MODID, "fuel/teleport_rail/" + name));
	}
	
	private void addTrackBuilderFuel(TagKey<Item> tag, int fuel, RecipeOutput output, String name) {
		addTrackBuilderFuel(getIngredientOfTag(tag), has(tag), fuel, output, name);
	}
	
	private void addTrackBuilderFuel(Ingredient ingredient, Criterion<?> trigger, int fuel, RecipeOutput output, String name) {
		FuelRecipeBuilder.trackBuilderFuel(ingredient, fuel).unlockedBy("has_ingredient", trigger).save(output, new ResourceLocation(UsefulRailroadsMod.MODID, "fuel/track_builder/" + name));
	}
}
