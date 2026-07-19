package battleSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import Entity.*;

public class zoneDetest extends JPanel implements ActionListener {

    private Image background, tchingImg, bobImg, darkBobImg, bobUltiImg;
    private List<Entity> heroes;
    private int hoveredIndex = -1;
    
    // Variables d'animation partagées (Merge de bobUltix et ultiX)
    private int freezeTimer = 0;
    private boolean hasBeenFrozen = false;
    private boolean isUltiActive = false;
    private int ultiX = 0; 
    private int currentWinWidth;
    
    // États spécifiques
    private boolean bobIsTransformed = false;
    private boolean isBobUlti = false; // Pour différencier l'animation visuelle

    private ActionButton btnUlt;
    private ActionButton btnUltBob;
    
    public zoneDetest(List<Entity> h) {
        this.heroes = h;
        this.setLayout(null);

        try {
            background = new ImageIcon(getClass().getResource("/assets/Background_Forest.jpg")).getImage();
            tchingImg = new ImageIcon(getClass().getResource("/assets/tching.png")).getImage();
            bobImg =  new ImageIcon(getClass().getResource("/assets/Bob.png")).getImage();
            darkBobImg =  new ImageIcon(getClass().getResource("/assets/Sprite_BoB_transfo.png")).getImage();
            bobUltiImg =  new ImageIcon(getClass().getResource("/assets/BoB_Ultimate_frame.png")).getImage();
        } catch (Exception e) {
            System.err.println("Images manquantes.");
        }

        // Bouton Tching
        btnUlt = new ActionButton("Attaque Ultime Tching", new Color(255, 140, 0));
        btnUlt.addActionListener(e -> startTchingUltimate());
        this.add(btnUlt);

        // Bouton Bob
        btnUltBob = new ActionButton("Transformation Bob", new Color(70, 130, 180));
        btnUltBob.addActionListener(e -> startBobUltimate());
        this.add(btnUltBob);
        
        // Détection du survol pour les barres de vie
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int oldIndex = hoveredIndex;
                hoveredIndex = -1;
                
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                
                for (int i = 0; i < heroes.size(); i++) {
                    int x = (int) (panelWidth * 0.0625) + (i * (int) (panelWidth * 0.15));
                    int y = (int) (panelHeight * 0.5);
                    int spriteW = (int) (panelWidth * 0.075);
                    int spriteH = (int) (panelHeight * 0.233);
                    
                    if (e.getX() >= x && e.getX() <= x + spriteW && e.getY() >= y && e.getY() <= y + spriteH) {
                        hoveredIndex = i;
                        break;
                    }
                }
                if (oldIndex != hoveredIndex) repaint();
            }
        });

        new Timer(16, this).start();
    }

    @Override
    public void doLayout() {
        super.doLayout();
        // Repositionner les boutons proportionnellement
        int w = getWidth();
        btnUlt.setBounds((int)(w * 0.75), 20, 170, 30);
        btnUltBob.setBounds((int)(w * 0.5), 20, 170, 30);
    }

    private void startTchingUltimate() {
        if (isUltiActive) return; 
        isUltiActive = true;
        isBobUlti = false;
        prepareAnimation();
    }
    
    private void startBobUltimate() {
        if (isUltiActive) return; 
        isUltiActive = true;
        isBobUlti = true;
        prepareAnimation();
        
        // Timer pour valider la transformation de Bob à la fin du mouvement
        Timer transformTimer = new Timer(3500, e -> {
            bobIsTransformed = true;
            isUltiActive = false;
        });
        transformTimer.setRepeats(false);
        transformTimer.start();
    }

    private void prepareAnimation() {
    	currentWinWidth = getWidth();
    	ultiX = (int) (-currentWinWidth * 0.3);
        hasBeenFrozen = false;
        freezeTimer = 0;
        
        // Pour Tching, on arrête l'animation après un certain temps
        if (!isBobUlti) {
            Timer stopTimer = new Timer(3000, e -> isUltiActive = false);
            stopTimer.setRepeats(false);
            stopTimer.start();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        if (isUltiActive) {
            if (isBobUlti) drawUltimateBob(g2);
            else drawUltimateTching(g2);
        } else {
            drawNormalState(g2);
        }
    }

    private void drawNormalState(Graphics2D g2) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        
        // Textes proportionnels
        int fontSize = (int) (panelHeight * 0.075);
        g2.setFont(new Font("Impact", Font.ITALIC, fontSize));
        
        drawTextWithBlur(g2, "Super Tching ATTTTTACK", (int)(panelWidth * 0.06), (int)(panelHeight * 0.13), fontSize);
        drawTextWithGradient(g2, "Super Tching ATTTTTACK", (int)(panelWidth * 0.06), (int)(panelHeight * 0.3), fontSize);
        
        drawHeroes(g2);
    }

    private void drawUltimateTching(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 210));
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        int fontSize = (int) (getHeight() * 0.116);
        g2.setFont(new Font("Impact", Font.ITALIC, fontSize));
        g2.setColor(Color.ORANGE);
        
        double ratio = 703.0 / 1614.0;
        int h = (int) (getHeight() * 0.5);
        int w = (int) (h * ratio);

        handleFreezeLogic();
        renderMovingSprite(g2, tchingImg, w, h);
    }

    private void drawUltimateBob(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 210));
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        int fontSize = (int) (getHeight() * 0.116);
        g2.setFont(new Font("Impact", Font.ITALIC, fontSize));
        g2.setColor(Color.YELLOW);
        
        double ratio = 1121.0 / 1152.0;
        int h = (int) (getHeight() * 0.5);
        int w = (int) (h * ratio);

        handleFreezeLogic();
        renderMovingSprite(g2, bobUltiImg, w, h);
    }

    private void handleFreezeLogic() {
        int freezePos = (int) (getWidth() * 0.08);
        if (ultiX >= freezePos && !hasBeenFrozen) {
            freezeTimer = 60;
            hasBeenFrozen = true;
        }
    }

    private void renderMovingSprite(Graphics2D g2, Image img, int w, int h) {
        int yPos = (int) (getHeight() * 0.4);
        int textX = (int) (getWidth() * 0.06);
        int textY = (int) (getHeight() * 0.33);
        
        if (freezeTimer > 0) {
            freezeTimer--; 
            int shake = (int)(Math.random() * 6) - 3;
            if (img != null) g2.drawImage(img, ultiX + shake, yPos, w, h, this);
            g2.drawString("ULTIME ATTAQUE", textX + shake, textY);
            g2.drawString("ULTIME ATTAQUE", textX - shake, textY);
        } else {
            ultiX += (int)(getWidth() * 0.02); 
            if (img != null) g2.drawImage(img, ultiX, yPos, w, h, this);
        }
    }
    
    private void drawHeroes(Graphics2D g2) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        
        for (int i = 0; i < heroes.size(); i++) {
            Entity h = heroes.get(i);
            // Positions et tailles relatives
            int x = (int) (panelWidth * 0.0625) + (i * (int) (panelWidth * 0.15));
            int y = (int) (panelHeight * 0.5);
            int spriteW = (int) (panelWidth * 0.075); // ~60px pour 800px
            int spriteH = (int) (panelHeight * 0.233); // ~140px pour 600px
            
            boolean isHovered = (i == hoveredIndex);

            Image imgToDraw = tchingImg; 
            if (h instanceof Bob) {
                imgToDraw = bobIsTransformed ? darkBobImg : bobImg;
            }

            if (imgToDraw != null) g2.drawImage(imgToDraw, x, y, spriteW, spriteH, this);
            
            // --- UI Barres de Vie ---
            int barWidth = (int) (panelWidth * 0.1);
            int barHeight = isHovered ? (int)(panelHeight * 0.033) : (int)(panelHeight * 0.013);
            int barY = y - (int)(panelHeight * 0.04);
            float hpRatio = Math.max(0, Math.min(1, (float) h.getHp() / 100f));
            
            g2.setColor(new Color(40, 40, 40, 200));
            g2.fillRoundRect(x - 10, barY, barWidth, barHeight, 5, 5);
            g2.setColor(new Color((int) (255 * (1 - hpRatio)), (int) (255 * hpRatio), 0));
            g2.fillRoundRect(x - 10, barY, (int) (barWidth * hpRatio), barHeight, 5, 5);

            if (isHovered) {
                g2.setColor(Color.WHITE);
                int labelFontSize = (int)(panelHeight * 0.023);
                g2.setFont(new Font("Arial", Font.BOLD, labelFontSize));
                g2.drawString(h.getName(), x - 10, barY - 5);
            }
        }
    }

    private void drawTextWithBlur(Graphics2D g2, String text, int x, int y, int fontSize) {
        g2.setFont(new Font("Impact", Font.ITALIC, fontSize));
        for (int i = 1; i <= 8; i++) {
            float opacity = 0.08f * i; 
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g2.setColor(new Color(0, 150, 255));
            g2.drawString(text, x, y + (int)(fontSize * 0.2 - i));
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
    }

    private void drawTextWithGradient(Graphics2D g2, String text, int x, int y, int fontSize) {
        g2.setFont(new Font("Impact", Font.ITALIC, fontSize));
        LinearGradientPaint gradient = new LinearGradientPaint(
            0, y - fontSize, 0, y, 
            new float[]{0f, 0.8f},
            new Color[]{Color.WHITE, new Color(0, 100, 255, 50)}
        );
        g2.setPaint(gradient);
        g2.drawString(text, x, y);
        
        g2.setPaint(null);
        g2.setColor(new Color(255, 255, 255, 100));
        TextLayout tl = new TextLayout(text, g2.getFont(), g2.getFontRenderContext());
        Shape shape = tl.getOutline(AffineTransform.getTranslateInstance(x, y));
        g2.draw(shape);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Interface Responsive");
        List<Entity> testHeroes = new ArrayList<>();
       
        testHeroes.add(new Tching("Tching Alpha", 100, 10, 10));
        testHeroes.add(new Bob("Bob le Tank", 150, 15, 20)); 
        testHeroes.add(new Tching("Tching Delta", 75, 10, 10));
        testHeroes.add(new Tching("Tching Gamma", 25, 10, 10));

        frame.add(new zoneDetest(testHeroes));
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
