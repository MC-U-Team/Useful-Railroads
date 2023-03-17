package info.u_team.useful_railroads.init;

import info.u_team.useful_railroads.UsefulRailroadsMod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class UsefulRailroadsCreativeTabs {
	
	public static CreativeModeTab TAB;
	
	private static void register(CreativeModeTabEvent.Register event) {
		TAB = event.registerCreativeModeTab(new ResourceLocation(UsefulRailroadsMod.MODID, "tab"), builder -> {
			builder.icon(() -> new ItemStack(UsefulRailroadsBlocks.HIGHSPEED_RAIL.get()));
			builder.title(Component.translatable("creativetabs.usefulrailroads.tab"));
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
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(UsefulRailroadsCreativeTabs::register);
	}
	
}
