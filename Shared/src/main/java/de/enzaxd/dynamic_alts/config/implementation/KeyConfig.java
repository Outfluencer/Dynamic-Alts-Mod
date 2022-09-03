package de.enzaxd.dynamic_alts.config.implementation;

import com.google.gson.JsonObject;
import de.enzaxd.dynamic_alts.config.AbstractConfig;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class KeyConfig extends AbstractConfig {

    public static String authKey;

    public KeyConfig() {
        super("auth-key");
    }

    @Override
    public void read(JsonObject node) {
        KeyConfig.authKey = new String(Base64.getDecoder().decode(node.get("auth-key").getAsString().getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public void write(JsonObject node) {
        node.addProperty("auth-key", Base64.getEncoder().encodeToString(KeyConfig.authKey.getBytes(StandardCharsets.UTF_8)));
    }
}
