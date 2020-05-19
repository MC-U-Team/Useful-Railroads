package info.u_team.useful_railroads.item;

import info.u_team.u_team_core.item.UItem;
import info.u_team.useful_railroads.block.StandardTrackBlock;
import info.u_team.useful_railroads.init.UsefulRailroadsBlocks;
import info.u_team.useful_railroads.init.UsefulRailroadsItemGroups;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class StandardGaugeBuilder extends UItem {

	public StandardGaugeBuilder(String name) {
		super(name, UsefulRailroadsItemGroups.GROUP, new Properties().maxStackSize(1).rarity(Rarity.UNCOMMON));
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		final World world = context.getWorld();
		final PlayerEntity player = context.getPlayer();

		final BlockPos currentPos = context.getPos();
		final BlockState currentBlock = world.getBlockState(currentPos);
		if (currentBlock.getBlock().equals(UsefulRailroadsBlocks.TRACK_BLOCK)) {
			if(currentBlock.get(StandardTrackBlock.HAS_TRACKS))
				return ActionResultType.PASS;
			if (!removeItem(Items.IRON_INGOT, player))
				return ActionResultType.FAIL;
			final Direction placementFacing = currentBlock.get(BlockStateProperties.HORIZONTAL_FACING);
			final BlockPos neighboring = currentPos.offset(placementFacing.rotateY());
			final BlockState neighboringState = world.getBlockState(neighboring);

			world.setBlockState(currentPos, currentBlock.with(StandardTrackBlock.HAS_TRACKS, true), 11);
			world.setBlockState(neighboring, neighboringState.with(StandardTrackBlock.HAS_TRACKS, true), 11);
			return ActionResultType.SUCCESS;
		}

		final BlockItemUseContext usecontext = new BlockItemUseContext(context);
		final BlockPos position = usecontext.getPos();

		final BlockPos checkPosition = position.down();

		final Direction placementFacing = usecontext.getPlacementHorizontalFacing();
		final BlockPos checkPositionnext = checkPosition.offset(placementFacing.rotateY());
		final BlockPos checkNear = checkPositionnext.up();

		final BlockState nearBlock = world.getBlockState(checkNear);

		if (usecontext.canPlace() && validPlacement(world, checkPosition, player)
				&& validPlacement(world, checkPositionnext, player) && nearBlock.isReplaceable(usecontext)) {
			if (!removeItem(Blocks.DARK_OAK_PLANKS.asItem(), player))
				return ActionResultType.FAIL;
			final BlockState placementState = UsefulRailroadsBlocks.TRACK_BLOCK.getStateForPlacement(usecontext);
			world.setBlockState(position, placementState, 11);
			world.setBlockState(checkNear,
					placementState.with(BlockStateProperties.HORIZONTAL_FACING, placementFacing.getOpposite()), 11);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.FAIL;
	}
	
	private static boolean removeItem(final Item item, final PlayerEntity entity) {
		if (entity.inventory.count(item) < 6 && !entity.isCreative()) {
			entity.sendStatusMessage(new TranslationTextComponent("msg.actionfail", item.getName().getFormattedText())
					.applyTextStyle(TextFormatting.DARK_RED), true);
			return false;
		}
		entity.inventory.clearMatchingItems(stack -> stack.getItem().equals(item), 6);
		return true;
	}

	private static boolean validPlacement(World world, BlockPos checkPosition, Entity entity) {
		final BlockState statetoplaceon = world.getBlockState(checkPosition);
		return statetoplaceon.isSolid() && statetoplaceon.isTopSolid(world, checkPosition, entity);
	}

}
