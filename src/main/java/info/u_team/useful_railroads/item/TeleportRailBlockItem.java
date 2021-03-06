package info.u_team.useful_railroads.item;

import info.u_team.u_team_core.util.MathUtil;
import info.u_team.useful_railroads.block.TeleportRailBlock;
import info.u_team.useful_railroads.util.Location;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class TeleportRailBlockItem extends BlockItem {
	
	public TeleportRailBlockItem(TeleportRailBlock block, Properties builder) {
		super(block, builder.maxStackSize(1).rarity(Rarity.EPIC));
	}
	
	@Override
	public ActionResultType tryPlace(BlockItemUseContext context) {
		final CompoundNBT compound = context.getItem().getChildTag("BlockEntityTag");
		if (compound != null && compound.contains("location")) {
			return super.tryPlace(context);
		}
		final PlayerEntity player = context.getPlayer();
		if (player != null && player.getEntityWorld().isRemote) {
			player.sendMessage(new TranslationTextComponent("block.usefulrailroads.teleport_rail.missing_setup").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
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
		final Vector3d itemEntityVector = itemEntity.getPositionVec();
		if (world.isRemote) { // Do client particles
			if (world.rand.nextInt(10) == 0) {
				for (int i = 0; i < 5; i++) {
					world.addParticle(ParticleTypes.ENCHANT, true, itemEntityVector.getX(), itemEntityVector.getY() + 0.5, itemEntityVector.getZ(), MathUtil.randomNumberInRange(world.rand, -0.2, 0.2), MathUtil.randomNumberInRange(world.rand, 0.1, 1.5), MathUtil.randomNumberInRange(world.rand, -0.2, 0.2));
				}
			}
		} else { // Do server stuff
			final int age = itemEntity.age;
			if (age < 100) { // Don't do anything when the age is not above 100 ticks
				return false;
			}
			final AxisAlignedBB aabb = new AxisAlignedBB(itemEntityVector.getX() - 1, itemEntityVector.getY() - 1, itemEntityVector.getZ() - 1, itemEntityVector.getX() + 1, itemEntityVector.getY() + 1, itemEntityVector.getZ() + 1);
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
						stack.getOrCreateChildTag("BlockEntityTag").put("location", new Location(world.getDimensionKey(), itemEntity.getPosition()).serializeNBT());
						
						final ItemEntity newItemEntity = new ItemEntity(world, itemEntityVector.getX(), itemEntityVector.getY(), itemEntityVector.getZ(), stack);
						newItemEntity.setDefaultPickupDelay();
						
						// Delete old entity
						itemEntity.remove();
						
						// Spawn new one with updated stack
						world.addEntity(newItemEntity);
						
						if (world instanceof ServerWorld) {
							final LightningBoltEntity lightningbolt = EntityType.LIGHTNING_BOLT.create(world);
							lightningbolt.moveForced(itemEntityVector);
							lightningbolt.setEffectOnly(true);
							world.addEntity(lightningbolt);
						}
					});
		}
		return false;
	}
}
