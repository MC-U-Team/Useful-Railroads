package info.u_team.useful_railroads.config;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.registries.ForgeRegistries;

public class CommonConfig {
	
	public static final ForgeConfigSpec CONFIG;
	private static final CommonConfig INSTANCE;
	
	static {
		Pair<CommonConfig, ForgeConfigSpec> pair = new Builder().configure(CommonConfig::new);
		CONFIG = pair.getRight();
		INSTANCE = pair.getLeft();
	}
	
	public static CommonConfig getInstance() {
		return INSTANCE;
	}
	
	private CommonConfig(Builder builder) {
		builder.comment("Common configuration settings").push("common");
		
		HashMap<String, Integer> fuel = new HashMap<>();
		fuel.put(Items.ENDER_PEARL.getRegistryName().toString(), 100);
		fuel.put(Items.ENDER_EYE.getRegistryName().toString(), 150);
		fuel.put(Blocks.CHORUS_FLOWER.getRegistryName().toString(), 200);
		fuel.put(Items.CHORUS_FRUIT.getRegistryName().toString(), 200);
		fuel.put(Items.POPPED_CHORUS_FRUIT.getRegistryName().toString(), 210);
		fuel.put(Items.REDSTONE.getRegistryName().toString(), 5);
		fuel.put(Items.GOLD_INGOT.getRegistryName().toString(), 10);
		fuel.put(Items.DIAMOND.getRegistryName().toString(), 50);
		
		
		builder.defineInList("values", new ArrayList<>(fuel.keySet()), ForgeRegistries.ITEMS.getKeys());
		
		builder.pop();
	}
	
}
