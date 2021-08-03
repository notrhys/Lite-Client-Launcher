package me.mat1337.easy.gui.util;

public class Timer {

    private float partialTicks;

    private long lastTime;
    private long delay;

    public Timer(int timing) {
        this.delay = (long) (1000000000L / (double) timing);
        this.reset();
    }

    public void reset() {
        lastTime = System.nanoTime();
    }

    public boolean hasReached() {
        long d = (System.nanoTime() - lastTime);
        partialTicks = d / 1000000000.0F;
        return d >= delay;
    }

    public boolean hasReached(boolean reset) {
        if (hasReached()) {
            reset();
            return true;
        } else {
            return false;
        }
    }

    public float getPartialTicks() {
        return partialTicks;
    }

}