package battleSystem.animation;

import javax.swing.ImageIcon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;

/** Animation de premier plan de l'ultime de Bob : flash -> transformation -> explosion -> "Super Bob". */
public class BobUltimateAnimation extends UltimateAnimation {
    private Image transfoImg;
    private Image frameImg;

    public BobUltimateAnimation() {
        super(150);
        try {
            transfoImg = new ImageIcon(getClass().getResource("/assets/Sprite_BoB_transfo.png")).getImage();
            frameImg = new ImageIcon(getClass().getResource("/assets/BoB_Ultimate_frame.png")).getImage();
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

        double ratio = 1121.0 / 1152.0;
        int spriteH = (int) (h * 0.55);
        int spriteW = (int) (spriteH * ratio);
        int x = (w - spriteW) / 2;
        int y = (int) (h * 0.22);

        Image img = (p < 0.5f) ? transfoImg : frameImg;
        if (img != null) g2.drawImage(img, x, y, spriteW, spriteH, obs);

        if (p >= 0.5f) {
            drawExplosion(g2, w / 2, y + spriteH / 2, p);
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
