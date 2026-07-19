package battleSystem.animation;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

/** Petits utilitaires de texte réutilisés par toutes les animations (DRY). */
final class AnimationTextUtils {
    private AnimationTextUtils() {}

    static void drawBlurText(Graphics2D g2, String text, int x, int y, int fontSize, Color color) {
        g2.setFont(new Font("Impact", Font.ITALIC, fontSize));
        for (int i = 1; i <= 8; i++) {
            float opacity = 0.08f * i;
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g2.setColor(color);
            g2.drawString(text, x, y + (int) (fontSize * 0.2 - i));
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
    }

    static void drawOutlinedText(Graphics2D g2, String text, int x, int y, int fontSize, Color fill) {
        g2.setFont(new Font("Impact", Font.ITALIC, fontSize));
        g2.setColor(fill);
        g2.drawString(text, x, y);
        g2.setColor(new Color(255, 255, 255, 120));
        TextLayout tl = new TextLayout(text, g2.getFont(), g2.getFontRenderContext());
        Shape shape = tl.getOutline(AffineTransform.getTranslateInstance(x, y));
        g2.draw(shape);
    }
}
