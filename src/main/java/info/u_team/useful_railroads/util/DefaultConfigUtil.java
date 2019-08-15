package info.u_team.useful_railroads.util;

import java.io.*;
import java.nio.file.Path;
import java.util.function.*;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import info.u_team.u_team_core.util.ConfigUtil;

public class DefaultConfigUtil {
	
	public static <T> T loadConfig(Path directory, String name, Gson gson, T defaultValue, BiConsumer<T, JsonWriter> write, Function<BufferedReader, T> read) throws IOException {
		return ConfigUtil.loadConfig(directory, name, gson, writer -> {
			write.accept(defaultValue, writer);
			return defaultValue;
		}, read);
	}
	
}
