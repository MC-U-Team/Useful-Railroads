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
import net.minecraft.advancements.criterion.*;
import net.minecraft.advancements.criterion.MinMaxBounds.IntBound;
import net.minecraft.data.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
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
	}
	
	private void addFuelRecipes(Consumer<IFinishedRecipe> consumer) {
		FuelRecipeBuilder.fuelRecipe(Ingredient.fromItems(Items.ENDER_PEARL), 100).addCriterion("test", hasItem(Items.ENDER_PEARL)).build(consumer, new ResourceLocation(UsefulRailroadsMod.MODID, "fuel_enderpearl"));
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
