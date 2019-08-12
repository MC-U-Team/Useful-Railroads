package info.u_team.useful_railroads.util;

import java.util.HashMap;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.*;
import net.minecraft.state.StateContainer;

public class EmptyStateContainer extends StateContainer<Block, BlockState> {
	
	public EmptyStateContainer(Block block) {
		super(block, BlockState::new, new HashMap<>());
	}
	
	@Override
	public ImmutableList<BlockState> getValidStates() {
		return getOwner().getStateContainer().getValidStates();
	}
	
}
