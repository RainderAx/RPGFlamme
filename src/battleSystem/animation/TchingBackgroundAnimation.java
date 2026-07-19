package battleSystem.animation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.util.Random;

/** Fond animé de l'ultime de Tching : ondes concentriques jaunes + éclairs. */
public class TchingBackgroundAnimation extends BackgroundAnimation {
    private final Random rand = new Random();

    public TchingBackgroundAnimation() {
        super(90);
    }

    @Override
    public void render(Graphics2D g2, int w, int h, ImageObserver obs) {
        g2.setColor(new Color(140, 120, 0, (int) (180 * progress())));
        g2.fillRect(0, 0, w, h);

        g2.setColor(new Color(255, 240, 150, 160));
        g2.setStroke(new BasicStroke(3f));
        int cx = w / 2;
        int cy = h / 2;
        for (int i = 0; i < 5; i++) {
            int radius = (elapsedTicks * 6 + i * 60) % Math.max(1, w);
            g2.drawOval(cx - radius, cy - radius, radius * 2, radius * 2);
        }

        if (rand.nextInt(6) == 0) {
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(2f));
            int x = rand.nextInt(Math.max(1, w));
            int y = 0;
            for (int i = 0; i < 6; i++) {
                int nx = x + rand.nextInt(40) - 20;
                int ny = y + h / 6;
                g2.drawLine(x, y, nx, ny);
                x = nx;
                y = ny;
            }
        }
    }
}
