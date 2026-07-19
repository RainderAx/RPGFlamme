package battleSystem.animation;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

/**
 * Pilote le cycle de vie des animations d'ultime (fond + premier plan).
 * Totalement générique : ne connaît aucune entité, aucune logique de jeu.
 * C'est le BattleSystem qui décide QUAND jouer une animation ; l'AnimationManager
 * se contente de la faire vivre tick après tick.
 */
public class AnimationManager {
    private BackgroundAnimation background;
    private UltimateAnimation ultimate;
    private boolean active = false;

    public void play(BackgroundAnimation background, UltimateAnimation ultimate, Runnable onComplete) {
        this.background = background;
        this.ultimate = ultimate;
        this.active = true;

        if (ultimate != null) {
            ultimate.setOnComplete(() -> {
                active = false;
                if (onComplete != null) onComplete.run();
            });
            ultimate.start();
        }
        if (background != null) background.start();
    }

    /** À appeler à chaque tick de la boucle de jeu. */
    public void update() {
        if (!active) return;
        if (background != null) background.update();
        if (ultimate != null) ultimate.update();
    }

    public boolean isActive() {
        return active;
    }

    public void render(Graphics2D g2, int width, int height, ImageObserver observer) {
        if (!active) return;
        if (background != null) background.render(g2, width, height, observer);
        if (ultimate != null) ultimate.render(g2, width, height, observer);
    }

    public int getShakeX() { return (active && ultimate != null) ? ultimate.getShakeX() : 0; }
    public int getShakeY() { return (active && ultimate != null) ? ultimate.getShakeY() : 0; }
}
