package info.u_team.useful_railroads.block;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.*;
import net.minecraft.util.EnumFacing.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.*;

public class BlockRailDirection extends BlockCustomRailPowered {
	
	public static final PropertyBool AXIS_DIRECTION = PropertyBool.create("positive_axis");
	
	public BlockRailDirection(String name) {
		super(name);
		setDefaultState(getDefaultState().withProperty(AXIS_DIRECTION, false));
	}
	
	@Override
	public void onMinecartPassPowered(World world, EntityMinecart cart, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		
		EnumRailDirection shape = getRailDirection(world, pos, state, cart);
		boolean positive_axis = state.getValue(AXIS_DIRECTION);
		
		if (shape == EnumRailDirection.EAST_WEST) {
			cart.motionX = positive_axis ? -0.02 : 0.02;
		} else if (shape == EnumRailDirection.NORTH_SOUTH) {
			cart.motionZ = positive_axis ? -0.02 : 0.02;
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		setModel(getItem(), 0, getRegistryName());
		
		ModelLoader.setCustomStateMapper(this, (block) -> {
			Map<IBlockState, ModelResourceLocation> models = Maps.newLinkedHashMap();
			ResourceLocation registryname = block.getRegistryName();
			
			models.put(getDefaultState().withProperty(POWERED, false).withProperty(SHAPE, EnumRailDirection.NORTH_SOUTH).withProperty(AXIS_DIRECTION, false), new ModelResourceLocation(registryname, "powered=false,shape=north"));
			models.put(getDefaultState().withProperty(POWERED, false).withProperty(SHAPE, EnumRailDirection.EAST_WEST).withProperty(AXIS_DIRECTION, false), new ModelResourceLocation(registryname, "powered=false,shape=west"));
			models.put(getDefaultState().withProperty(POWERED, true).withProperty(SHAPE, EnumRailDirection.NORTH_SOUTH).withProperty(AXIS_DIRECTION, false), new ModelResourceLocation(registryname, "powered=true,shape=north"));
			models.put(getDefaultState().withProperty(POWERED, true).withProperty(SHAPE, EnumRailDirection.EAST_WEST).withProperty(AXIS_DIRECTION, false), new ModelResourceLocation(registryname, "powered=true,shape=west"));
			
			models.put(getDefaultState().withProperty(POWERED, false).withProperty(SHAPE, EnumRailDirection.NORTH_SOUTH).withProperty(AXIS_DIRECTION, true), new ModelResourceLocation(registryname, "powered=false,shape=south"));
			models.put(getDefaultState().withProperty(POWERED, false).withProperty(SHAPE, EnumRailDirection.EAST_WEST).withProperty(AXIS_DIRECTION, true), new ModelResourceLocation(registryname, "powered=false,shape=east"));
			models.put(getDefaultState().withProperty(POWERED, true).withProperty(SHAPE, EnumRailDirection.NORTH_SOUTH).withProperty(AXIS_DIRECTION, true), new ModelResourceLocation(registryname, "powered=true,shape=south"));
			models.put(getDefaultState().withProperty(POWERED, true).withProperty(SHAPE, EnumRailDirection.EAST_WEST).withProperty(AXIS_DIRECTION, true), new ModelResourceLocation(registryname, "powered=true,shape=east"));
			return models;
		});
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(AXIS_DIRECTION, facing.getAxisDirection() == AxisDirection.POSITIVE).withProperty(SHAPE, facing.getAxis() == Axis.Z ? EnumRailDirection.NORTH_SOUTH : EnumRailDirection.EAST_WEST);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		boolean powered = (meta & 1) == 1;
		boolean axis_direction = ((meta >> 1) & 1) == 1;
		int shape = (meta >> 2) & 1;
		return getDefaultState().withProperty(POWERED, powered).withProperty(AXIS_DIRECTION, axis_direction).withProperty(SHAPE, EnumRailDirection.byMetadata(shape));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		boolean powered = state.getValue(POWERED);
		boolean axis_direction = state.getValue(AXIS_DIRECTION);
		int shape = state.getValue(SHAPE).getMetadata();
		shape %= 2; // Don't save states that are higher than 1
		int meta = powered ? 1 : 0;
		meta |= (axis_direction ? 1 : 0) << 1;
		meta |= shape << 2;
		return meta;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, POWERED, AXIS_DIRECTION, SHAPE);
	}
}
