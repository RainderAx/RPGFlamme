package battleSystem.animation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.util.Random;

/** Fond animé de l'ultime de Bob : lignes de vitesse radiales + particules d'énergie. */
public class BobBackgroundAnimation extends BackgroundAnimation {
    private final Random rand = new Random();

    public BobBackgroundAnimation() {
        super(90);
    }

    @Override
    public void render(Graphics2D g2, int w, int h, ImageObserver obs) {
        g2.setColor(new Color(10, 20, 45, (int) (200 * progress())));
        g2.fillRect(0, 0, w, h);

        g2.setColor(new Color(120, 200, 255, 160));
        g2.setStroke(new BasicStroke(2f));
        int cx = w / 2;
        int cy = h / 2;
        for (int i = 0; i < 40; i++) {
            double angle = (Math.PI * 2 / 40) * i + elapsedTicks * 0.03;
            int len = (int) (Math.min(w, h) * (0.25 + 0.35 * progress()));
            int x2 = cx + (int) (Math.cos(angle) * len);
            int y2 = cy + (int) (Math.sin(angle) * len);
            g2.drawLine(cx, cy, x2, y2);
        }

        g2.setColor(new Color(255, 220, 100, 200));
        for (int i = 0; i < 25; i++) {
            int px = rand.nextInt(Math.max(1, w));
            int py = rand.nextInt(Math.max(1, h));
            int size = 2 + rand.nextInt(4);
            g2.fillOval(px, py, size, size);
        }
    }
}
