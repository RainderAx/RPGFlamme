package battleSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import Entity.Entity;

/**
 * Barre de vie stylisée avec dégradé et gestion du survol.
 */
public class HealthBar {
    private int width = 120;
    private int height = 12;
    private int cornerRadius = 8;

    private static final Color COLOR_START = new Color(255, 105, 180); // Rose
    private static final Color COLOR_END = new Color(138, 43, 226);    // Violet
    private static final Color COLOR_BG = new Color(50, 50, 50, 180);  // Fond sombre

    /**
     * Dessine la barre de vie.
     *
     * @param g2        contexte graphique
     * @param x         position X (centrée par rapport à l'entité)
     * @param y         position Y (au-dessus de l'entité)
     * @param h         l'entité pour récupérer les PV / PV max / nom
     * @param isHovered état de survol pour l'animation
     */
    public void draw(Graphics2D g2, int x, int y, Entity h, boolean isHovered) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int currentHeight = isHovered ? 20 : 12;
        int currentWidth = isHovered ? 140 : 120;
        int drawX = x - (currentWidth / 2);
        int drawY = y - currentHeight;

        float hpRatio = Math.max(0, Math.min(1, (float) h.getHp() / h.getMaxHp()));

        g2.setColor(COLOR_BG);
        g2.fill(new RoundRectangle2D.Double(drawX, drawY, currentWidth, currentHeight, cornerRadius, cornerRadius));

        if (hpRatio > 0) {
            GradientPaint gradient = new GradientPaint(
                    drawX, drawY, COLOR_START,
                    drawX + currentWidth, drawY, COLOR_END
            );
            g2.setPaint(gradient);

            Shape oldClip = g2.getClip();
            RoundRectangle2D barShape = new RoundRectangle2D.Double(drawX, drawY, currentWidth, currentHeight, cornerRadius, cornerRadius);
            g2.clip(barShape);

            g2.fill(new Rectangle(drawX, drawY, (int) (currentWidth * hpRatio), currentHeight));

            g2.setClip(oldClip);
        }

        if (isHovered) {
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(new RoundRectangle2D.Double(drawX, drawY, currentWidth, currentHeight, cornerRadius, cornerRadius));

            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString(h.getName(), drawX, drawY - 8);
        }

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 10));
        String hpText = h.getHp() + " / " + h.getMaxHp() + " HP";
        FontMetrics fm = g2.getFontMetrics();
        int tx = drawX + (currentWidth - fm.stringWidth(hpText)) / 2;
        int ty = drawY + (currentHeight / 2) + (fm.getAscent() / 2) - 2;
        g2.drawString(hpText, tx, ty);
    }
}
