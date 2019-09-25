package info.u_team.useful_railroads.block;

import info.u_team.u_team_core.block.UBlock;
import info.u_team.useful_railroads.init.UsefulRailroadsItemGroups;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BufferStopBlock extends UBlock {
	
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	
	public BufferStopBlock(String name) {
		super(name, UsefulRailroadsItemGroups.GROUP, Properties.create(Material.IRON).doesNotBlockMovement().hardnessAndResistance(1.5F).sound(SoundType.METAL));
		setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
	}
	
	@Override
	public void onEntityWalk(World world, BlockPos pos, Entity entity) {
		
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!world.isRemote && entity instanceof AbstractMinecartEntity) {
			final AbstractMinecartEntity minecart = (AbstractMinecartEntity) entity;
			minecart.removePassengers();
			entity.remove();
		}
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
}
