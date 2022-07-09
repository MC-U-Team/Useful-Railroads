package info.u_team.useful_railroads.block;

import java.util.function.Supplier;

import info.u_team.u_team_core.api.block.EntityBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CustomBlockEntityPoweredRailBlock extends CustomPoweredRailBlock implements EntityBlockProvider {
	
	protected final Supplier<? extends BlockEntityType<?>> blockEntityType;
	
	public CustomBlockEntityPoweredRailBlock(Supplier<? extends BlockEntityType<?>> blockEntityType) {
		this.blockEntityType = blockEntityType;
	}
	
	@Override
	public BlockEntityType<?> blockEntityType(BlockPos pos, BlockState state) {
		return blockEntityType.get();
	}
	
}
