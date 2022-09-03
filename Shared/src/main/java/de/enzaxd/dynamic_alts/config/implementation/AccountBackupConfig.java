package de.enzaxd.dynamic_alts.config.implementation;

import com.google.gson.JsonObject;
import de.enzaxd.dynamic_alts.DynamicAlts;
import de.enzaxd.dynamic_alts.config.AbstractConfig;
import de.enzaxd.dynamic_alts.struct.Account;

public class AccountBackupConfig extends AbstractConfig {

    public AccountBackupConfig() {
        super("account-cache");
    }

    @Override
    public void read(JsonObject node) {
        // Not Implemented
    }

    @Override
    public void write(JsonObject node) {
        for (Account account : DynamicAlts.self.getAccounts()) 
            account.write(node);
    }
}
