package info.u_team.useful_railroads.util;

import java.lang.reflect.Type;

import com.google.gson.*;

import net.minecraft.item.crafting.Ingredient;

public class IngredientSerializerType implements JsonSerializer<Ingredient>, JsonDeserializer<Ingredient> {
	
	
	public IngredientSerializerType() {
		System.out.println("INIT");
	}
	
	@Override
	public JsonElement serialize(Ingredient ingredient, Type typeOfSrc, JsonSerializationContext context) {
		System.out.println("TESTSETESTSKLJFEHDSDJKLFHSDLJKFH");
		return ingredient.serialize();
	}
	
	@Override
	public Ingredient deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return Ingredient.deserialize(json);
	}
	
}
