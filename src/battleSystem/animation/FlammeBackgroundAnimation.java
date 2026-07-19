package battleSystem.animation;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.util.Random;

/** Fond animé de l'ultime de Flamme : nappe rouge + flammes montantes. */
public class FlammeBackgroundAnimation extends BackgroundAnimation {
    private final Random rand = new Random();

    public FlammeBackgroundAnimation() {
        super(90);
    }

    @Override
    public void render(Graphics2D g2, int w, int h, ImageObserver obs) {
        g2.setColor(new Color(120, 0, 0, (int) (200 * progress())));
        g2.fillRect(0, 0, w, h);

        for (int i = 0; i < 18; i++) {
            int fx = rand.nextInt(Math.max(1, w));
            int fy = h - rand.nextInt(Math.max(1, h / 2));
            int size = 10 + rand.nextInt(30);
            GradientPaint gp = new GradientPaint(
                    fx, fy, new Color(255, 200, 0, 200),
                    fx, fy - size, new Color(255, 60, 0, 0));
            g2.setPaint(gp);
            g2.fillOval(fx, fy - size, size, size * 2);
        }
        g2.setPaint(null);
    }
}
