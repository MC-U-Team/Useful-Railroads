package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import info.u_team.u_team_core.data.CommonProvider;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.data.builder.FuelRecipeBuilder;
import info.u_team.useful_railroads.init.UsefulRailroadsRecipeSerializers;
import net.minecraft.advancements.criterion.*;
import net.minecraft.advancements.criterion.MinMaxBounds.IntBound;
import net.minecraft.data.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraft.item.crafting.Ingredient.TagList;
import net.minecraft.tags.Tag;
import net.minecraft.util.*;
import net.minecraftforge.common.Tags;

public class UsefulRailroadsRecipesProvider extends CommonProvider {
	
	public UsefulRailroadsRecipesProvider(DataGenerator generator) {
		super("Recipes", generator);
	}
	
	@Override
	public void act(DirectoryCache cache) throws IOException {
		final Path recipePath = path.resolve("recipes");
		final Path advancementPath = path.resolve("advancements");
		
		final Consumer<IFinishedRecipe> consumer = recipe -> {
			try {
				write(cache, recipe.getRecipeJson(), recipePath.resolve(recipe.getID().getPath() + ".json"));
				if (recipe.getAdvancementJson() != null) {
					write(cache, recipe.getAdvancementJson(), advancementPath.resolve(recipe.getID().getPath() + ".json"));
				}
			} catch (IOException ex) {
				LOGGER.error(marker, "Could not write data.", ex);
			}
		};
		
		addCraftingRecipes(consumer);
		addFuelRecipes(consumer);
	}
	
	@Override
	protected Path resolvePath(Path outputFolder) {
		return resolveData(outputFolder, UsefulRailroadsMod.MODID);
	}
	
	private void addCraftingRecipes(Consumer<IFinishedRecipe> consumer) {
		
		ShapedRecipeBuilder.shapedRecipe(HIGHSPEED_RAIL, 24) //
				.patternLine("IDI") //
				.patternLine("LSL") //
				.patternLine("IRI") //
				.key('R', getIngredientOfTag(Tags.Items.DUSTS_REDSTONE)) //
				.key('S', Items.STICK) //
				.key('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.key('D', getIngredientOfTag(Tags.Items.GEMS_DIAMOND)) //
				.key('L', getIngredientOfTag(Tags.Items.GEMS_LAPIS)) //
				.addCriterion("has_minecart", this.hasItem(Items.MINECART)) //
				.build(consumer);
		
		ShapedRecipeBuilder.shapedRecipe(DIRECTION_RAIL, 16) //
				.patternLine("IEI") //
				.patternLine("IRI") //
				.patternLine("ISI") //
				.key('S', Items.STICK) //
				.key('R', getIngredientOfTag(Tags.Items.DUSTS_REDSTONE)) //
				.key('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.key('E', Items.REPEATER) //
				.addCriterion("has_minecart", this.hasItem(Items.MINECART)) //
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
				.addCriterion("has_minecart", this.hasItem(Items.MINECART)) //
				.build(consumer);
		
		ShapedRecipeBuilder.shapedRecipe(INTERSECTION_RAIL, 8) //
				.patternLine("III") //
				.patternLine("ISI") //
				.patternLine("III") //
				.key('S', Items.STICK) //
				.key('I', getIngredientOfTag(Tags.Items.INGOTS_IRON)) //
				.addCriterion("has_minecart", this.hasItem(Items.MINECART)) //
				.build(consumer);
		
		CustomRecipeBuilder.func_218656_a(UsefulRailroadsRecipeSerializers.CRAFTING_SPECIAL_TELEPORT_RAIL).build(consumer, "teleport_rail_remove_location");
	}
	
	private void addFuelRecipes(Consumer<IFinishedRecipe> consumer) {
		addBasicFuelRecipe(Items.ENDER_PEARL, 100, consumer, "ender_pearl");
		addBasicFuelRecipe(Items.ENDER_EYE, 150, consumer, "ender_eye");
		addBasicFuelRecipe(Items.CHORUS_FLOWER, 250, consumer, "chorus_flower");
		addBasicFuelRecipe(Items.CHORUS_FRUIT, 200, consumer, "chorus_fruit");
		addBasicFuelRecipe(Items.POPPED_CHORUS_FRUIT, 210, consumer, "popped_chorus_fruit");
		addBasicFuelRecipe(Tags.Items.DUSTS_REDSTONE, 5, consumer, "redstone_dusts");
		addBasicFuelRecipe(Tags.Items.INGOTS_GOLD, 10, consumer, "gold_ingots");
		addBasicFuelRecipe(Tags.Items.GEMS_DIAMOND, 50, consumer, "diamond_gems");
	}
	
	private void addBasicFuelRecipe(Item item, int fuel, Consumer<IFinishedRecipe> consumer, String name) {
		FuelRecipeBuilder.fuelRecipe(Ingredient.fromItems(item), fuel).addCriterion("has_ingredient", hasItem(item)).build(consumer, new ResourceLocation(UsefulRailroadsMod.MODID, "fuel/" + name));
	}
	
	private void addBasicFuelRecipe(Tag<Item> tag, int fuel, Consumer<IFinishedRecipe> consumer, String name) {
		FuelRecipeBuilder.fuelRecipe(getIngredientOfTag(tag), fuel).addCriterion("has_ingredient", hasItem(tag)).build(consumer, new ResourceLocation(UsefulRailroadsMod.MODID, "fuel/" + name));
	}
	
	protected InventoryChangeTrigger.Instance hasItem(Tag<Item> tag) {
		return hasItem(ItemPredicate.Builder.create().tag(tag).build());
	}
	
	private InventoryChangeTrigger.Instance hasItem(IItemProvider item) {
		return hasItem(ItemPredicate.Builder.create().item(item).build());
	}
	
	private InventoryChangeTrigger.Instance hasItem(ItemPredicate... predicates) {
		return new InventoryChangeTrigger.Instance(IntBound.UNBOUNDED, IntBound.UNBOUNDED, IntBound.UNBOUNDED, predicates);
	}
	
	private Ingredient getIngredientOfTag(Tag<Item> tag) {
		return Ingredient.fromItemListStream(Stream.of(new TagList(tag) {
			
			@Override
			public Collection<ItemStack> getStacks() {
				return Arrays.asList(new ItemStack(Items.ACACIA_BOAT)); // Return default value, so the ingredient will serialize our tag.
			}
		}));
	}
}
