package battleSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.OutputStream;
import java.io.PrintStream;

public class StyledConsole extends JPanel {
    private JTextArea txtConsole;
    private JScrollPane scrollPane;
    private boolean isHovered = false;
    
    // --- Palette Graphique alignée sur votre DA ---
    private final Color bgIdle = new Color(15, 20, 30, 80);       // Très transparent au repos
    private final Color bgHover = new Color(10, 12, 18, 220);     // Sombre et opaque au survol
    
    private final Color textIdle = new Color(200, 225, 255, 130);  // Bleu-blanc doux estompé
    private final Color textHover = new Color(255, 255, 255, 255); // Blanc pur et brillant au survol
    
    private final Color borderIdle = new Color(0, 150, 255, 60);   // Bordure bleutée discrète
    private final Color borderHover = new Color(0, 150, 255, 180); // Bordure "lumière" active
    
    private final int cornerRadius = 12; // Arrondis cohérents avec vos ActionButtons

    public StyledConsole() {
        this.setLayout(new BorderLayout());
        this.setOpaque(false); // Permet de rendre le fond du panel transparent

        // Configuration de la zone de texte
        txtConsole = new JTextArea();
        txtConsole.setEditable(false);
        txtConsole.setLineWrap(true);
        txtConsole.setWrapStyleWord(true);
        txtConsole.setOpaque(false); // Arrière-plan géré par le paintComponent du panel
        txtConsole.setFont(new Font("Arial", Font.PLAIN, 12));
        txtConsole.setForeground(textIdle);
        txtConsole.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Configuration du ScrollPane
        scrollPane = new JScrollPane(txtConsole);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Customisation de la barre de défilement pour qu'elle soit discrète et moderne
        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, isHovered ? 120 : 40));
                g2.fillRoundRect(r.x, r.y, r.width, r.height, 6, 6);
                g2.dispose();
            }
            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {}
            @Override
            protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
            @Override
            protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }
            private JButton createZeroButton() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
        });

        this.add(scrollPane, BorderLayout.CENTER);

        // --- Logique d'interaction au survol (Hover) ---
        MouseAdapter hoverAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isHovered) {
                    isHovered = true;
                    txtConsole.setForeground(textHover);
                    txtConsole.setFont(new Font("Arial", Font.BOLD, 12)); // Devient plus lisible
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Vérification pour s'assurer que la souris a bien quitté tout le composant global
                Point p = e.getPoint();
                SwingUtilities.convertPointToScreen(p, (Component) e.getSource());
                SwingUtilities.convertPointFromScreen(p, StyledConsole.this);
                
                if (!getBounds().contains(p.x, p.y, getWidth(), getHeight())) {
                    isHovered = false;
                    txtConsole.setForeground(textIdle);
                    txtConsole.setFont(new Font("Arial", Font.PLAIN, 12));
                    repaint();
                }
            }
        };

        // On applique le listener partout pour éviter les conflits de focus quand la souris passe sur le texte
        this.addMouseListener(hoverAdapter);
        txtConsole.addMouseListener(hoverAdapter);
        scrollPane.addMouseListener(hoverAdapter);

        redirectSystemOut();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dessin du fond dynamique (transparent ou opaque)
        g2.setColor(isHovered ? bgHover : bgIdle);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        // Dessin de la bordure lumineuse
        g2.setColor(isHovered ? borderHover : borderIdle);
        g2.setStroke(new BasicStroke(1.5f));
        g2.draw(new RoundRectangle2D.Double(0.75, 0.75, getWidth() - 1.5, getHeight() - 1.5, cornerRadius, cornerRadius));

        g2.dispose();
        super.paintComponent(g);
    }

    public void appendText(String text) {
        txtConsole.append(text);
        // Scroll automatique vers le bas lors de l'ajout de texte
        txtConsole.setCaretPosition(txtConsole.getDocument().getLength());
    }

    private void redirectSystemOut() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                appendText(String.valueOf((char) b));
            }
            @Override
            public void write(byte[] b, int off, int len) {
                appendText(new String(b, off, len));
            }
        };
        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }
}