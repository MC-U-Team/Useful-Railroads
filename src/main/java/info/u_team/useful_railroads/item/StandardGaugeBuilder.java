package info.u_team.useful_railroads.item;

import info.u_team.u_team_core.item.UItem;
import info.u_team.useful_railroads.block.StandardTrackBlock;
import info.u_team.useful_railroads.init.UsefulRailroadsBlocks;
import info.u_team.useful_railroads.init.UsefulRailroadsItemGroups;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StandardGaugeBuilder extends UItem {

	public StandardGaugeBuilder(String name) {
		super(name, UsefulRailroadsItemGroups.GROUP, new Properties().maxStackSize(1).rarity(Rarity.UNCOMMON));
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		final World world = context.getWorld();
		
		final BlockPos currentPos = context.getPos();
		final BlockState currentBlock = world.getBlockState(currentPos);
		if(currentBlock.getBlock().equals(UsefulRailroadsBlocks.TRACK_BLOCK)) {
			world.setBlockState(currentPos, currentBlock.with(StandardTrackBlock.HAS_TRACKS, true), 11);
			return ActionResultType.SUCCESS;
		}
		
		final BlockItemUseContext usecontext = new BlockItemUseContext(context);
		final BlockPos position = usecontext.getPos();

		final BlockPos checkPosition = position.down();
		
		final Direction placementFacing = usecontext.getPlacementHorizontalFacing();
		final BlockPos checkPositionnext = checkPosition.offset(placementFacing.rotateY());
		final BlockPos checkNear = checkPositionnext.up();

		final BlockState nearBlock = world.getBlockState(checkNear);
		
		if (usecontext.canPlace() && validPlacement(world, checkPosition, context.getPlayer())
				&& validPlacement(world, checkPositionnext, context.getPlayer())
				&& nearBlock.isReplaceable(usecontext)) {
			
			final BlockState placementState = UsefulRailroadsBlocks.TRACK_BLOCK.getStateForPlacement(usecontext);
			world.setBlockState(position, placementState, 11);
			world.setBlockState(checkNear, placementState.with(BlockStateProperties.HORIZONTAL_FACING, placementFacing.getOpposite()), 11);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.FAIL;
	}

	private static boolean validPlacement(World world, BlockPos checkPosition, Entity entity) {
		final BlockState statetoplaceon = world.getBlockState(checkPosition);
		return statetoplaceon.isSolid() && statetoplaceon.isTopSolid(world, checkPosition, entity);
	}

}
