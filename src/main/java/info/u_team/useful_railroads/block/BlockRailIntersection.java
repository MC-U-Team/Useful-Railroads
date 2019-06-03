package info.u_team.useful_railroads.block;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.*;

public class BlockRailIntersection extends BlockCustomRail {
	
	public BlockRailIntersection(String name) {
		super(name);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		super.registerModel();
		
		ModelLoader.setCustomStateMapper(this, (block) -> {
			Map<IBlockState, ModelResourceLocation> models = Maps.newLinkedHashMap();
			ResourceLocation registryname = block.getRegistryName();
			
			for (EnumRailDirection direction : EnumRailDirection.values()) {
				models.put(getDefaultState().withProperty(SHAPE, direction), new ModelResourceLocation(registryname, "normal"));
			}
			return models;
		});
	}
	
	@Override
	public EnumRailDirection getRailDirection(IBlockAccess world, BlockPos pos, IBlockState state, EntityMinecart cart) {
		if (cart != null) {
			if (Math.abs(cart.motionX) > 0) {
				return EnumRailDirection.EAST_WEST;
			} else if (Math.abs(cart.motionZ) > 0) {
				return EnumRailDirection.NORTH_SOUTH;
			}
		}
		return super.getRailDirection(world, pos, state, cart);
	}
	
	@Override
	public boolean canMakeSlopes(IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean isFlexibleRail(IBlockAccess world, BlockPos pos) {
		return false;
	}
}
