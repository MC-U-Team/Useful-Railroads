package info.u_team.useful_railroads.hook;

import com.mojang.datafixers.DataFixerBuilder;

public class DataFixesManagerHook {
	
	public static void hook(DataFixerBuilder builder) {
		System.out.println(builder);
		System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
	}
	
}
