package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.*;

import info.u_team.u_team_core.data.CommonBlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;

public class UsefulRailroadsBlockTagsProvider extends CommonBlockTagsProvider {
	
	public UsefulRailroadsBlockTagsProvider(DataGenerator generator) {
		super("Block-Tags", generator);
	}
	
	@Override
	protected void registerTags() {
		getBuilder(BlockTags.RAILS).add(HIGHSPEED_RAIL, DIRECTION_RAIL, INTERSECTION_RAIL, TELEPORT_RAIL, BUFFER_STOP);
	}
	
}
