package info.u_team.useful_railroads.block;

import info.u_team.u_team_core.api.IModelProvider;
import info.u_team.u_team_core.api.registry.IUBlock;
import info.u_team.useful_railroads.init.UsefulRailroadsCreativeTabs;
import net.minecraft.block.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.*;

public abstract class BlockCustomRail extends BlockRailBase implements IUBlock, IModelProvider {
	
	public static final PropertyEnum<EnumRailDirection> SHAPE = PropertyEnum.create("shape", EnumRailDirection.class, EnumRailDirection.NORTH_SOUTH, EnumRailDirection.EAST_WEST); // Just static and not slopes
	
	protected final String name;
	
	public BlockCustomRail(String name) {
		this(name, false);
	}
	
	public BlockCustomRail(String name, boolean powered) {
		super(powered);
		this.name = name;
		setCreativeTab(UsefulRailroadsCreativeTabs.tab);
		setHardness(0.7F);
		setSoundType(SoundType.METAL);
		// Don't set the default state here, cause the inheriting classes may change the
		// state. Obviously they should set the state in their ctors
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
	public int getMetaFromState(IBlockState state) {
		return state.getValue(SHAPE).getMetadata();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return blockState.getBaseState().withProperty(SHAPE, EnumRailDirection.byMetadata(meta));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, SHAPE);
	}
	
	@Override
	public IProperty<EnumRailDirection> getShapeProperty() {
		return SHAPE;
	}
	
}
