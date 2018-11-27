package info.u_team.useful_railroads.block;

import info.u_team.u_team_core.api.IModelProvider;
import info.u_team.u_team_core.api.registry.IUBlock;
import info.u_team.useful_railroads.init.UsefulRailroadsCreativeTabs;
import net.minecraft.block.*;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public abstract class BlockCustomRailPowered extends BlockRailPowered implements IUBlock, IModelProvider {
	
	protected final String name;
	
	public BlockCustomRailPowered(String name) {
		this.name = name;
		setCreativeTab(UsefulRailroadsCreativeTabs.tab);
		setHardness(0.7F);
		setSoundType(SoundType.METAL);
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		setModel(getItem(), 0, getRegistryName());
	}
	
	@Override
	public Item getItem() {
		return Item.getItemFromBlock(this);
	}
	
	@Override
	public ItemBlock getItemBlock() {
		return (ItemBlock) new ItemBlock(this).setRegistryName(getRegistryName());
	}
	
	@Override
	public boolean canMakeSlopes(IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	@Override
	public void onMinecartPass(World world, EntityMinecart cart, BlockPos pos) {
		if (!world.getBlockState(pos).getValue(POWERED)) {
			cart.motionX = 0;
			cart.motionY = 0;
			cart.motionZ = 0;
			return;
		}
		onMinecartPassPowered(world, cart, pos);
	}
	
	public abstract void onMinecartPassPowered(World world, EntityMinecart cart, BlockPos pos);
}