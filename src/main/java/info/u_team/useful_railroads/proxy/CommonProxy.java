package info.u_team.useful_railroads.proxy;

import com.mojang.datafixers.DataFixUtils;

import info.u_team.u_team_core.api.IModProxy;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.datafix.*;

public class CommonProxy implements IModProxy {
	
	@Override
	public void construct() {
	}
	
	@Override
	public void setup() {
		System.out.println(DataFixesManager.getDataFixer().getSchema(DataFixUtils.makeKey(SharedConstants.getVersion().getWorldVersion())).findChoiceType(TypeReferences.BLOCK_ENTITY));
	}
	
	@Override
	public void complete() {
	}
}
