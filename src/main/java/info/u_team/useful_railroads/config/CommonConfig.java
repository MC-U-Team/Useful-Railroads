package info.u_team.useful_railroads.config;

import static info.u_team.useful_railroads.UsefulRailroadsConstants.MODID;

import java.util.HashMap;

import net.minecraft.init.*;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;

@Config(modid = MODID, category = "")
public class CommonConfig {
	
	public static TeleportRail teleportrail = new TeleportRail();
	
	public static class TeleportRail {
		
		@Comment("A map with all suitable items for fuel and how much they give")
		@Name("Fuel")
		public HashMap<String, Integer> fuel = new HashMap<>();
		
		public TeleportRail() {
			fuel.put(Items.ENDER_PEARL.getRegistryName().toString(), 100);
			fuel.put(Items.ENDER_EYE.getRegistryName().toString(), 150);
			fuel.put(Blocks.CHORUS_FLOWER.getRegistryName().toString(), 200);
			fuel.put(Items.CHORUS_FRUIT.getRegistryName().toString(), 200);
			fuel.put(Items.CHORUS_FRUIT_POPPED.getRegistryName().toString(), 210);
			fuel.put(Items.REDSTONE.getRegistryName().toString(), 5);
			fuel.put(Items.GOLD_INGOT.getRegistryName().toString(), 10);
		}
	}
	
}
