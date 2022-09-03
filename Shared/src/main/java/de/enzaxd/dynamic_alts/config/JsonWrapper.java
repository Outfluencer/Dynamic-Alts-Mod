package de.enzaxd.dynamic_alts.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

public class JsonWrapper {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static JsonObject fromFile(final File file) {
        try {
            return GSON.fromJson(new FileReader(file), JsonObject.class).getAsJsonObject();
        } catch (FileNotFoundException e) {
            return new JsonObject();
        }
    }

    public static JsonObject fromString(final String string) {
        return GSON.fromJson(string, JsonObject.class).getAsJsonObject();
    }

    public static void writeToFile(final File file, final JsonObject object) {
        try (final FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(GSON.toJson(object));
            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
