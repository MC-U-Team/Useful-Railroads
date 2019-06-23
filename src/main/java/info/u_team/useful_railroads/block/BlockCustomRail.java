package info.u_team.useful_railroads.block;

import info.u_team.u_team_core.api.registry.IUBlockRegistryType;
import info.u_team.useful_railroads.init.UsefulRailroadsItemGroups;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.*;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockCustomRail extends RailBlock implements IUBlockRegistryType {
	
	protected final String name;
	protected final BlockItem blockItem;
	
	public BlockCustomRail(String name) {
		super(Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.7F).sound(SoundType.METAL));
		this.name = name;
		blockItem = new BlockItem(this, new Item.Properties().group(UsefulRailroadsItemGroups.GROUP));
	}
	
	@Override
	public String getEntryName() {
		return name;
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
	}
	
	@Override
	public BlockItem getBlockItem() {
		return blockItem;
	}
	
}