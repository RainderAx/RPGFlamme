package battleSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Barre de vie stylisée avec dégradé basée sur le design Figma.
 */
public class HealthBar {
    private int width = 150;
    private int height = 15;
    private int cornerRadius = 8;
    
    // Couleurs du dégradé Figma (Rose/Violet)
    private static final Color COLOR_START = new Color(255, 105, 180); // Rose
    private static final Color COLOR_END = new Color(138, 43, 226);   // Violet
    private static final Color COLOR_BG = new Color(50, 50, 50, 150); // Fond sombre transparent

    public void draw(Graphics2D g2, int x, int y, int currentHp, int maxHp) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Dessiner le fond de la barre
        g2.setColor(COLOR_BG);
        g2.fill(new RoundRectangle2D.Double(x, y, width, height, cornerRadius, cornerRadius));

        // 2. Calculer la largeur de la barre de vie actuelle
        double percentage = Math.max(0, Math.min(1, (double) currentHp / maxHp));
        int currentWidth = (int) (width * percentage);

        if (currentWidth > 0) {
            // 3. Créer le dégradé
            GradientPaint gradient = new GradientPaint(
                x, y, COLOR_START, 
                x + width, y, COLOR_END
            );
            g2.setPaint(gradient);
            
            // 4. Dessiner la partie remplie
            // On utilise un clip pour s'assurer que le remplissage respecte les bords arrondis
            Shape oldClip = g2.getClip();
            RoundRectangle2D barShape = new RoundRectangle2D.Double(x, y, width, height, cornerRadius, cornerRadius);
            g2.clip(barShape);
            g2.fill(new Rectangle(x, y, currentWidth, height));
            g2.setClip(oldClip);
        }

        // 5. Dessiner une bordure fine
        g2.setColor(new Color(255, 255, 255, 80));
        g2.setStroke(new BasicStroke(1.5f));
        g2.draw(new RoundRectangle2D.Double(x, y, width, height, cornerRadius, cornerRadius));

        // 6. Afficher le texte HP au-dessus ou à côté (Optionnel, ici centré au-dessus)
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(Color.WHITE);
        String hpText = currentHp + " / " + maxHp;
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(hpText, x + (width - fm.stringWidth(hpText)) / 2, y - 5);
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
