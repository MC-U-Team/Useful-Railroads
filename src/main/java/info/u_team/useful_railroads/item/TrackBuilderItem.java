package info.u_team.useful_railroads.item;

import info.u_team.u_team_core.item.UItem;
import info.u_team.useful_railroads.init.UsefulRailroadsItemGroups;
import net.minecraft.item.Rarity;

public class TrackBuilderItem extends UItem {
	
	public TrackBuilderItem(String name) {
		super(name, UsefulRailroadsItemGroups.GROUP, new Properties().maxStackSize(1).rarity(Rarity.RARE));
	}
	
}
