package de.enzaxd.dynamic_alts;

import com.google.gson.*;
import de.enzaxd.dynamic_alts.config.AbstractConfig;
import de.enzaxd.dynamic_alts.config.JsonWrapper;
import de.enzaxd.dynamic_alts.config.implementation.KeyConfig;
import de.enzaxd.dynamic_alts.struct.Account;
import de.enzaxd.dynamic_alts.util.Timer;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DynamicAlts {

    private final static Gson GSON = new Gson();

    public static DynamicAlts self;
    private final IPlatformProvider platformProvider;

    private final List<AbstractConfig> configs;

    private final List<Account> accounts = new ArrayList<>();
    private final Timer guiReloader;

    public DynamicAlts(final IPlatformProvider platformProvider) {
        self = this;
        this.platformProvider = platformProvider;
        this.configs = Collections.singletonList(new KeyConfig());

        try {
            this.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.guiReloader = new Timer();
    }

    public Timer getGuiReloader() {
        return guiReloader;
    }

    public boolean shouldReloadGUI() {
        return this.guiReloader.hasReached(1200000); // Minutes
    }

    public void reload() {
        this.guiReloader.reset();
    }

    public void init() throws Exception {
        this.loadConfigs();
        this.loadAccounts();

        Runtime.getRuntime().addShutdownHook(new Thread(this::saveConfigs));
    }

    public void loadAccounts() throws IOException {
        if (KeyConfig.authKey == null) return;

        final CloseableHttpClient client = HttpClientBuilder.create().build();

        final HttpPost post = new HttpPost("http://api.dynamic-alts.com/api/v1/accounts");

        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept", "application/json");
        post.setHeader("Accept-Charset", "UTF-8");

        final JsonObject obj = new JsonObject();
        obj.addProperty("api-token", KeyConfig.authKey);

        StringEntity entity = new StringEntity(GSON.toJson(obj));
        post.setEntity(entity);

        final JsonObject json = new JsonParser().parse(EntityUtils.toString(client.execute(post).getEntity())).getAsJsonObject();
        JsonArray accounts = json.get("accounts").getAsJsonArray(); // list of all accounts

        for (JsonElement account : accounts) {
            System.out.println(account);
            final JsonObject node = account.getAsJsonObject();

            final int id = node.get("id").getAsInt();
            final String name = node.get("name").getAsString();
            final String uuid = node.get("uuid").getAsString();

            this.accounts.add(new Account(id, name, uuid));
        }
    }

    public String getAuthToken(int id) throws Throwable {
        final CloseableHttpClient client = HttpClientBuilder.create().build();

        final HttpPost post = new HttpPost("http://api.dynamic-alts.com/api/v1/login");

        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept", "application/json");
        post.setHeader("Accept-Charset", "UTF-8");

        final JsonObject obj = new JsonObject();

        obj.addProperty("api-token", "your token");
        obj.addProperty("id", id);

        final StringEntity entity = new StringEntity(GSON.toJson(obj));

        post.setEntity(entity);
        post.setHeader("Content-type", "application/json");

        final String data = EntityUtils.toString(client.execute(post).getEntity());
        final JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();

        return jsonObject.get("access_token").getAsString();
    }

    public void loadConfigs() throws Exception {
        for (AbstractConfig object : this.configs) {
            final boolean exists = object.getFile().exists();

            final JsonObject node = JsonWrapper.fromFile(object.getFile());

            if (exists)
                object.read(node);
        }
    }

    public void saveConfigs() {
        for (AbstractConfig object : this.configs) {
            object.getFile().delete();
            try {
                object.getFile().createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            final JsonObject json = new JsonObject();
            object.write(json);

            JsonWrapper.writeToFile(object.getFile(), json);
        }
    }

    public IPlatformProvider getPlatformProvider() {
        return platformProvider;
    }

    public List<Account> getAccounts() {
        return accounts;
    }
}
