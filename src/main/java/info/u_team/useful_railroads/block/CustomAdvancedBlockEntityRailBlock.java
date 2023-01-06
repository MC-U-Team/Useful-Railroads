package info.u_team.useful_railroads.block;

import java.util.function.Supplier;

import info.u_team.u_team_core.api.block.BlockItemProvider;
import info.u_team.u_team_core.api.block.EntityBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;

public class CustomAdvancedBlockEntityRailBlock extends BaseRailBlock implements BlockItemProvider, EntityBlockProvider {
	
	public static final EnumProperty<RailShape> SHAPE = EnumProperty.create("shape", RailShape.class, RailShape.NORTH_SOUTH, RailShape.EAST_WEST);
	
	protected final BlockItem blockItem;
	
	protected final Supplier<? extends BlockEntityType<?>> blockEntityType;
	
	public CustomAdvancedBlockEntityRailBlock(Properties properties, Supplier<? extends BlockEntityType<?>> blockEntityType) {
		super(false, properties);
		blockItem = createBlockItem(new Item.Properties());
		this.blockEntityType = blockEntityType;
		registerDefaultState(defaultBlockState().setValue(SHAPE, RailShape.NORTH_SOUTH).setValue(WATERLOGGED, false));
	}
	
	protected BlockItem createBlockItem(Item.Properties blockItemProperties) {
		return new BlockItem(this, blockItemProperties);
	}
	
	@Override
	public BlockItem blockItem() {
		return blockItem;
	}
	
	@Override
	public BlockEntityType<?> blockEntityType(BlockPos pos, BlockState state) {
		return blockEntityType.get();
	}
	
	@Override
	public Property<RailShape> getShapeProperty() {
		return SHAPE;
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(SHAPE, WATERLOGGED);
	}
	
}
