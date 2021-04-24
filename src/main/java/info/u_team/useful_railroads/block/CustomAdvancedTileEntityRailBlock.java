package info.u_team.useful_railroads.block;

import java.util.function.Supplier;

import info.u_team.u_team_core.api.ITileEntityBlock;
import info.u_team.u_team_core.api.registry.IBlockItemProvider;
import info.u_team.useful_railroads.init.UsefulRailroadsItemGroups;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class CustomAdvancedTileEntityRailBlock extends AbstractRailBlock implements IBlockItemProvider, ITileEntityBlock {
	
	public static final EnumProperty<RailShape> SHAPE = EnumProperty.create("shape", RailShape.class, RailShape.NORTH_SOUTH, RailShape.EAST_WEST);
	
	protected final BlockItem blockItem;
	
	protected final Supplier<? extends TileEntityType<?>> tileEntityType;
	
	public CustomAdvancedTileEntityRailBlock(Properties properties, Supplier<? extends TileEntityType<?>> tileEntityType) {
		super(false, properties);
		blockItem = createBlockItem(new Item.Properties().group(UsefulRailroadsItemGroups.GROUP));
		this.tileEntityType = tileEntityType;
		setDefaultState(getDefaultState().with(SHAPE, RailShape.NORTH_SOUTH));
	}
	
	protected BlockItem createBlockItem(Item.Properties blockItemProperties) {
		return new BlockItem(this, blockItemProperties);
	}
	
	@Override
	public BlockItem getBlockItem() {
		return blockItem;
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return tileEntityType.get().create();
	}
	
	@Override
	public TileEntityType<?> getTileEntityType(IBlockReader world, BlockPos pos) {
		return tileEntityType.get();
	}
	
	@Override
	public Property<RailShape> getShapeProperty() {
		return SHAPE;
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(SHAPE);
	}
	
}
