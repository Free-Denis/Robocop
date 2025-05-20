// src/model/TimerModel.java
package model;

public class TimerModel {
    private long startTime;
    private final long timeLimitMillis = 120000;
    private boolean isRunning;

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.isRunning = true;
    }

    public void stop() {
        this.isRunning = false;
    }

    public void reset() {
        start();
    }

    public long getRemainingTimeMillis() {
        if (!isRunning) return timeLimitMillis;
        long elapsed = System.currentTimeMillis() - startTime;
        return Math.max(0, timeLimitMillis - elapsed);
    }

    public boolean isTimeExpired() {
        return getRemainingTimeMillis() <= 0;
    }
}