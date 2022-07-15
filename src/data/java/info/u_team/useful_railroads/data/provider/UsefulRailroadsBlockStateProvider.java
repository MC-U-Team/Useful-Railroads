package info.u_team.useful_railroads.data.provider;

import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.BUFFER_STOP;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.DIRECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.HIGHSPEED_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.INTERSECTION_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.SPEED_CLAMP_RAIL;
import static info.u_team.useful_railroads.init.UsefulRailroadsBlocks.TELEPORT_RAIL;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;

import info.u_team.u_team_core.data.CommonBlockStateProvider;
import info.u_team.u_team_core.data.GenerationData;
import info.u_team.useful_railroads.block.BufferStopBlock;
import info.u_team.useful_railroads.block.CustomAdvancedBlockEntityRailBlock;
import info.u_team.useful_railroads.block.CustomPoweredRailBlock;
import info.u_team.useful_railroads.block.DirectionRailBlock;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.IGeneratedBlockState;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder.PartialBlockstate;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class UsefulRailroadsBlockStateProvider extends CommonBlockStateProvider {
	
	public UsefulRailroadsBlockStateProvider(GenerationData generationData) {
		super(generationData);
	}
	
	@Override
	public void register() {
		// Highspeed rail
		customFlatPoweredRail(HIGHSPEED_RAIL.get(), flatRail("highspeed_rail"), flatRail("highspeed_powered_rail"));
		
		// Speed Clamp rail
		customFlatPoweredRail(SPEED_CLAMP_RAIL.get(), flatRail("clamp_rail"), flatRail("clamp_powered_rail"));
		
		// Direction rail
		forAllFlatRailStates(getUncheckedVariantBuilder(DIRECTION_RAIL.get()), state -> {
			final RailShape shape = state.getValue(PoweredRailBlock.SHAPE);
			final boolean powered = state.getValue(PoweredRailBlock.POWERED);
			final boolean positiveAxis = state.getValue(DirectionRailBlock.AXIS_DIRECTION);
			return ConfiguredModel.builder() //
					.modelFile(powered ? flatRail("direction_powered_rail") : flatRail("direction_rail")) //
					.rotationY((shape == RailShape.EAST_WEST ? -90 : 0) + (positiveAxis ? 180 : 0)) //
					.build();
		});
		
		// Intersection rail
		simpleBlock(INTERSECTION_RAIL.get(), flatRail("intersection_rail"));
		
		// Teleport rail
		customFlatPoweredRail(TELEPORT_RAIL.get(), flatRail("teleport_rail"), flatRail("teleport_powered_rail"));
		
		// Buffer stop
		getVariantBuilder(BUFFER_STOP.get()).forAllStatesExcept(state -> { //
			return ConfiguredModel.builder() //
					.modelFile(new ExistingModelFile(modLoc("block/buffer_stop"), models().existingFileHelper)) //
					.rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360) //
					.build();
		}, BufferStopBlock.POWERED, CustomAdvancedBlockEntityRailBlock.SHAPE);
	}
	
	private void customFlatPoweredRail(CustomPoweredRailBlock block, ModelFile normal, ModelFile powered) {
		customFlatPoweredRail(block, blockState -> blockState.getValue(PoweredRailBlock.POWERED) ? powered : normal);
	}
	
	private void customFlatPoweredRail(CustomPoweredRailBlock block, Function<BlockState, ModelFile> modelFunc) {
		forAllFlatRailStates(getUncheckedVariantBuilder(block), state -> {
			final RailShape shape = state.getValue(PoweredRailBlock.SHAPE);
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
		builder.getOwner().getStateDefinition().getPossibleStates().forEach(fullState -> {
			final Map<Property<?>, Comparable<?>> propertyValues = Maps.newLinkedHashMap(fullState.getValues());
			propertyValues.remove(BaseRailBlock.WATERLOGGED); // Remove water logging in state generation as we do not differentiate models based on that
			final PartialBlockstate partialState = newPartialBlockState(builder.getOwner(), propertyValues, builder);
			if (seen.add(partialState)) {
				final RailShape shape = fullState.getValue(PoweredRailBlock.SHAPE);
				if (shape == RailShape.NORTH_SOUTH || shape == RailShape.EAST_WEST) { // We only generate the NORTH_SOUTH and EAST_WEST shapes
					builder.setModels(partialState, mapper.apply(fullState));
				}
			}
		});
		return builder;
	}
	
	private BlockModelBuilder flatRail(String name) {
		return models().withExistingParent(name, "block/rail_flat") //
				.texture("rail", modLoc("block/" + name)).renderType("cutout");
	}
	
	// Utility methods
	
	// Create partial block state with reflection
	private final Constructor<PartialBlockstate> PARTIAL_BLOCK_STATE_CONSTRUCTOR = ObfuscationReflectionHelper.findConstructor(PartialBlockstate.class, Block.class, Map.class, VariantBlockStateBuilder.class);
	
	private PartialBlockstate newPartialBlockState(Block owner, Map<Property<?>, Comparable<?>> setStates, VariantBlockStateBuilder outerBuilder) {
		try {
			return PARTIAL_BLOCK_STATE_CONSTRUCTOR.newInstance(owner, setStates, outerBuilder);
		} catch (final Exception ex) {
			throw new RuntimeException("Cannot create new PartialBlockstate with reflection.", ex);
		}
	}
	
	// Create variant block state builder with reflection
	private final Constructor<VariantBlockStateBuilder> VARIANT_BLOCK_STATE_BUILDER_CONSTRUCTOR = ObfuscationReflectionHelper.findConstructor(VariantBlockStateBuilder.class, Block.class);
	
	private VariantBlockStateBuilder newVariantBlockStateBuilder(Block block) {
		try {
			return VARIANT_BLOCK_STATE_BUILDER_CONSTRUCTOR.newInstance(block);
		} catch (final Exception ex) {
			throw new RuntimeException("Cannot create new VariantBlockStateBuilder with reflection.", ex);
		}
	}
	
	// Create a variant block state builder that don't check if all shapes are covered
	private VariantBlockStateBuilder getUncheckedVariantBuilder(Block block) {
		if (registeredBlocks.containsKey(block)) {
			final IGeneratedBlockState old = registeredBlocks.get(block);
			Preconditions.checkState(old instanceof VariantBlockStateBuilder);
			return (VariantBlockStateBuilder) old;
		} else {
			final VariantBlockStateBuilder ret = newVariantBlockStateBuilder(block);
			registeredBlocks.put(block, () -> {
				final JsonObject variants = new JsonObject();
				ret.getModels().entrySet().stream() //
						.sorted(Entry.comparingByKey(PartialBlockstate.comparingByProperties())) //
						.forEach(entry -> variants.add(entry.getKey().toString(), entry.getValue().toJSON()));
				final JsonObject main = new JsonObject();
				main.add("variants", variants);
				return main;
			});
			return ret;
		}
	}
}
