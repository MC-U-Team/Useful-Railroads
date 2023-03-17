package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.BUFFER_STOP;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.DIRECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.HIGHSPEED_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.INTERSECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.SPEED_CLAMP_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.TELEPORT_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsItems.DOUBLE_TRACK_BUILDER;
import static info.u_team.useful_railroads.init.UsefulRailroadsItems.SINGLE_TRACK_BUILDER;

import info.u_team.u_team_core.data.CommonItemModelProvider;
import info.u_team.u_team_core.data.GenerationData;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.registries.ForgeRegistries;

public class UsefulRailroadsItemModelProvider extends CommonItemModelProvider {
	
	public UsefulRailroadsItemModelProvider(GenerationData generationData) {
		super(generationData);
	}
	
	@Override
	public void register() {
		// Items
		simpleGenerated(SINGLE_TRACK_BUILDER.get());
		simpleGenerated(DOUBLE_TRACK_BUILDER.get());
		
		// Rails
		simpleRail(HIGHSPEED_RAIL.get());
		simpleRail(SPEED_CLAMP_RAIL.get());
		simpleRail(DIRECTION_RAIL.get());
		simpleRail(INTERSECTION_RAIL.get());
		simpleRail(TELEPORT_RAIL.get());
		
		// Buffer stop
		getBuilder(BUFFER_STOP.getItemId().getPath()) //
				.parent(new ExistingModelFile(modLoc("block/buffer_stop"), existingFileHelper)) //
				.transforms() //
				
				.transform(ItemDisplayContext.GUI) //
				.rotation(0, -150, 0) //
				.translation(-0.5F, -0.5F, 0) //
				.scale(0.75F) //
				.end() //
				
				.transform(ItemDisplayContext.GROUND) //
				.translation(0, 2, 0) //
				.scale(0.25F) //
				.end() //
				
				.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) //
				.rotation(0, 120, 0) //
				.translation(2.5F, 1.5F, 0) //
				.scale(0.25F) //
				.end() //
				
				.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) //
				.rotation(70, 0, 0) //
				.translation(0, 0, 1) //
				.scale(0.35F) //
				.end() //
				
				.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND) //
				.rotation(0, 120, 0) //
				.translation(2.5F, 1.5F, 0) //
				.scale(0.25F) //
				.end() //
				
				.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND) //
				.rotation(70, 0, 0) //
				.translation(0, 0, 1) //
				.scale(0.35F) //
				.end() //
				
				.end();
	}
	
	private void simpleRail(ItemLike provider) {
		final String registryPath = ForgeRegistries.ITEMS.getKey(provider.asItem()).getPath();
		getBuilder(registryPath).parent(new UncheckedModelFile("item/generated")).texture("layer0", "block/" + registryPath);
	}
	
}
