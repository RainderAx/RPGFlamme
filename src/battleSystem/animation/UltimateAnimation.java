package battleSystem.animation;

/**
 * Animation de premier plan jouée lors du déclenchement d'un ultime
 * (sprite, texte, explosion...). Prévient l'AnimationManager de sa fin
 * via {@link #setOnComplete(Runnable)} pour que le BattleSystem puisse
 * enchaîner l'exécution de la logique de jeu.
 */
public abstract class UltimateAnimation implements Animation {
    protected boolean started = false;
    protected boolean finished = false;
    protected int elapsedTicks = 0;
    protected final int durationTicks;
    private Runnable onComplete;

    protected UltimateAnimation(int durationTicks) {
        this.durationTicks = durationTicks;
    }

    public void setOnComplete(Runnable onComplete) {
        this.onComplete = onComplete;
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
        if (elapsedTicks >= durationTicks) {
            finished = true;
            if (onComplete != null) onComplete.run();
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    protected float progress() {
        return Math.min(1f, (float) elapsedTicks / durationTicks);
    }

    /** Décalage caméra (tremblement). 0 par défaut, surchargé si besoin (ex : Tching). */
    public int getShakeX() { return 0; }
    public int getShakeY() { return 0; }
}
