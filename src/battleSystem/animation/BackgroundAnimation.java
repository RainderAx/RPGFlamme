package battleSystem.animation;

/**
 * Fond animé affiché pendant toute la durée d'une capacité ultime
 * (ex : brasier pour Flamme, ondes pour Tching...). Ne connaît aucune entité.
 */
public abstract class BackgroundAnimation implements Animation {
    protected boolean started = false;
    protected boolean finished = false;
    protected int elapsedTicks = 0;
    protected final int durationTicks;

    protected BackgroundAnimation(int durationTicks) {
        this.durationTicks = durationTicks;
    }

    @Override
    public void start() {
        started = true;
        finished = false;
        elapsedTicks = 0;
    }

    @Override
    public void update() {
        if (!started || finished) return;
        elapsedTicks++;
        if (elapsedTicks >= durationTicks) finished = true;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    /** Progression normalisée entre 0 et 1. */
    protected float progress() {
        return Math.min(1f, (float) elapsedTicks / durationTicks);
    }
}
