package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.*;
import static info.u_team.useful_railroads.init.UsefulRailroadsItems.*;

import java.util.function.Consumer;

import info.u_team.u_team_core.data.*;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.data.builder.FuelRecipeBuilder;
import info.u_team.useful_railroads.init.UsefulRailroadsRecipeSerializers;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class UsefulRailroadsRecipesProvider extends CommonRecipesProvider {
	
	public UsefulRailroadsRecipesProvider(GenerationData data) {
		super(data);
	}
	
	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		registerCraftingRecipes(consumer);
		registerFuelRecipes(consumer);
	}
	
	private void registerCraftingRecipes(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(HIGHSPEED_RAIL, 24) //
				.patternLine("IDI") //
				.patternLine("LSL") //
				.patternLine("IRI") //
				.key('R', getIngredientOfTag(Tags.Items.DUSTS_REDSTONE)) //
				.key('S', Items.STICK) //
				.key('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.key('D', getIngredientOfTag(Tags.Items.GEMS_DIAMOND)) //
				.key('L', getIngredientOfTag(Tags.Items.GEMS_LAPIS)) //
				.addCriterion("has_minecart", hasItem(Items.MINECART)) //
				.build(consumer);
		
		ShapedRecipeBuilder.shapedRecipe(SPEED_CLAMP_RAIL, 24) //
				.patternLine("IDI") //
				.patternLine("LSL") //
				.patternLine("IRI") //
				.key('R', getIngredientOfTag(Tags.Items.DUSTS_REDSTONE)) //
				.key('S', Items.STICK) //
				.key('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.key('D', getIngredientOfTag(Tags.Items.GEMS_DIAMOND)) //
				.key('L', getIngredientOfTag(Tags.Items.SLIMEBALLS)) //
				.addCriterion("has_minecart", hasItem(Items.MINECART)) //
				.build(consumer);
		
		ShapedRecipeBuilder.shapedRecipe(DIRECTION_RAIL, 16) //
				.patternLine("IEI") //
				.patternLine("IRI") //
				.patternLine("ISI") //
				.key('S', Items.STICK) //
				.key('R', getIngredientOfTag(Tags.Items.DUSTS_REDSTONE)) //
				.key('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.key('E', Items.REPEATER) //
				.addCriterion("has_minecart", hasItem(Items.MINECART)) //
				.build(consumer);
		
		ShapedRecipeBuilder.shapedRecipe(TELEPORT_RAIL, 1) //
				.patternLine("IDI") //
				.patternLine("ESE") //
				.patternLine("IRI") //
				.key('R', getIngredientOfTag(Tags.Items.DUSTS_REDSTONE)) //
				.key('S', Items.STICK) //
				.key('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.key('D', getIngredientOfTag(Tags.Items.GEMS_DIAMOND)) //
				.key('E', Items.ENDER_PEARL) //
				.addCriterion("has_minecart", hasItem(Items.MINECART)) //
				.build(consumer);
		
		ShapedRecipeBuilder.shapedRecipe(INTERSECTION_RAIL, 8) //
				.patternLine("III") //
				.patternLine("ISI") //
				.patternLine("III") //
				.key('S', Items.STICK) //
				.key('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.addCriterion("has_minecart", hasItem(Items.MINECART)) //
				.build(consumer);
		
		ShapedRecipeBuilder.shapedRecipe(BUFFER_STOP, 2) //
				.patternLine("III") //
				.patternLine(" B ") //
				.patternLine("I I") //
				.key('B', getIngredientOfTag(Tags.Items.STORAGE_BLOCKS_IRON)) //
				.key('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.addCriterion("has_minecart", hasItem(Items.MINECART)) //
				.build(consumer);
		
		ShapedRecipeBuilder.shapedRecipe(SINGLE_TRACK_BUILDER, 1) //
				.patternLine("IBI") //
				.patternLine("PRC") //
				.patternLine("IAI") //
				.key('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.key('B', getIngredientOfTag(Tags.Items.STORAGE_BLOCKS_REDSTONE)) //
				.key('P', Items.REPEATER) //
				.key('R', getIngredientOfTag(Tags.Items.DUSTS_REDSTONE)) //
				.key('C', Items.COMPARATOR) //
				.key('A', Items.POWERED_RAIL) //
				.addCriterion("has_redstone", hasItem(Tags.Items.DUSTS_REDSTONE)) //
				.addCriterion("has_iron", hasItem(Tags.Items.INGOTS_IRON)) //
				.addCriterion("has_rail", hasItem(Items.POWERED_RAIL)) //
				.build(consumer);
		
		ShapedRecipeBuilder.shapedRecipe(DOUBLE_TRACK_BUILDER, 1) //
				.patternLine("IBI") //
				.patternLine("PRC") //
				.patternLine("AIA") //
				.key('I', getIngredientOfTag(Tags.Items.STORAGE_BLOCKS_IRON)) //
				.key('B', getIngredientOfTag(Tags.Items.STORAGE_BLOCKS_REDSTONE)) //
				.key('P', Items.REPEATER) //
				.key('R', getIngredientOfTag(Tags.Items.DUSTS_REDSTONE)) //
				.key('C', Items.COMPARATOR) //
				.key('A', Items.POWERED_RAIL) //
				.addCriterion("has_redstone", hasItem(Tags.Items.DUSTS_REDSTONE)) //
				.addCriterion("has_iron", hasItem(Tags.Items.INGOTS_IRON)) //
				.addCriterion("has_rail", hasItem(Items.POWERED_RAIL)) //
				.build(consumer);
		
		CustomRecipeBuilder.customRecipe(UsefulRailroadsRecipeSerializers.CRAFTING_SPECIAL_TELEPORT_RAIL_REMOVE_LOCATION).build(consumer, UsefulRailroadsMod.MODID + ":teleport_rail_remove_location");
	}
	
	private void registerFuelRecipes(Consumer<IFinishedRecipe> consumer) {
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
	
	private void addTeleportRailFuel(Item item, int fuel, Consumer<IFinishedRecipe> consumer, String name) {
		addTeleportRailFuel(Ingredient.fromItems(item), hasItem(item), fuel, consumer, name);
	}
	
	private void addTeleportRailFuel(Tag<Item> tag, int fuel, Consumer<IFinishedRecipe> consumer, String name) {
		addTeleportRailFuel(getIngredientOfTag(tag), hasItem(tag), fuel, consumer, name);
	}
	
	private void addTeleportRailFuel(Ingredient ingredient, InventoryChangeTrigger.Instance trigger, int fuel, Consumer<IFinishedRecipe> consumer, String name) {
		FuelRecipeBuilder.teleportRailFuel(ingredient, fuel).addCriterion("has_ingredient", trigger).build(consumer, new ResourceLocation(UsefulRailroadsMod.MODID, "fuel/teleport_rail/" + name));
	}
	
	private void addTrackBuilderFuel(Tag<Item> tag, int fuel, Consumer<IFinishedRecipe> consumer, String name) {
		addTrackBuilderFuel(getIngredientOfTag(tag), hasItem(tag), fuel, consumer, name);
	}
	
	private void addTrackBuilderFuel(Ingredient ingredient, InventoryChangeTrigger.Instance trigger, int fuel, Consumer<IFinishedRecipe> consumer, String name) {
		FuelRecipeBuilder.trackBuilderFuel(ingredient, fuel).addCriterion("has_ingredient", trigger).build(consumer, new ResourceLocation(UsefulRailroadsMod.MODID, "fuel/track_builder/" + name));
	}
}
