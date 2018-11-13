package info.u_team.useful_railroads.block;

import info.u_team.u_team_core.api.IModelProvider;
import info.u_team.u_team_core.api.registry.IUBlock;
import info.u_team.useful_railroads.init.UsefulRailroadsCreativeTabs;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.relauncher.*;

public class BlockCustomRailPowered extends BlockRailPowered implements IUBlock, IModelProvider {
	
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
}
