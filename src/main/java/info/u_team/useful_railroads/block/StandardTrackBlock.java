package info.u_team.useful_railroads.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import info.u_team.u_team_core.block.UBlock;
import info.u_team.useful_railroads.init.UsefulRailroadsItems;
import info.u_team.useful_railroads.util.VoxelShapeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class StandardTrackBlock extends UBlock {

	private static final Map<Direction, VoxelShape> VOXEL_SHAPES = createVoxelShape();

	public final static Material STANDARD_RAIL = new Material(MaterialColor.AIR, false, false, true, false, true, false,
			false, PushReaction.IGNORE);

	public static final BooleanProperty HAS_TRACKS = BooleanProperty.create("track");

	public StandardTrackBlock(String name) {
		super(name, Properties.create(STANDARD_RAIL).sound(SoundType.WOOD).hardnessAndResistance(0.25f).harvestTool(ToolType.AXE));
		this.setDefaultState(getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
				.with(HAS_TRACKS, false));
	}

	private static Map<Direction, VoxelShape> createVoxelShape() {
		final List<Pair<Vec3d, Vec3d>> northShape = new ArrayList<>();
		northShape.add(VoxelShapeUtil.createVectorPair(0, 0, 5, 16, 2, 11));
		return VoxelShapeUtil.getHorizontalRotations(northShape).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, valueEntry -> {
			return VoxelShapes.or(VoxelShapes.empty(), VoxelShapeUtil.createVoxelShapeFromVector(valueEntry.getValue()).stream().toArray(VoxelShape[]::new));
		}));
	}

	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos,
			PlayerEntity player) {
		return new ItemStack(UsefulRailroadsItems.STANDARD_TRACK_BUILDER);
	}
	
	@Override
	public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return false;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return VOXEL_SHAPES.getOrDefault(state.get(BlockStateProperties.HORIZONTAL_FACING), VoxelShapes.fullCube());
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return super.getStateForPlacement(context)
				.with(BlockStateProperties.HORIZONTAL_FACING, context.getPlacementHorizontalFacing())
				.with(HAS_TRACKS, false);
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		super.onBlockHarvested(worldIn, pos, state, player);
		final Direction facing = state.get(BlockStateProperties.HORIZONTAL_FACING).rotateY();
		worldIn.removeBlock(pos.offset(facing), false);
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING, HAS_TRACKS);
	}

}
