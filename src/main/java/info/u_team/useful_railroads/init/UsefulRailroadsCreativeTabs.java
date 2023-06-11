package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.api.registry.CreativeModeTabRegister;
import info.u_team.u_team_core.api.registry.RegistryEntry;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class UsefulRailroadsCreativeTabs {
	
	public static final CreativeModeTabRegister CREATIVE_TABS = CreativeModeTabRegister.create(UsefulRailroadsMod.MODID);
	
	public static final RegistryEntry<CreativeModeTab> TAB = CREATIVE_TABS.register("tab", builder -> {
		builder.icon(() -> new ItemStack(UsefulRailroadsBlocks.HIGHSPEED_RAIL.get()));
		builder.displayItems((parameters, output) -> {
			UsefulRailroadsBlocks.BLOCKS.itemIterable().forEach(item -> {
				output.accept(item);
			});
			UsefulRailroadsItems.ITEMS.forEach(registryObject -> {
				final Item item = registryObject.get();
				output.accept(item);
			});
		});
	});
	
	static void register() {
		CREATIVE_TABS.register();
	}
	
}
