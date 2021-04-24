package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.BUFFER_STOP;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.DIRECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.HIGHSPEED_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.INTERSECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.SPEED_CLAMP_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.TELEPORT_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsItems.DOUBLE_TRACK_BUILDER;
import static info.u_team.useful_railroads.init.UsefulRailroadsItems.SINGLE_TRACK_BUILDER;

import info.u_team.u_team_core.data.CommonItemModelsProvider;
import info.u_team.u_team_core.data.GenerationData;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.client.model.generators.ModelBuilder.Perspective;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;

public class UsefulRailroadsItemModelsProvider extends CommonItemModelsProvider {
	
	public UsefulRailroadsItemModelsProvider(GenerationData data) {
		super(data);
	}
	
	@Override
	protected void registerModels() {
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
		getBuilder(BUFFER_STOP.get().getRegistryName().getPath()) //
				.parent(new ExistingModelFile(modLoc("block/buffer_stop"), existingFileHelper)) //
				.transforms() //
				
				.transform(Perspective.GUI) //
				.rotation(0, -150, 0) //
				.translation(-0.5F, -0.5F, 0) //
				.scale(0.75F) //
				.end() //
				
				.transform(Perspective.GROUND) //
				.translation(0, 2, 0) //
				.scale(0.25F) //
				.end() //
				
				.transform(Perspective.FIRSTPERSON_RIGHT) //
				.rotation(0, 120, 0) //
				.translation(2.5F, 1.5F, 0) //
				.scale(0.25F) //
				.end() //
				
				.transform(Perspective.THIRDPERSON_RIGHT) //
				.rotation(70, 0, 0) //
				.translation(0, 0, 1) //
				.scale(0.35F) //
				.end() //
				
				.transform(Perspective.FIRSTPERSON_LEFT) //
				.rotation(0, 120, 0) //
				.translation(2.5F, 1.5F, 0) //
				.scale(0.25F) //
				.end() //
				
				.transform(Perspective.THIRDPERSON_LEFT) //
				.rotation(70, 0, 0) //
				.translation(0, 0, 1) //
				.scale(0.35F) //
				.end() //
				
				.end();
	}
	
	private void simpleRail(IItemProvider provider) {
		final String registryPath = provider.asItem().getRegistryName().getPath();
		getBuilder(registryPath).parent(new UncheckedModelFile("item/generated")).texture("layer0", "block/" + registryPath);
	}
	
}
