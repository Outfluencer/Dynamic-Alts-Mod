package de.enzaxd.dynamic_alts.struct;

import com.google.gson.JsonObject;

public class Account {

    private final int id;
    private final String name;
    private final String uuid;

    private String accessToken;
    private long lastLogin;

    public Account(int id, String name, String uuid) {
        this.id = id;
        this.name = name;
        this.uuid = uuid;
    }

    public void write(final JsonObject object) {
        final JsonObject node = new JsonObject();

        node.addProperty("id", this.id);
        node.addProperty("name", this.name);
        node.addProperty("uuid", this.uuid);

        if (accessToken != null)
            node.addProperty("access-token", this.accessToken);

        if (lastLogin != 0L)
            node.addProperty("last-login", this.lastLogin);

        object.add(this.id + "", node);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void refresh() {
        this.lastLogin = System.currentTimeMillis();
    }
}
