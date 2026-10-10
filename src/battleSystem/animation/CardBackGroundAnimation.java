package battleSystem.animation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.util.Random;

/** Fond animé de l'ultime de Card : un vortex de portails engloutissant la forêt. */
public class CardBackgroundAnimation extends BackgroundAnimation {
    private final Random rand = new Random();

    public CardBackgroundAnimation() {
        super(100);
    }

    @Override
    public void render(Graphics2D g2, int w, int h, ImageObserver obs) {
        g2.setColor(new Color(10, 30, 15, (int) (210 * progress())));
        g2.fillRect(0, 0, w, h);

        int cx = w / 2;
        int cy = h / 2;
        g2.setStroke(new BasicStroke(4f));
        for (int i = 0; i < 6; i++) {
            int radius = (elapsedTicks * 8 + i * 50) % Math.max(1, w);
            int alpha = Math.max(0, 200 - i * 25);
            g2.setColor(new Color(80, 220, 120, alpha));
            g2.drawOval(cx - radius, cy - radius / 2, radius * 2, radius);
        }

        g2.setColor(new Color(40, 25, 15, 220));
        for (int i = 0; i < 10; i++) {
            int treeX = (rand.nextInt(Math.max(1, w)) + elapsedTicks * 3) % Math.max(1, w);
            int treeY = (elapsedTicks * 5 + i * 40) % Math.max(1, h);
            g2.fillRect(treeX, treeY, 6, 30);
            g2.fillOval(treeX - 10, treeY - 15, 26, 26);
        }
    }
}