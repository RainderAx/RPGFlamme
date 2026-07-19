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

    private final Color bgIdle = new Color(15, 20, 30, 80);
    private final Color bgHover = new Color(10, 12, 18, 220);

    private final Color textIdle = new Color(200, 225, 255, 130);
    private final Color textHover = new Color(255, 255, 255, 255);

    private final Color borderIdle = new Color(0, 150, 255, 60);
    private final Color borderHover = new Color(0, 150, 255, 180);

    private final int cornerRadius = 12;

    public StyledConsole() {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        txtConsole = new JTextArea();
        txtConsole.setEditable(false);
        txtConsole.setLineWrap(true);
        txtConsole.setWrapStyleWord(true);
        txtConsole.setOpaque(false);
        txtConsole.setFont(new Font("Arial", Font.PLAIN, 12));
        txtConsole.setForeground(textIdle);
        txtConsole.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scrollPane = new JScrollPane(txtConsole);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

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

        MouseAdapter hoverAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isHovered) {
                    isHovered = true;
                    txtConsole.setForeground(textHover);
                    txtConsole.setFont(new Font("Arial", Font.BOLD, 12));
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
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

        this.addMouseListener(hoverAdapter);
        txtConsole.addMouseListener(hoverAdapter);
        scrollPane.addMouseListener(hoverAdapter);

        redirectSystemOut();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(isHovered ? bgHover : bgIdle);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        g2.setColor(isHovered ? borderHover : borderIdle);
        g2.setStroke(new BasicStroke(1.5f));
        g2.draw(new RoundRectangle2D.Double(0.75, 0.75, getWidth() - 1.5, getHeight() - 1.5, cornerRadius, cornerRadius));

        g2.dispose();
        super.paintComponent(g);
    }

    public void appendText(String text) {
        txtConsole.append(text);
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
