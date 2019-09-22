package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsTags.Items.*;

import info.u_team.u_team_core.data.CommonItemTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Items;
import net.minecraft.tags.*;

public class UsefulRailroadsItemTagsProvider extends CommonItemTagsProvider {
	
	public UsefulRailroadsItemTagsProvider(DataGenerator generator) {
		super("Item-Tags", generator);
	}
	
	@Override
	protected void registerTags() {
		copy(BlockTags.RAILS, ItemTags.RAILS);
		getBuilder(TRACK_BUILDER_RAILS).add(ItemTags.RAILS);
		getBuilder(TRACK_BUILDER_GROUND_BLOCKS).add(Items.GRAVEL);
		getBuilder(TRACK_BUILDER_REDSTONE_TORCHES).add(Items.REDSTONE_TORCH);
	}
	
}
