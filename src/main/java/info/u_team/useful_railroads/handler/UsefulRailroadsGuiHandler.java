package info.u_team.useful_railroads.handler;

import info.u_team.useful_railroads.container.ContainerRailTeleport;
import info.u_team.useful_railroads.gui.GuiRailTeleport;
import info.u_team.useful_railroads.tilentity.TileEntityRailTeleport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class UsefulRailroadsGuiHandler implements IGuiHandler {
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
			if (tile instanceof TileEntityRailTeleport) {
				return new ContainerRailTeleport((TileEntityRailTeleport) tile, player);
			}
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
			if (tile instanceof TileEntityRailTeleport) {
				return new GuiRailTeleport((TileEntityRailTeleport) tile, player);
			}
		}
		return null;
	}
	
}
