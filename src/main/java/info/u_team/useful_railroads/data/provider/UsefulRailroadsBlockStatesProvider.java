package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.*;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;

import info.u_team.u_team_core.data.*;
import info.u_team.useful_railroads.block.*;
import net.minecraft.block.*;
import net.minecraft.state.IProperty;
import net.minecraft.state.properties.*;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder.PartialBlockstate;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class UsefulRailroadsBlockStatesProvider extends CommonBlockStatesProvider {
	
	public UsefulRailroadsBlockStatesProvider(GenerationData data) {
		super(data);
	}
	
	@Override
	protected void registerStatesAndModels() {
		// Highspeed rail
		customFlatPoweredRail(HIGHSPEED_RAIL, flatRail("highspeed_rail"), flatRail("highspeed_powered_rail"));
		
		// Direction rail
		forAllFlatRailStates(getUncheckedVariantBuilder(DIRECTION_RAIL), state -> {
			final RailShape shape = state.get(DirectionRailBlock.SHAPE);
			final boolean powered = state.get(DirectionRailBlock.POWERED);
			final boolean positiveAxis = state.get(DirectionRailBlock.AXIS_DIRECTION);
			return ConfiguredModel.builder() //
					.modelFile(powered ? flatRail("direction_powered_rail") : flatRail("direction_rail")) //
					.rotationY((shape == RailShape.EAST_WEST ? -90 : 0) + (positiveAxis ? 180 : 0)) //
					.build();
		});
		
		// Intersection rail
		simpleBlock(INTERSECTION_RAIL, flatRail("intersection_rail"));
		
		// Teleport rail
		customFlatPoweredRail(TELEPORT_RAIL, flatRail("teleport_rail"), flatRail("teleport_powered_rail"));
		
		// Buffer stop
		getVariantBuilder(BUFFER_STOP).forAllStatesExcept(state -> { //
			return ConfiguredModel.builder() //
					.modelFile(new ExistingModelFile(modLoc("block/buffer_stop"), existingFileHelper)) //
					.rotationY(((int) state.get(BlockStateProperties.HORIZONTAL_FACING).getHorizontalAngle() + 180) % 360) //
					.build();
		}, BufferStopBlock.POWERED, BufferStopBlock.SHAPE);
	}
	
	private void customFlatPoweredRail(CustomPoweredRailBlock block, ModelFile normal, ModelFile powered) {
		customFlatPoweredRail(block, blockState -> blockState.get(CustomPoweredRailBlock.POWERED) ? powered : normal);
	}
	
	private void customFlatPoweredRail(CustomPoweredRailBlock block, Function<BlockState, ModelFile> modelFunc) {
		forAllFlatRailStates(getUncheckedVariantBuilder(block), state -> {
			final RailShape shape = state.get(CustomPoweredRailBlock.SHAPE);
			return ConfiguredModel.builder() //
					.modelFile(modelFunc.apply(state)) //
					.rotationY(shape == RailShape.EAST_WEST ? 90 : 0) //
					.build();
		});
	}
	
	private VariantBlockStateBuilder forAllFlatRailStates(VariantBlockStateBuilder builder, Function<BlockState, ConfiguredModel[]> mapper) {
		final Set<PartialBlockstate> seen = new HashSet<>();
		if (!(builder.getOwner() instanceof CustomPoweredRailBlock)) {
			throw new IllegalArgumentException("This method only allow custom powered rail blocks");
		}
		builder.getOwner().getStateContainer().getValidStates().forEach(fullState -> {
			final PartialBlockstate partialState = newPartialBlockState(builder.getOwner(), Maps.newLinkedHashMap(fullState.getValues()), builder);
			if (seen.add(partialState)) {
				final RailShape shape = fullState.get(CustomPoweredRailBlock.SHAPE);
				if (shape.getMeta() < 2) { // We only generate the NORTH_SOUTH and EAST_WEST shapes
					builder.setModels(partialState, mapper.apply(fullState));
				}
			}
		});
		return builder;
	}
	
	private BlockModelBuilder flatRail(String name) {
		return withExistingParent(name, "block/rail_flat") //
				.texture("rail", modLoc("block/" + name));
	}
	
	// Utility methods
	
	// Create partial block state with reflection
	private final Constructor<PartialBlockstate> PARTIAL_BLOCK_STATE_CONSTRUCTOR = ObfuscationReflectionHelper.findConstructor(PartialBlockstate.class, Block.class, Map.class, VariantBlockStateBuilder.class);
	
	private PartialBlockstate newPartialBlockState(Block owner, Map<IProperty<?>, Comparable<?>> setStates, VariantBlockStateBuilder outerBuilder) {
		try {
			return PARTIAL_BLOCK_STATE_CONSTRUCTOR.newInstance(owner, setStates, outerBuilder);
		} catch (Exception ex) {
			CommonProvider.LOGGER.fatal(marker, "Cannot create new PartialBlockstate with reflection.", ex);
		}
		return null;
	}
	
	// Create variant block state builder with reflection
	private final Constructor<VariantBlockStateBuilder> VARIANT_BLOCK_STATE_BUILDER_CONSTRUCTOR = ObfuscationReflectionHelper.findConstructor(VariantBlockStateBuilder.class, Block.class);
	
	private VariantBlockStateBuilder newVariantBlockStateBuilder(Block block) {
		try {
			return VARIANT_BLOCK_STATE_BUILDER_CONSTRUCTOR.newInstance(block);
		} catch (Exception ex) {
			CommonProvider.LOGGER.fatal(marker, "Cannot create new VariantBlockStateBuilder with reflection.", ex);
		}
		return null;
	}
	
	// Create a variant block state builder that don't check if all shapes are covered
	private VariantBlockStateBuilder getUncheckedVariantBuilder(Block block) {
		if (registeredBlocks.containsKey(block)) {
			final IGeneratedBlockstate old = registeredBlocks.get(block);
			Preconditions.checkState(old instanceof VariantBlockStateBuilder);
			return (VariantBlockStateBuilder) old;
		} else {
			VariantBlockStateBuilder ret = newVariantBlockStateBuilder(block);
			registeredBlocks.put(block, () -> {
				JsonObject variants = new JsonObject();
				ret.getModels().entrySet().stream() //
						.sorted(Entry.comparingByKey(PartialBlockstate.comparingByProperties())) //
						.forEach(entry -> variants.add(entry.getKey().toString(), entry.getValue().toJSON()));
				JsonObject main = new JsonObject();
				main.add("variants", variants);
				return main;
			});
			return ret;
		}
	}
}
