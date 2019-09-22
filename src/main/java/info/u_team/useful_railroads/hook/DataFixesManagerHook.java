package info.u_team.useful_railroads.hook;

import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.*;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.util.Pair;

import net.minecraft.util.datafix.*;
import net.minecraft.util.datafix.fixes.AddNewChoices;

public class DataFixesManagerHook {
	
	/**
	 * Called via asm from {@link DataFixesManager#addFixers(DataFixerBuilder)} at
	 * the end
	 * 
	 * @param builder
	 */
	public static void hook(DataFixerBuilder builder) {
		System.out.println(builder);
		System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
		
		DataFixerUpper.ERRORS_ARE_FATAL = true;
		
		Schema old = builder.addSchema(1343, V1343_UsefulRailroads::new);
		builder.addFixer(new AddNewChoices(old, "Added rail", TypeReferences.BLOCK_ENTITY));
		
		Schema schema = builder.addSchema(1976, V1976_UsefulRailroads::new);
		builder.addFixer(new AddNewChoices(schema, "Added rail", TypeReferences.BLOCK_ENTITY));
		
		final Map<String, String> renameMap = ImmutableMap.of( //
				"usefulrailroads:rail_highspeed", "usefulrailroads:highspeed_rail", //
				"usefulrailroads:rail_direction", "usefulrailroads:direction_rail", //
				"usefulrailroads:rail_intersection", "usefulrailroads:intersection_rail", //
				"usefulrailroads:rail_teleport", "usefulrailroads:teleport_rail" //
		);
		
		// builder.addFixer(BlockRenameM.create(schema, "Rails block fixer", key -> {
		// System.out.println("REMAP BLOCk AAA");
		// System.out.println(key);
		// return renameMap.getOrDefault(key, key);
		// }));
		
		builder.addFixer(BlockRenameM.create(schema, "TESTS"));
		
		// builder.addFixer(ItemRename.create(schema, "Rails item fixer", key -> {
		// System.out.println("REMAP ITEM XXXX");
		// System.out.println(key);
		// return renameMap.getOrDefault(key, key);
		// }));
	}
	
	public static class V1343_UsefulRailroads extends NamespacedSchema {
		
		public V1343_UsefulRailroads(int version, Schema parent) {
			super(version, parent);
		}
		
		@Override
		public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
			Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
			
			schema.registerSimple(map, "usefulrailroads:rail_teleport_tile");
			System.out.println(map);
			return map;
		}
		
	}
	
	public static class V1976_UsefulRailroads extends NamespacedSchema {
		
		public V1976_UsefulRailroads(int version, Schema parent) {
			super(version, parent);
		}
		
		@Override
		public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
			Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
			schema.registerSimple(map, "usefulrailroads:rail_teleport_tile");
			System.out.println(map);
			
			
			// map.keySet().forEach(System.out::println);
			//
			// System.out.println("____________________________________________");
			//
			// map.put("usefulrailroads:teleport_rail",
			// map.remove("usefulrailroads:rail_teleport_tile"));
			// map.keySet().forEach(System.out::println);
			return map;
		}
		
	}
	
	public static abstract class BlockRenameM extends DataFix {
		
		private final String name;
		
		public BlockRenameM(Schema outputSchema, String name) {
			super(outputSchema, false);
			this.name = name;
		}
		
		public TypeRewriteRule makeRule() {
			Type<Pair<String, String>> type = DSL.named(TypeReferences.BLOCK_STATE.typeName(), DSL.namespacedString());
			
			// System.out.println(this.getInputSchema().getType(TypeReferences.BLOCK_ENTITY));
			// System.out.println(type);
			
			// if
			// (!Objects.equals(this.getInputSchema().getType(TypeReferences.BLOCK_ENTITY),
			// type)) {
			// throw new IllegalStateException("block name type is not what was expected.");
			// } else {
			return this.fixTypeEverywhere(this.name, type, (p_211012_1_) -> {
				return (p_206354_1_) -> {
					return p_206354_1_.mapSecond(this::fixBlock);
				};
			});
			// }
		}
		
		protected abstract String fixBlock(String p_206355_1_);
		
		public static DataFix create(Schema schema, String string) {
			return new BlockRenameM(schema, string) {
				
				protected String fixBlock(String p_206355_1_) {
					
					System.out.println(p_206355_1_);
					
					return p_206355_1_;
				}
			};
		}
	}
	
}
