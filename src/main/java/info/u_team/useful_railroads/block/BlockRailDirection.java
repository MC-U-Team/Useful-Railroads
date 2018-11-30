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
			cart.motionX = positive_axis ? 0.6 : -0.6;
		} else if (shape == EnumRailDirection.NORTH_SOUTH) {
			cart.motionZ = positive_axis ? 0.6 : -0.6;
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
		EnumFacing placeFacing = placer.getHorizontalFacing();
		return getDefaultState().withProperty(AXIS_DIRECTION, placeFacing.getAxisDirection() == AxisDirection.POSITIVE).withProperty(SHAPE, placeFacing.getAxis() == Axis.Z ? EnumRailDirection.NORTH_SOUTH : EnumRailDirection.EAST_WEST);
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
	
	@Override
	protected IBlockState updateDir(World world, BlockPos pos, IBlockState state, boolean initialPlacement) {
		return world.isRemote ? state : (new DirectionRail(world, pos, state)).place(world.isBlockPowered(pos), initialPlacement).getBlockState();
	}
	
	public class DirectionRail extends Rail {
		
		public DirectionRail(World world, BlockPos pos, IBlockState state) {
			super(world, pos, state);
		}
		
		@Override
		public Rail place(boolean powered, boolean initialPlacement) {
			boolean hasRailNorth = hasNeighborRail(pos.north());
			boolean hasRailSouth = hasNeighborRail(pos.south());
			boolean hasRailWest = hasNeighborRail(pos.west());
			boolean hasRailEast = hasNeighborRail(pos.east());
			
			EnumRailDirection direction = null;
			boolean positive_axis = state.getValue(AXIS_DIRECTION);
			
			if ((hasRailNorth || hasRailSouth) && !hasRailWest && !hasRailEast) {
				direction = EnumRailDirection.NORTH_SOUTH;
				if (hasRailNorth && !hasRailSouth) {
					positive_axis = false;
				} else if (hasRailSouth && !hasRailNorth) {
					positive_axis = true;
				}
			}
			
			if ((hasRailWest || hasRailEast) && !hasRailNorth && !hasRailSouth) {
				direction = EnumRailDirection.EAST_WEST;
				if (hasRailWest && !hasRailEast) {
					positive_axis = false;
				} else if (hasRailEast && !hasRailWest) {
					positive_axis = true;
				}
			}
			
			if (direction == null) {
				direction = state.getValue(SHAPE);
			}
			
			updateConnectedRails(direction);
			state = state.withProperty(SHAPE, direction).withProperty(AXIS_DIRECTION, positive_axis);
			
			if (initialPlacement || world.getBlockState(pos) != state) {
				world.setBlockState(pos, state, 3);
				
				for (int i = 0; i < connectedRails.size(); ++i) {
					Rail rail = findRailAt(connectedRails.get(i));
					
					if (rail != null) {
						rail.removeSoftConnections();
						
						if (rail.canConnectTo(this)) {
							rail.connectTo(this);
						}
					}
				}
			}
			
			return this;
		}
	}
}
