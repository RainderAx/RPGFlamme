package battleSystem.animation;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

/** Contrat commun à toute animation pilotée par l'AnimationManager. */
public interface Animation {
    void start();
    void update();
    boolean isFinished();
    void render(Graphics2D g2, int width, int height, ImageObserver observer);
}
