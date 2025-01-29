package info.u_team.useful_railroads.block;

import info.u_team.u_team_core.api.block.BlockItemProvider;
import info.u_team.u_team_core.util.CastUtil;
import net.minecraft.world.level.block.Block;

public class ForgeRailBlockCreator implements RailBlockCreator.Creator {
	
	@Override
	public <B extends Block & BlockItemProvider> B createHighspeedRail() {
		return CastUtil.uncheckedCast(new HighSpeedRailBlock());
	}
	
	@Override
	public <B extends Block & BlockItemProvider> B createSpeedClampRail() {
		return CastUtil.uncheckedCast(new SpeedClampRailBlock());
	}
	
	@Override
	public <B extends Block & BlockItemProvider> B createDirectionalRail() {
		return CastUtil.uncheckedCast(new DirectionRailBlock());
	}
	
	@Override
	public <B extends Block & BlockItemProvider> B createIntersectionRail() {
		return CastUtil.uncheckedCast(new IntersectionRailBlock());
	}
	
	@Override
	public <B extends Block & BlockItemProvider> B createTeleportRail() {
		return CastUtil.uncheckedCast(new TeleportRailBlock());
	}
	
	@Override
	public <B extends Block & BlockItemProvider> B createBufferStop() {
		return CastUtil.uncheckedCast(new BufferStopBlock());
	}
	
}
