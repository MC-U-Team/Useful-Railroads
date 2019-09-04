package info.u_team.useful_railroads.data.provider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

import info.u_team.u_team_core.data.CommonProvider;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.data.builder.FuelRecipeBuilder;
import net.minecraft.advancements.criterion.*;
import net.minecraft.advancements.criterion.MinMaxBounds.IntBound;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.*;

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
		
		addFuelRecipes(consumer);
	}
	
	@Override
	protected Path resolvePath(Path outputFolder) {
		return resolveData(outputFolder, UsefulRailroadsMod.MODID);
	}
	
	private void addFuelRecipes(Consumer<IFinishedRecipe> consumer) {
		new FuelRecipeBuilder(Ingredient.fromItems(Items.ENDER_PEARL), 100).addCriterion("test", hasItem(Items.ENDER_PEARL)).build(consumer, new ResourceLocation(UsefulRailroadsMod.MODID, "fuel_enderpearl"));
	}
	
	private InventoryChangeTrigger.Instance hasItem(IItemProvider item) {
		return hasItem(ItemPredicate.Builder.create().item(item).build());
	}
	
	private InventoryChangeTrigger.Instance hasItem(ItemPredicate... predicates) {
		return new InventoryChangeTrigger.Instance(IntBound.UNBOUNDED, IntBound.UNBOUNDED, IntBound.UNBOUNDED, predicates);
	}
}
