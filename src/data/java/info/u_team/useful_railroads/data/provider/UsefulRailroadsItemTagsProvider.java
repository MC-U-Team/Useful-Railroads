package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsTags.Items.TRACK_BUILDER_GROUND_BLOCKS;
import static info.u_team.useful_railroads.init.UsefulRailroadsTags.Items.TRACK_BUILDER_RAILS;
import static info.u_team.useful_railroads.init.UsefulRailroadsTags.Items.TRACK_BUILDER_REDSTONE_TORCHES;
import static info.u_team.useful_railroads.init.UsefulRailroadsTags.Items.TRACK_BUILDER_TORCHES;
import static info.u_team.useful_railroads.init.UsefulRailroadsTags.Items.TRACK_BUILDER_TUNNEL_BLOCKS;

import info.u_team.u_team_core.data.CommonBlockTagsProvider;
import info.u_team.u_team_core.data.CommonItemTagsProvider;
import info.u_team.u_team_core.data.GenerationData;
import info.u_team.useful_railroads.init.UsefulRailroadsBlocks;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

public class UsefulRailroadsItemTagsProvider extends CommonItemTagsProvider {
	
	public UsefulRailroadsItemTagsProvider(GenerationData data, CommonBlockTagsProvider blockProvider) {
		super(data, blockProvider);
	}
	
	@Override
	protected void registerTags() {
		copy(BlockTags.RAILS, ItemTags.RAILS);
		getBuilder(TRACK_BUILDER_RAILS).add(Items.POWERED_RAIL, UsefulRailroadsBlocks.HIGHSPEED_RAIL.get().asItem(), UsefulRailroadsBlocks.SPEED_CLAMP_RAIL.get().asItem());
		getBuilder(TRACK_BUILDER_GROUND_BLOCKS).add(Items.GRAVEL);
		getBuilder(TRACK_BUILDER_TUNNEL_BLOCKS).add(ItemTags.STONE_BRICKS).add(Tags.Items.STONE).add(Tags.Items.COBBLESTONE).add(Tags.Items.SANDSTONE);
		getBuilder(TRACK_BUILDER_REDSTONE_TORCHES).add(Items.REDSTONE_TORCH, Items.REDSTONE_BLOCK);
		getBuilder(TRACK_BUILDER_TORCHES).add(Items.REDSTONE_TORCH, Items.TORCH);
	}
	
}
