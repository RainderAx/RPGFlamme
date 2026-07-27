package battleSystem.animation;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;


final class AnimationImageUtils {
    private AnimationImageUtils() {}

    static void drawContained(Graphics2D g2, Image img, int boxX, int boxY, int boxW, int boxH, ImageObserver obs) {
        if (img == null) return;

        int iw = img.getWidth(obs);
        int ih = img.getHeight(obs);


        if (iw <= 0 || ih <= 0) {
            g2.drawImage(img, boxX, boxY, boxW, boxH, obs);
            return;
        }

        double imgRatio = (double) iw / ih;
        double boxRatio = (double) boxW / boxH;

        int drawW, drawH;
        if (imgRatio > boxRatio) {
            // Image relativement plus large que la boîte -> on cale sur la largeur
            drawW = boxW;
            drawH = (int) (boxW / imgRatio);
        } else {
            // Image relativement plus haute que la boîte -> on cale sur la hauteur
            drawH = boxH;
            drawW = (int) (boxH * imgRatio);
        }

        int drawX = boxX + (boxW - drawW) / 2;
        int drawY = boxY + (boxH - drawH) / 2;

        g2.drawImage(img, drawX, drawY, drawW, drawH, obs);
    }
}
