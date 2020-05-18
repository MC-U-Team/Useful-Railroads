package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.*;

import info.u_team.u_team_core.data.*;
import net.minecraft.tags.BlockTags;

public class UsefulRailroadsBlockTagsProvider extends CommonBlockTagsProvider {
	
	public UsefulRailroadsBlockTagsProvider(GenerationData data) {
		super(data);
	}
	
	@Override
	protected void registerTags() {
		getBuilder(BlockTags.RAILS).add(HIGHSPEED_RAIL, SPEED_CLAMP_RAIL, DIRECTION_RAIL, INTERSECTION_RAIL, TELEPORT_RAIL, BUFFER_STOP);
	}
	
}
