package info.u_team.useful_railroads.util;

import java.util.function.Predicate;

public class Predicates {
	
	public static <T> Predicate<T> not(Predicate<T> predicate) {
		return predicate.negate();
	}
	
}
