package info.u_team.useful_railroads.block;

import info.u_team.u_team_core.api.registry.IUBlockRegistryType;
import info.u_team.useful_railroads.init.UsefulRailroadsItemGroups;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public abstract class BlockCustomRailPowered extends PoweredRailBlock implements IUBlockRegistryType {
	
	protected final String name;
	protected final BlockItem blockItem;
	
	public BlockCustomRailPowered(String name) {
		super(Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.7F).sound(SoundType.METAL));
		this.name = name;
		blockItem = new BlockItem(this, new Item.Properties().group(UsefulRailroadsItemGroups.GROUP));
	}
	
	@Override
	public String getEntryName() {
		return name;
	}
	
	@Override
	public BlockItem getBlockItem() {
		return blockItem;
	}
	
	@Override
	public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}
	
	@Override
	public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
		if (!state.get(POWERED)) {
			cart.setMotion(Vec3d.ZERO);
			return;
		}
		onMinecartPassPowered(state, world, pos, cart);
	}
	
	public abstract void onMinecartPassPowered(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart);
}