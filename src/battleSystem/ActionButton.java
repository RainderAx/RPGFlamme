package battleSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/** Bouton d'action stylisé (dégradé + coins arrondis) utilisé pour toutes les commandes de combat. */
public class ActionButton extends JButton {
    private final Color baseColor;
    private final Color hoverColor;
    private final Color pressColor;
    private boolean isHovered = false;
    private boolean isPressed = false;
    private final int cornerRadius = 12;

    public ActionButton(String text, Color color) {
        super(text.toUpperCase());
        this.baseColor = color;
        this.hoverColor = color.brighter();
        this.pressColor = color.darker();

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { isHovered = true; repaint(); }
            @Override public void mouseExited(MouseEvent e) { isHovered = false; repaint(); }
            @Override public void mousePressed(MouseEvent e) { isPressed = true; repaint(); }
            @Override public void mouseReleased(MouseEvent e) { isPressed = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!isEnabled()) {
            g2.setColor(new Color(90, 90, 90, 180));
        } else {
            Color current = isPressed ? pressColor : (isHovered ? hoverColor : baseColor);
            GradientPaint gradient = new GradientPaint(0, 0, current, getWidth(), getHeight(), current.darker());
            g2.setPaint(gradient);
        }
        // Dessiner le fond arrondi
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        g2.setColor(new Color(255, 255, 255, 50));
        g2.setStroke(new BasicStroke(1f));
        g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, cornerRadius, cornerRadius));

        FontMetrics fm = g2.getFontMetrics();
        Rectangle r = fm.getStringBounds(getText(), g2).getBounds();
        int x = (getWidth() - r.width) / 2;
        int y = (getHeight() - r.height) / 2 + fm.getAscent();

    / Ombre portée légère pour le texte
        g2.setColor(new Color(0, 0, 0, 100));
        g2.drawString(getText(), x + 1, y + 1);

        g2.setColor(getForeground());
        g2.drawString(getText(), x, y);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(180, 42);
    }
}
