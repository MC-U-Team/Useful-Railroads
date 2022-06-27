package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.util.TagUtil;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class UsefulRailroadsTags {
	
	public static class Items {
		
		public static final TagKey<Item> TRACK_BUILDER_RAILS = TagUtil.createItemTag(UsefulRailroadsMod.MODID, "track_builder/rails");
		public static final TagKey<Item> TRACK_BUILDER_GROUND_BLOCKS = TagUtil.createItemTag(UsefulRailroadsMod.MODID, "track_builder/ground_blocks");
		public static final TagKey<Item> TRACK_BUILDER_TUNNEL_BLOCKS = TagUtil.createItemTag(UsefulRailroadsMod.MODID, "track_builder/tunnel_blocks");
		public static final TagKey<Item> TRACK_BUILDER_REDSTONE_TORCHES = TagUtil.createItemTag(UsefulRailroadsMod.MODID, "track_builder/redstone_torches");
		public static final TagKey<Item> TRACK_BUILDER_TORCHES = TagUtil.createItemTag(UsefulRailroadsMod.MODID, "track_builder/torches");
	}
	
}
