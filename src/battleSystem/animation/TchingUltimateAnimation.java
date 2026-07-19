package battleSystem.animation;

import javax.swing.ImageIcon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.Random;

/** Animation de premier plan de l'ultime de Tching : onde de choc + éclairs + tremblement de caméra. */
public class TchingUltimateAnimation extends UltimateAnimation {
    private final Random rand = new Random();
    private Image tchingImg;

    public TchingUltimateAnimation() {
        super(130);
        try {
            tchingImg = new ImageIcon(getClass().getResource("/assets/tching.png")).getImage();
        } catch (Exception e) {
            System.err.println("Sprite tching manquant : " + e.getMessage());
        }
    }

    @Override
    public void render(Graphics2D g2, int w, int h, ImageObserver obs) {
        float p = progress();

        g2.setColor(new Color(0, 0, 0, (int) (200 * Math.min(1f, p * 2))));
        g2.fillRect(0, 0, w, h);

        double ratio = 703.0 / 1614.0;
        int spriteH = (int) (h * 0.5);
        int spriteW = (int) (spriteH * ratio);
        int x = (int) (w * 0.1 + Math.sin(p * Math.PI) * w * 0.1);
        int y = (int) (h * 0.3);
        if (tchingImg != null) g2.drawImage(tchingImg, x, y, spriteW, spriteH, obs);

        int cx = w / 2;
        int cy = h / 2;
        int radius = (int) (Math.min(w, h) * p);
        g2.setStroke(new BasicStroke(6f));
        g2.setColor(new Color(255, 255, 0, (int) (200 * (1 - p))));
        g2.drawOval(cx - radius, cy - radius, radius * 2, radius * 2);

        if (rand.nextInt(3) == 0) {
            g2.setColor(Color.WHITE);
            int lx = rand.nextInt(Math.max(1, w));
            g2.drawLine(lx, 0, lx + rand.nextInt(60) - 30, h);
        }

        AnimationTextUtils.drawBlurText(g2, "ONDE DE CHOC", (int) (w * 0.06), (int) (h * 0.2),
                (int) (h * 0.09), Color.YELLOW);
    }

    @Override
    public int getShakeX() {
        return isFinished() ? 0 : rand.nextInt(10) - 5;
    }

    @Override
    public int getShakeY() {
        return isFinished() ? 0 : rand.nextInt(10) - 5;
    }
}
