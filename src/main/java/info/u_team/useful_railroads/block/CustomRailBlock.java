package info.u_team.useful_railroads.block;

import info.u_team.u_team_core.api.block.BlockItemProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.SoundType;

public abstract class CustomRailBlock extends RailBlock implements BlockItemProvider {
	
	protected final BlockItem blockItem;
	
	public CustomRailBlock() {
		super(Properties.of().noCollission().strength(0.7F).sound(SoundType.METAL));
		blockItem = createBlockItem(new Item.Properties());
	}
	
	protected BlockItem createBlockItem(Item.Properties blockItemProperties) {
		return new BlockItem(this, blockItemProperties);
	}
	
	@Override
	public BlockItem blockItem() {
		return blockItem;
	}
}
