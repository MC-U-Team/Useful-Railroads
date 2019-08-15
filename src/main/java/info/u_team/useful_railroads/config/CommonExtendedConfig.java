package info.u_team.useful_railroads.config;

import java.io.IOException;
import java.lang.reflect.*;
import java.nio.file.Path;
import java.util.*;

import org.apache.logging.log4j.*;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;

import info.u_team.useful_railroads.UsefulRailroadsMod;
import info.u_team.useful_railroads.util.*;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

public class CommonExtendedConfig {
	
	private static final Marker MARKER = MarkerManager.getMarker("Config");
	
	private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve(UsefulRailroadsMod.MODID);
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setVersion(1.2).enableComplexMapKeySerialization().registerTypeAdapter(Ingredient.class, new IngredientSerializerType()).create();
	
	private static HashMap<Ingredient, Integer> FUEL = new HashMap<Ingredient, Integer>();
	
	public static void loadConfig() {
		loadFuel();
	}
	
	private static void loadFuel() {
		FUEL.put(Ingredient.fromItems(Items.ENDER_PEARL), 100);
		FUEL.put(Ingredient.fromItems(Items.ENDER_EYE), 150);
		FUEL.put(Ingredient.fromItems(Blocks.CHORUS_FLOWER), 200);
		FUEL.put(Ingredient.fromItems(Items.CHORUS_FRUIT), 200);
		FUEL.put(Ingredient.fromItems(Items.POPPED_CHORUS_FRUIT), 210);
		FUEL.put(Ingredient.fromItems(Items.REDSTONE), 5);
		FUEL.put(Ingredient.fromItems(Items.GOLD_INGOT), 10);
		FUEL.put(Ingredient.fromItems(Items.DIAMOND), 50);
		
		System.out.println(ItemTags.getCollection().getTagMap());
		
		Tag<Item> tag = ItemTags.getCollection().get(new ResourceLocation("wool"));
		System.out.println(tag);
		System.out.println(FUEL.hashCode());
		
		try {
			FUEL = DefaultConfigUtil.loadConfig(CONFIG_PATH, "teleport_rail_fuel", GSON, FUEL, (defaultValue, writer) -> {
				GSON.toJson(defaultValue, TYPE, writer);
			}, reader -> {
				return GSON.fromJson(reader, TYPE);
			});
		} catch (IOException ex) {
			UsefulRailroadsMod.LOGGER.error(MARKER, "Could not load fuel list.", ex);
		}
		
		System.out.println(FUEL.hashCode());
		
		FUEL.forEach((ingredient, value) -> {
			System.out.println(ingredient.serialize() + " -> " + value);
		});
	}
	
	public static Map<Ingredient, Integer> getFuel() {
		return Collections.unmodifiableMap(FUEL);
	}
	
	private static final ParameterizedType TYPE = new ParameterizedType() {
		
		public Type[] getActualTypeArguments() {
			return new Type[] { Ingredient.class, Integer.class };
		}
		
		public Type getRawType() {
			return Map.class;
		}
		
		public Type getOwnerType() {
			return null;
		}
	};
}
