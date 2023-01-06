package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.BUFFER_STOP;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.DIRECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.HIGHSPEED_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.INTERSECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.SPEED_CLAMP_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.TELEPORT_RAIL;

import info.u_team.u_team_core.data.CommonBlockTagsProvider;
import info.u_team.u_team_core.data.GenerationData;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

public class UsefulRailroadsBlockTagsProvider extends CommonBlockTagsProvider {
	
	public UsefulRailroadsBlockTagsProvider(GenerationData generationData) {
		super(generationData);
	}
	
	@Override
	public void register(HolderLookup.Provider provider) {
		tag(BlockTags.RAILS).add(HIGHSPEED_RAIL.get(), SPEED_CLAMP_RAIL.get(), DIRECTION_RAIL.get(), INTERSECTION_RAIL.get(), TELEPORT_RAIL.get(), BUFFER_STOP.get());
	}
	
}
