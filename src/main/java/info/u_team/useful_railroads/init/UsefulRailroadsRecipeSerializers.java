package info.u_team.useful_railroads.init;

import info.u_team.u_team_core.recipeserializer.USpecialRecipeSerializer;
import info.u_team.u_team_core.util.registry.BaseRegistryUtil;
import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.recipe.*;
import net.minecraft.item.crafting.*;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = UsefulRailroadsMod.MODID, bus = Bus.MOD)
public class UsefulRailroadsRecipeSerializers {
	
	public static final IRecipeSerializer<TeleportRailFuelRecipe> TELEPORT_FUEL = new TeleportRailFuelRecipe.Serializer("teleport_fuel");
	
	public static final SpecialRecipeSerializer<TeleportRailSpecialCraftingRecipe> CRAFTING_SPECIAL_TELEPORT_RAIL = new USpecialRecipeSerializer<>("crafting_special_teleport_rail", TeleportRailSpecialCraftingRecipe::new);
	
	@SubscribeEvent
	public static void register(Register<IRecipeSerializer<?>> event) {
		BaseRegistryUtil.getAllGenericRegistryEntriesAndApplyNames(UsefulRailroadsMod.MODID, IRecipeSerializer.class).forEach(event.getRegistry()::register);
	}
	
}
