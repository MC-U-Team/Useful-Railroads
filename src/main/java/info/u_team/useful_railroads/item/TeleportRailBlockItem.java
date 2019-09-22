package info.u_team.useful_railroads.item;

import java.util.Random;

import info.u_team.useful_railroads.block.TeleportRailBlock;
import info.u_team.useful_railroads.util.Location;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class TeleportRailBlockItem extends BlockItem {
	
	public TeleportRailBlockItem(TeleportRailBlock block, Properties builder) {
		super(block, builder.maxStackSize(1));
	}
	
	@Override
	public ActionResultType tryPlace(BlockItemUseContext context) {
		final CompoundNBT compound = context.getItem().getChildTag("BlockEntityTag");
		if (compound != null && compound.contains("location")) {
			return super.tryPlace(context);
		}
		final PlayerEntity player = context.getPlayer();
		if (player != null && player.getEntityWorld().isRemote) {
			player.sendMessage(new StringTextComponent(TextFormatting.DARK_RED + "You need to setup the rail first."));
		}
		return ActionResultType.FAIL;
	}
	
	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity itemEntity) {
		final CompoundNBT compound = stack.getChildTag("BlockEntityTag");
		if (compound != null && compound.contains("location")) { // Prevent overwriting already installed rails
			return false;
		}
		final World world = itemEntity.getEntityWorld();
		if (world.isRemote) { // Do client particles
			if (world.rand.nextInt(10) == 0) {
				for (int i = 0; i < 5; i++) {
					world.addParticle(ParticleTypes.ENCHANT, true, itemEntity.posX, itemEntity.posY + 0.5, itemEntity.posZ, getRandomNumberInRange(world.rand, -0.2, 0.2), getRandomNumberInRange(world.rand, 0.1, 1.5), getRandomNumberInRange(world.rand, -0.2, 0.2));
				}
			}
		} else { // Do server stuff
			final int age = itemEntity.age;
			if (age < 100) { // Don't do anything when the age is not above 100 ticks
				return false;
			}
			final AxisAlignedBB aabb = new AxisAlignedBB(itemEntity.posX - 1, itemEntity.posY - 1, itemEntity.posZ - 1, itemEntity.posX + 1, itemEntity.posY + 1, itemEntity.posZ + 1);
			// Search all item entities in a range of 1 block away from the origin entity.
			// Then lookup if it's an enderpearl.
			world.getEntitiesInAABBexcluding(itemEntity, aabb, ItemEntity.class::isInstance).stream() //
					.map(ItemEntity.class::cast) //
					.map(ItemEntity::getItem) //
					.filter(otherStack -> otherStack.getItem() == Items.ENDER_PEARL) //
					.findAny() //
					.ifPresent(otherStack -> {
						// Consume enderpearl
						otherStack.shrink(1);
						
						// Set location to the stack
						stack.getOrCreateChildTag("BlockEntityTag").put("location", new Location(world.getDimension().getType(), itemEntity.getPosition()).serializeNBT());
						
						final ItemEntity newItemEntity = new ItemEntity(world, itemEntity.posX, itemEntity.posY, itemEntity.posZ, stack);
						newItemEntity.setDefaultPickupDelay();
						
						// Delete old entity
						itemEntity.remove();
						
						// Spawn new one with updated stack
						world.addEntity(newItemEntity);
						
						if (world instanceof ServerWorld) {
							((ServerWorld) world).addLightningBolt(new LightningBoltEntity(world, itemEntity.posX, itemEntity.posY, itemEntity.posZ, true));
						}
					});
		}
		return false;
	}
	
	/**
	 * Just a helper method. Might be in uteamcore soon again TODO
	 * 
	 * @param random
	 * @param min
	 * @param max
	 * @return
	 */
	public static double getRandomNumberInRange(Random random, double min, double max) {
		return random.nextDouble() * (max - min) + min;
	}
}
