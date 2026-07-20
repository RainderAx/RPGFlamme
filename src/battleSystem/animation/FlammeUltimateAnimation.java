package battleSystem.animation;

import javax.swing.ImageIcon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.Random;


public class FlammeUltimateAnimation extends UltimateAnimation {
    private final Random rand = new Random();
    private Image flammeImg;

    public FlammeUltimateAnimation() {
        super(140);
        try {
            flammeImg = new ImageIcon(getClass().getResource("/assets/Flamme_Ultimate.png")).getImage();
        } catch (Exception e) {
            System.err.println("Sprite flamme manquant : " + e.getMessage());
        }
    }

    @Override
    public void render(Graphics2D g2, int w, int h, ImageObserver obs) {
        float p = progress();

        int alpha = (int) (140 * Math.sin(p * Math.PI));
        g2.setColor(new Color(200, 0, 0, Math.max(0, alpha)));
        g2.fillRect(0, 0, w, h);

        int arrowY = (int) (h * 0.45);
        int arrowLen = (int) (w * Math.min(1f, p * 1.4f));
        g2.setColor(new Color(255, 140, 0));
        g2.setStroke(new BasicStroke(10f));
        g2.drawLine(0, arrowY, arrowLen, arrowY);
        g2.fillPolygon(new int[]{arrowLen, arrowLen - 40, arrowLen - 40},
                new int[]{arrowY, arrowY - 25, arrowY + 25}, 3);

        for (int i = 0; i < 20; i++) {
            int px = rand.nextInt(Math.max(1, arrowLen + 1));
            int py = arrowY + rand.nextInt(40) - 20;
            g2.setColor(new Color(255, rand.nextInt(150) + 80, 0, 200));
            g2.fillOval(px, py, 6, 6);
        }

        if (flammeImg != null) {
            int spriteH = (int) (h * 0.4);
            int spriteW = (int) (spriteH * (621.0 / 1331.0));
            g2.drawImage(flammeImg, (int) (w * 0.05), (int) (h * 0.5), spriteW, spriteH, obs);
        }

        if (p > 0.75f) {
            float local = (p - 0.75f) / 0.25f;
            int r = (int) (300 * local);
            g2.setColor(new Color(255, 255, 200, (int) (200 * (1 - local))));
            g2.fillOval(arrowLen - r / 2, arrowY - r / 2, r, r);
        }

        AnimationTextUtils.drawOutlinedText(g2, "FLECHE ARDENTE", (int) (w * 0.06), (int) (h * 0.2),
                (int) (h * 0.09), Color.ORANGE);
    }
}
