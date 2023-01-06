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
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

public class UsefulRailroadsItemTagsProvider extends CommonItemTagsProvider {
	
	public UsefulRailroadsItemTagsProvider(GenerationData generationData, CommonBlockTagsProvider blockProvider) {
		super(generationData, blockProvider);
	}
	
	@Override
	public void register(HolderLookup.Provider provider) {
		copy(BlockTags.RAILS, ItemTags.RAILS);
		tag(TRACK_BUILDER_RAILS).add(Items.POWERED_RAIL, UsefulRailroadsBlocks.HIGHSPEED_RAIL.get().asItem(), UsefulRailroadsBlocks.SPEED_CLAMP_RAIL.get().asItem());
		tag(TRACK_BUILDER_GROUND_BLOCKS).add(Items.GRAVEL);
		tag(TRACK_BUILDER_TUNNEL_BLOCKS).addTag(ItemTags.STONE_BRICKS).addTag(Tags.Items.STONE).addTag(Tags.Items.COBBLESTONE).addTag(Tags.Items.SANDSTONE);
		tag(TRACK_BUILDER_REDSTONE_TORCHES).add(Items.REDSTONE_TORCH, Items.REDSTONE_BLOCK);
		tag(TRACK_BUILDER_TORCHES).add(Items.REDSTONE_TORCH, Items.TORCH);
	}
	
}
