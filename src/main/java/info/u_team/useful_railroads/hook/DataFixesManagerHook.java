package info.u_team.useful_railroads.hook;

import java.util.Objects;

import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;

import net.minecraft.util.datafix.*;
import net.minecraft.util.datafix.fixes.*;

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
		
		Schema schema51 = builder.addSchema(20000, DataFixesManager.NAMESPACED_SCHEMA_FACTORY);
		builder.addFixer(BlockRename.create(schema51, "Colorless shulker block fixer", (p_207106_0_) -> {
			return Objects.equals(NamespacedSchema.ensureNamespaced(p_207106_0_), "minecraft:purple_shulker_box") ? "minecraft:shulker_box" : p_207106_0_;
		}));
		builder.addFixer(ItemRename.create(schema51, "Colorless shulker item fixer", (p_207101_0_) -> {
			return Objects.equals(NamespacedSchema.ensureNamespaced(p_207101_0_), "minecraft:purple_shulker_box") ? "minecraft:shulker_box" : p_207101_0_;
		}));
	}
}
