package battleSystem.animation;

import javax.swing.ImageIcon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;

public class BobUltimateAnimation extends UltimateAnimation {
    private Image transfoImg;
    private Image frameImg;

    public BobUltimateAnimation() {
        super(150);
        try {
            frameImg = new ImageIcon(getClass().getResource("/assets/Sprite_BoB_transfo.png")).getImage();
            transfoImg = new ImageIcon(getClass().getResource("/assets/BoB_Ultimate_frame.png")).getImage();
        } catch (Exception e) {
            System.err.println("Sprites Bob Ultimate manquants : " + e.getMessage());
        }
    }

    @Override
    public void render(Graphics2D g2, int w, int h, ImageObserver obs) {
        float p = progress();

        if (p < 0.15f) {
            int alpha = (int) (255 * (1 - p / 0.15f));
            g2.setColor(new Color(255, 255, 255, alpha));
            g2.fillRect(0, 0, w, h);
        }

        int boxW = (int) (w * 0.45);
        int boxH = (int) (h * 0.55);
        int boxX = (w - boxW) / 2;
        int boxY = (int) (h * 0.22);

        Image img = (p < 0.5f) ? transfoImg : frameImg;
        AnimationImageUtils.drawContained(g2, img, boxX, boxY, boxW, boxH, obs);

        if (p >= 0.5f) {
            drawExplosion(g2, w / 2, boxY + boxH / 2, p);
        }

        int fontSize = (int) (h * 0.09);
        AnimationTextUtils.drawBlurText(g2, "SUPER BOB !", (int) (w * 0.08), (int) (h * 0.15), fontSize, Color.CYAN);
    }

    private void drawExplosion(Graphics2D g2, int cx, int cy, float p) {
        int rays = 16;
        float local = (p - 0.5f) / 0.5f;
        int maxLen = (int) (220 * local);
        g2.setStroke(new BasicStroke(4f));
        g2.setColor(new Color(255, 200, 60, (int) (220 * (1 - local))));
        for (int i = 0; i < rays; i++) {
            double angle = (Math.PI * 2 / rays) * i;
            int x2 = cx + (int) (Math.cos(angle) * maxLen);
            int y2 = cy + (int) (Math.sin(angle) * maxLen);
            g2.drawLine(cx, cy, x2, y2);
        }
    }
}