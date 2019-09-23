package info.u_team.useful_railroads.item;

import java.util.stream.IntStream;

import info.u_team.u_team_core.item.UItem;
import info.u_team.useful_railroads.container.TrackBuilderContainer;
import info.u_team.useful_railroads.init.UsefulRailroadsItemGroups;
import info.u_team.useful_railroads.inventory.TrackBuilderInventoryWrapper;
import info.u_team.useful_railroads.util.TrackBuilderManager;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.*;
import net.minecraft.item.*;
import net.minecraft.network.play.server.SMultiBlockChangePacket;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class TrackBuilderItem extends UItem {
	
	public TrackBuilderItem(String name) {
		super(name, UsefulRailroadsItemGroups.GROUP, new Properties().maxStackSize(1).rarity(Rarity.RARE));
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		final ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote && !player.isSneaking() && player instanceof ServerPlayerEntity) { // Server & Player not sneaking
			final TrackBuilderInventoryWrapper wrapper = new TrackBuilderInventoryWrapper.Server(stack, () -> player.world);
			
			NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
				
				@Override
				public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
					return new TrackBuilderContainer(id, playerInventory, wrapper);
				}
				
				@Override
				public ITextComponent getDisplayName() {
					return stack.getDisplayName();
				}
			}, buffer -> buffer.writeVarInt(wrapper.getFuel()));
		}
		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		final World world = context.getWorld();
		if (world.isRemote) { // Server side
			return ActionResultType.PASS;
		}
		final PlayerEntity player = context.getPlayer();
		if (player == null || !context.isPlacerSneaking()) { // No player or no sneaking
			return ActionResultType.PASS;
		}
		final ItemStack stack = context.getItem();
		final TrackBuilderInventoryWrapper wrapper = new TrackBuilderInventoryWrapper.Server(stack, () -> player.world);
		
		final TrackBuilderManager manager = new TrackBuilderManager(context.getPos(), context.getFace(), world, player.getLookVec());
		
		manager.getRenderSet().forEach(pos -> {
			BlockState previous = world.getBlockState(pos);
			world.setBlockState(pos, Blocks.DIAMOND_ORE.getDefaultState());
//			world.markAndNotifyBlock(pos, world.getChunkAt(pos), Blocks.AIR.getDefaultState(), Blocks.DIAMOND_ORE.getDefaultState(), 3);
		});
		
		// Short[] objectArray = manager.getRenderSet().stream() //
		// .map(pos -> new Vec3i(pos.getX() & 15, pos.getY(), pos.getZ() & 15)) //
		// .map(vec -> (short) (vec.getX() << 12 | vec.getZ() << 8 | vec.getY())) //
		// .toArray(Short[]::new);
		//
		// short[] array = new short[objectArray.length];
		// IntStream.range(0, array.length).forEach(i -> array[i] = objectArray[i]);
		//
		// ((ServerPlayerEntity) player).connection.sendPacket(new SMultiBlockChangePacket(objectArray.length, array,
		// world.getChunkAt(player.getPosition())));
		
		return super.onItemUse(context);
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return !ItemStack.areItemsEqual(oldStack, newStack);
	}
}
