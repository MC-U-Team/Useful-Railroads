package info.u_team.useful_railroads.data.provider;

import info.u_team.u_team_core.data.CommonItemTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.*;

public class UsefulRailroadsItemTagsProvider extends CommonItemTagsProvider {
	
	public UsefulRailroadsItemTagsProvider(DataGenerator generator) {
		super("Item-Tags", generator);
	}
	
	@Override
	protected void registerTags() {
		copy(BlockTags.RAILS, ItemTags.RAILS);
	}
	
}
