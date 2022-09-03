package de.enzaxd.dynamic_alts.config;

import com.google.gson.JsonObject;
import de.enzaxd.dynamic_alts.DynamicAlts;

import java.io.File;

public abstract class AbstractConfig {

    private final String name;
    private final File file;

    public AbstractConfig(final String name) {
        this.name = name + ".json";
        this.file = new File(DynamicAlts.self.getPlatformProvider().dataDir(), this.name());
    }

    public abstract void read(final JsonObject node);
    public abstract void write(final JsonObject node);

    public File getFile() {
        return file;
    }

    public String name() {
        return this.name;
    }
}
