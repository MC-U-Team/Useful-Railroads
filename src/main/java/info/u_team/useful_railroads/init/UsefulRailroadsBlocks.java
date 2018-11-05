package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.registry.BlockRegistry;
import info.u_team.u_team_core.util.RegistryUtil;
import info.u_team.useful_railroads.UsefulRailroadsConstants;
import info.u_team.useful_railroads.block.BlockTest;
import net.minecraft.block.Block;

public class UsefulRailroadsBlocks {
	
	public static final BlockTest test = new BlockTest();
	
	public static void preinit() {
		BlockRegistry.register(UsefulRailroadsConstants.MODID, RegistryUtil.getRegistryEntries(Block.class, UsefulRailroadsBlocks.class));
	}
	
}
