package de.enzaxd.dynamic_alts.util;

public class Timer {

    private long lastMs;

    public Timer() {
        this.reset();
    }

    public boolean hasReached(final long ms) {
        return this.getDelay() >= ms;
    }

    public long getDelay() {
        return System.currentTimeMillis() - this.lastMs;
    }

    public void reset() {
        this.lastMs = System.currentTimeMillis();
    }
}
