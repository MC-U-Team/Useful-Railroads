package info.u_team.useful_railroads.block;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.block.*;
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
	
//	@Override
//	protected IBlockState updateDir(World world, BlockPos pos, IBlockState state, boolean initialPlacement) {
//		return world.isRemote ? state : (new DirectionRail(world, pos, state)).place(world.isBlockPowered(pos), initialPlacement).getBlockState();
//	}
	
//	class DirectionRail extends Rail {
//		
//		public DirectionRail(World world, BlockPos pos, IBlockState state) {
//			super(world, pos, state);
//		}
//		
//		@Override
//		public Rail place(boolean powered, boolean initialPlacement) {
//			BlockPos blockpos = this.pos.north();
//            BlockPos blockpos1 = this.pos.south();
//            BlockPos blockpos2 = this.pos.west();
//            BlockPos blockpos3 = this.pos.east();
//            boolean flag = this.hasNeighborRail(blockpos);
//            boolean flag1 = this.hasNeighborRail(blockpos1);
//            boolean flag2 = this.hasNeighborRail(blockpos2);
//            boolean flag3 = this.hasNeighborRail(blockpos3);
//            BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = null;
//
//            if ((flag || flag1) && !flag2 && !flag3)
//            {
//                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
//            }
//
//            if ((flag2 || flag3) && !flag && !flag1)
//            {
//                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
//            }
//
//            if (!this.isPowered)
//            {
//                if (flag1 && flag3 && !flag && !flag2)
//                {
//                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
//                }
//
//                if (flag1 && flag2 && !flag && !flag3)
//                {
//                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
//                }
//
//                if (flag && flag2 && !flag1 && !flag3)
//                {
//                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
//                }
//
//                if (flag && flag3 && !flag1 && !flag2)
//                {
//                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
//                }
//            }
//
//            if (blockrailbase$enumraildirection == null)
//            {
//                if (flag || flag1)
//                {
//                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
//                }
//
//                if (flag2 || flag3)
//                {
//                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
//                }
//
//                if (!this.isPowered)
//                {
//                    if (powered)
//                    {
//                        if (flag1 && flag3)
//                        {
//                            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
//                        }
//
//                        if (flag2 && flag1)
//                        {
//                            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
//                        }
//
//                        if (flag3 && flag)
//                        {
//                            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
//                        }
//
//                        if (flag && flag2)
//                        {
//                            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
//                        }
//                    }
//                    else
//                    {
//                        if (flag && flag2)
//                        {
//                            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
//                        }
//
//                        if (flag3 && flag)
//                        {
//                            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
//                        }
//
//                        if (flag2 && flag1)
//                        {
//                            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
//                        }
//
//                        if (flag1 && flag3)
//                        {
//                            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
//                        }
//                    }
//                }
//            }
//
//            if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH && canMakeSlopes)
//            {
//                if (BlockRailBase.isRailBlock(this.world, blockpos.up()))
//                {
//                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
//                }
//
//                if (BlockRailBase.isRailBlock(this.world, blockpos1.up()))
//                {
//                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
//                }
//            }
//
//            if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST && canMakeSlopes)
//            {
//                if (BlockRailBase.isRailBlock(this.world, blockpos3.up()))
//                {
//                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
//                }
//
//                if (BlockRailBase.isRailBlock(this.world, blockpos2.up()))
//                {
//                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
//                }
//            }
//
//            if (blockrailbase$enumraildirection == null)
//            {
//                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
//            }
//
//            this.updateConnectedRails(blockrailbase$enumraildirection);
//            this.state = this.state.withProperty(this.block.getShapeProperty(), blockrailbase$enumraildirection);
//
//            if (initialPlacement || this.world.getBlockState(this.pos) != this.state)
//            {
//                this.world.setBlockState(this.pos, this.state, 3);
//
//                for (int i = 0; i < this.connectedRails.size(); ++i)
//                {
//                    BlockRailBase.Rail blockrailbase$rail = this.findRailAt(this.connectedRails.get(i));
//
//                    if (blockrailbase$rail != null)
//                    {
//                        blockrailbase$rail.removeSoftConnections();
//
//                        if (blockrailbase$rail.canConnectTo(this))
//                        {
//                            blockrailbase$rail.connectTo(this);
//                        }
//                    }
//                }
//            }
//
//            return this;
//		}
//	}
}
