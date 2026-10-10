package battleSystem.animation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.util.Random;

/** Animation de premier plan de l'ultime de Card : portail béant + pluie d'arbres déracinés. */
public class CardUltimateAnimation extends UltimateAnimation {
    private final Random rand = new Random();

    public CardUltimateAnimation() {
        super(150);
    }

    @Override
    public void render(Graphics2D g2, int w, int h, ImageObserver obs) {
        float p = progress();

        g2.setColor(new Color(0, 0, 0, (int) (200 * Math.min(1f, p * 1.5f))));
        g2.fillRect(0, 0, w, h);

        int cx = w / 2;
        int cy = (int) (h * 0.45);
        int radius = (int) (Math.min(w, h) * 0.6 * p);
        g2.setStroke(new BasicStroke(8f));
        g2.setColor(new Color(120, 255, 160, (int) (220 * (1 - p * 0.6f))));
        g2.drawOval(cx - radius, cy - radius / 2, radius * 2, radius);

        int nbArbres = (int) (14 * p);
        for (int i = 0; i < nbArbres; i++) {
            int ax = cx - radius + rand.nextInt(Math.max(1, radius * 2));
            int ay = (int) (cy - radius / 2 + (h * p) - rand.nextInt(40));
            g2.setColor(new Color(60, 35, 20));
            g2.fillRect(ax, ay, 8, 40);
            g2.setColor(new Color(30, 110, 50));
            g2.fillOval(ax - 14, ay - 20, 36, 36);
        }

        AnimationTextUtils.drawBlurText(g2, "EFFONDREMENT SYLVESTRE", (int) (w * 0.05), (int) (h * 0.18),
                (int) (h * 0.08), new Color(120, 255, 160));
    }

    @Override
    public int getShakeX() {
        return isFinished() ? 0 : rand.nextInt(14) - 7;
    }

    @Override
    public int getShakeY() {
        return isFinished() ? 0 : rand.nextInt(14) - 7;
    }
}