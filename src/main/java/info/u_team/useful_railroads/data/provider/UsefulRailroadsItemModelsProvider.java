package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.*;
import static info.u_team.useful_railroads.init.UsefulRailroadsItems.*;

import info.u_team.u_team_core.data.*;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.client.model.generators.ModelBuilder.Perspective;
import net.minecraftforge.client.model.generators.ModelFile.*;

public class UsefulRailroadsItemModelsProvider extends CommonItemModelsProvider {
	
	public UsefulRailroadsItemModelsProvider(GenerationData data) {
		super(data);
	}
	
	@Override
	protected void registerModels() {
		// Items
		simpleGenerated(SINGLE_TRACK_BUILDER);
		simpleGenerated(DOUBLE_TRACK_BUILDER);
		
		// Rails
		simpleRail(HIGHSPEED_RAIL);
		simpleRail(DIRECTION_RAIL);
		simpleRail(INTERSECTION_RAIL);
		simpleRail(TELEPORT_RAIL);
		
		// Buffer stop
		getBuilder(BUFFER_STOP.getRegistryName().getPath()) //
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
