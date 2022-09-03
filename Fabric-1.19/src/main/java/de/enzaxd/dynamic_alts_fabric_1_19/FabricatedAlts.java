package de.enzaxd.dynamic_alts_fabric_1_19;

import de.enzaxd.dynamic_alts.DynamicAlts;
import net.fabricmc.api.ModInitializer;

public class FabricatedAlts implements ModInitializer {

    @Override
    public void onInitialize() {
        new DynamicAlts(new FabricPlatformProvider());
    }
}
