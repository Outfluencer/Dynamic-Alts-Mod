package de.enzaxd.dynamic_alts;

import java.io.File;

public interface IPlatformProvider {

    File dataDir();

    void reloadSecureProfile();

    int scaledWidth();
    int scaledHeight();

    void renderScrollbar(double i, double j, double p, double o, double posBottom, double posTop);

}
