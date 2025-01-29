package info.u_team.useful_railroads.block;

import info.u_team.u_team_core.api.block.BlockItemProvider;
import info.u_team.u_team_core.util.ServiceUtil;
import net.minecraft.world.level.block.Block;

public class RailBlockCreator {
	
	// TODO generics are shit here, implement by concrete common types
	
	private static final Creator INSTANCE = ServiceUtil.loadOne(Creator.class);
	
	public static <B extends Block & BlockItemProvider> B createHighspeedRail() {
		return INSTANCE.createHighspeedRail();
	}
	
	public static <B extends Block & BlockItemProvider> B createSpeedClampRail() {
		return INSTANCE.createSpeedClampRail();
	}
	
	public static <B extends Block & BlockItemProvider> B createDirectionalRail() {
		return INSTANCE.createDirectionalRail();
	}
	
	public static <B extends Block & BlockItemProvider> B createIntersectionRail() {
		return INSTANCE.createIntersectionRail();
	}
	
	public static <B extends Block & BlockItemProvider> B createTeleportRail() {
		return INSTANCE.createTeleportRail();
	}
	
	public static <B extends Block & BlockItemProvider> B createBufferStop() {
		return INSTANCE.createBufferStop();
	}
	
	public static interface Creator {
		
		<B extends Block & BlockItemProvider> B createHighspeedRail();
		
		<B extends Block & BlockItemProvider> B createSpeedClampRail();
		
		<B extends Block & BlockItemProvider> B createDirectionalRail();
		
		<B extends Block & BlockItemProvider> B createIntersectionRail();
		
		<B extends Block & BlockItemProvider> B createTeleportRail();
		
		<B extends Block & BlockItemProvider> B createBufferStop();
		
	}
	
}
