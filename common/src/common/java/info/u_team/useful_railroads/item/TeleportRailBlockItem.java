package info.u_team.useful_railroads.item;

import info.u_team.u_team_core.api.item.UItemExtension;
import info.u_team.u_team_core.util.MathUtil;
import info.u_team.useful_railroads.util.Location;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TeleportRailBlockItem extends BlockItem implements UItemExtension {
	
	public TeleportRailBlockItem(Block block, Properties builder) {
		super(block, builder.stacksTo(1).rarity(Rarity.EPIC));
	}
	
	@Override
	public InteractionResult place(BlockPlaceContext context) {
		final CustomData component = context.getItemInHand().get(DataComponents.BLOCK_ENTITY_DATA);
		final CompoundTag compound = component == null ? null : component.copyTag();
		if (compound != null && compound.contains("location")) {
			return super.place(context);
		}
		final Player player = context.getPlayer();
		if (player != null && player.getCommandSenderWorld().isClientSide) {
			player.sendSystemMessage(Component.translatable("block.usefulrailroads.teleport_rail.missing_setup").withStyle(ChatFormatting.RED));
		}
		return InteractionResult.FAIL;
	}
	
	@Override
	public boolean updateItemEntity(ItemStack stack, ItemEntity itemEntity) {
		final CustomData component = stack.get(DataComponents.BLOCK_ENTITY_DATA);
		final CompoundTag compound = component == null ? null : component.copyTag();
		if (compound != null && compound.contains("location")) { // Prevent overwriting already installed rails
			return false;
		}
		final Level world = itemEntity.getCommandSenderWorld();
		final Vec3 itemEntityVector = itemEntity.position();
		if (world.isClientSide) { // Do client particles
			if (world.random.nextInt(10) == 0) {
				for (int i = 0; i < 5; i++) {
					// world.addParticle(ParticleTypes.ENCHANT, true, itemEntityVector.x(), itemEntityVector.y() + 0.5,
					// itemEntityVector.z(), MathUtil.randomNumberInRange(world.random, -0.2, 0.2),
					// MathUtil.randomNumberInRange(world.random, 0.1, 1.5), MathUtil.randomNumberInRange(world.random, -0.2, 0.2));
					world.addParticle(ParticleTypes.ENCHANT, true, itemEntityVector.x(), itemEntityVector.y() + 0.5, itemEntityVector.z(), MathUtil.randomNumberInRange(world.getRandom(), -0.2, 0.2), MathUtil.randomNumberInRange(world.getRandom(), 0.1, 1.5), MathUtil.randomNumberInRange(world.getRandom(), -0.2, 0.2));
				}
			}
		} else { // Do server stuff
			final int age = itemEntity.getAge();
			if (age < 100) { // Don't do anything when the age is not above 100 ticks
				return false;
			}
			final AABB aabb = new AABB(itemEntityVector.x() - 1, itemEntityVector.y() - 1, itemEntityVector.z() - 1, itemEntityVector.x() + 1, itemEntityVector.y() + 1, itemEntityVector.z() + 1);
			// Search all item entities in a range of 1 block away from the origin entity.
			// Then lookup if it's an enderpearl.
			world.getEntities(itemEntity, aabb, ItemEntity.class::isInstance).stream() //
					.map(ItemEntity.class::cast) //
					.map(ItemEntity::getItem) //
					.filter(otherStack -> otherStack.getItem() == Items.ENDER_PEARL) //
					.findAny() //
					.ifPresent(otherStack -> {
						// Consume enderpearl
						otherStack.shrink(1);
						
						// Set location to the stack
						stack.update(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY, old -> {
							final CompoundTag oldCompound = old.copyTag();
							oldCompound.put("location", new Location(world.dimension(), itemEntity.blockPosition()).serializeNBT());
							return CustomData.of(oldCompound);
						});
						
						final ItemEntity newItemEntity = new ItemEntity(world, itemEntityVector.x(), itemEntityVector.y(), itemEntityVector.z(), stack);
						newItemEntity.setDefaultPickUpDelay();
						
						// Delete old entity
						itemEntity.remove(RemovalReason.DISCARDED);
						
						// Spawn new one with updated stack
						world.addFreshEntity(newItemEntity);
						
						if (world instanceof ServerLevel) {
							final LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(world);
							lightningbolt.moveTo(itemEntityVector);
							lightningbolt.setVisualOnly(true);
							world.addFreshEntity(lightningbolt);
						}
					});
		}
		return false;
	}
}
