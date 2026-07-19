package battleSystem;

import javax.swing.*;
import java.awt.*;

/**
 * Panneau de menu d'actions stylisé.
 */
public class ActionMenuPanel extends JPanel {

    public ActionMenuPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
    }

    /
    public void addActionButton(String text, Color color, Runnable action) {
        ActionButton btn = new ActionButton(text, color);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(e -> action.run());

        add(btn);
        add(Box.createRigidArea(new Dimension(0, 10)));
    }

    
    public static class Palette {
        public static final Color BLUE = new Color(74, 109, 255);      // Actions de base
        public static final Color TEAL = new Color(0, 191, 165);       // Provocation / Support
        public static final Color PURPLE = new Color(138, 75, 255);    // Préparation Mentale / Spécial
        public static final Color ORANGE = new Color(255, 145, 0);     // Attaque lourde
    }
}
