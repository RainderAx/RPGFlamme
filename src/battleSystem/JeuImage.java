package battleSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Entity.*;


public class JeuImage extends JPanel implements ActionListener, BattleSystemListener {

    private Image background, flammeImg, tchingImg, bobImg, darkBobImg, enemyImg, chefGobelin;
    private final List<Entity> heroes;
    private final List<Entity> monsters;
    private final BattleSystem battleSystem;

    private final HealthBar healthBar = new HealthBar();
    private Entity hoveredEntity = null;

    private JLabel lblStatus;
    private ActionMenuPanel pnlActions;
    private StyledConsole console;
    private double animationAngle = 0;

    public JeuImage(List<Entity> h, List<Entity> m) {
        this.heroes = h;
        this.monsters = m;
        this.battleSystem = new BattleSystem(h, m);
        this.battleSystem.setListener(this);

        setLayout(null);
        setBackground(Color.BLACK);

        loadImages();
        buildUI();

        addMouseMotionListener(new MouseAdapter() {
            @Override public void mouseMoved(MouseEvent e) { handleHover(e.getPoint()); }
        });

        addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { handleClick(e.getPoint()); }
        });

        updateActionButtons();
        updateStatus();

        new Timer(16, this).start();
    }

    private void loadImages() {
        try {
            background = new ImageIcon(getClass().getResource("/assets/Background_Forest.jpg")).getImage();
            flammeImg  = new ImageIcon(getClass().getResource("/assets/flamme.png")).getImage();
            tchingImg  = new ImageIcon(getClass().getResource("/assets/tching.png")).getImage();
            bobImg     = new ImageIcon(getClass().getResource("/assets/Bob.png")).getImage();
            darkBobImg = new ImageIcon(getClass().getResource("/assets/Sprite_BoB_transfo.png")).getImage();
            enemyImg   = new ImageIcon(getClass().getResource("/assets/Sprite_Monster.png")).getImage();
            chefGobelin = new ImageIcon(getClass().getResource("/assets/Chef_Gobelin.png")).getImage();
        } catch (Exception e) {
            System.err.println("Images manquantes : " + e.getMessage());
        }
    }

    private void buildUI() {
        lblStatus = new JLabel("", SwingConstants.CENTER);
        lblStatus.setBounds(0, 10, 1000, 30);
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblStatus);

        pnlActions = new ActionMenuPanel();
        pnlActions.setBounds(20, 560, 220, 160);
        add(pnlActions);

        console = new StyledConsole();
        console.setBounds(260, 560, 500, 160);
        add(console);
    }


    private void handleHover(Point p) {
        Entity old = hoveredEntity;
        hoveredEntity = null;
        Map<Entity, Rectangle> bounds = allEntitiesWithBounds();
        for (Map.Entry<Entity, Rectangle> entry : bounds.entrySet()) {
            if (entry.getValue().contains(p)) {
                hoveredEntity = entry.getKey();
                break;
            }
        }
        if (old != hoveredEntity) repaint();
    }

    private void handleClick(Point p) {
        if (!battleSystem.isSelectingTarget()) return;
        battleSystem.trySelectTarget(p, getMonsterHitboxes());
    }



    private void updateActionButtons() {
        pnlActions.removeAll();
        Entity currentHero = battleSystem.getCurrentHero();

        if (!battleSystem.isPlayerPhase() || currentHero == null) {
            pnlActions.revalidate();
            pnlActions.repaint();
            return;
        }


        pnlActions.addActionButton("Attaquer", ActionMenuPanel.Palette.BLUE, () -> showHeroMethods(currentHero));

        if (currentHero instanceof UltimateCapable) {
            UltimateCapable u = (UltimateCapable) currentHero;
            String label = u.isUltimateReady()
                    ? "Ultime : " + u.getUltimateName()
                    : "Ultime (" + u.getUltiTicksRemaining() + ")";
            ButtonUlti btnUlti = new ButtonUlti(label);
            btnUlti.setEnabled(u.isUltimateReady());
            btnUlti.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnUlti.addActionListener(e -> battleSystem.prepareAction("ULTI"));
            pnlActions.add(btnUlti);
        }

        pnlActions.revalidate();
        pnlActions.repaint();
    }

    private void showHeroMethods(Entity hero) {
        pnlActions.removeAll();
        
       
        pnlActions.addActionButton("< Retour", Color.GRAY, this::updateActionButtons);
        

        if (hero instanceof Flamme) {
            pnlActions.addActionButton("Briquet", ActionMenuPanel.Palette.ORANGE, () -> battleSystem.prepareAction("BRIQUET"));
            pnlActions.addActionButton("Encens", ActionMenuPanel.Palette.TEAL, () -> battleSystem.prepareAction("ENCENS"));
            pnlActions.addActionButton("Prep. Mentale", ActionMenuPanel.Palette.PURPLE, () -> battleSystem.prepareAction("PREP_MENTALE"));
            
        } else if (hero instanceof Bob) {
        	Bob b = (Bob) hero;
            pnlActions.addActionButton("Marteau", ActionMenuPanel.Palette.ORANGE, () -> battleSystem.prepareAction("MARTEAU"));
            pnlActions.addActionButton("Provoc", ActionMenuPanel.Palette.TEAL, () -> battleSystem.prepareAction("PROVOC"));
            if (b.isTransformed()) {
                pnlActions.addActionButton("Super Provoc", ActionMenuPanel.Palette.PURPLE, () -> battleSystem.prepareAction("SUPER_PROVOC"));
                pnlActions.addActionButton("Perceuse", ActionMenuPanel.Palette.PURPLE, () -> battleSystem.prepareAction("PERCEUSE"));
            }
            
        } else if (hero instanceof Tching) {
            pnlActions.addActionButton("Coup Rapide", ActionMenuPanel.Palette.ORANGE, () -> battleSystem.prepareAction("RAPIDE"));
            pnlActions.addActionButton("Technique Tigre", ActionMenuPanel.Palette.TEAL, () -> battleSystem.prepareAction("TIGRE"));
            pnlActions.addActionButton("Entraînement", ActionMenuPanel.Palette.PURPLE, () -> battleSystem.prepareAction("ENTRAINEMENT"));
        }

        pnlActions.revalidate();
        pnlActions.repaint();
    }

    private void updateStatus() {
        Entity currentHero = battleSystem.getCurrentHero();
        Entity target = battleSystem.getTarget();
        if (currentHero != null && target != null) {
            lblStatus.setText("Tour de : " + currentHero.getName() + "   |   Cible : " + target.getName());
        }
    }


    @Override
    public void onTargetSelectionStarted(String actionType) {
        lblStatus.setText("Choisissez une cible avec la flèche !");
        pnlActions.setVisible(false);
    }

    @Override
    public void onTargetSelectionCancelled() {
        pnlActions.setVisible(true);
        updateStatus();
    }

    @Override
    public void onUltimateStarted(Entity caster) {
        lblStatus.setText(caster.getName() + " déclenche sa capacité ultime !");
    }

    @Override
    public void onUltimateEnded(Entity caster) {
        pnlActions.setVisible(true);
    }

    @Override
    public void onTurnChanged(Entity currentHero, boolean isPlayerPhase) {
        pnlActions.setVisible(isPlayerPhase);
        updateActionButtons();
        updateStatus();
    }

    @Override
    public void onBattleEnded(boolean victory) {
        pnlActions.setVisible(false);
        lblStatus.setText(victory ? "VICTOIRE ! Les héros ont triomphé !" : "DEFAITE... Tous les héros sont tombés.");
    }

    // ------------------------------------------------------------------
    // Rendu
    // ------------------------------------------------------------------

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int shakeX = battleSystem.getAnimationManager().getShakeX();
        int shakeY = battleSystem.getAnimationManager().getShakeY();
        g2.translate(shakeX, shakeY);

        if (background != null) g2.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        animationAngle += 0.05;

        drawHeroes(g2);
        drawMonsters(g2);

        if (battleSystem.isSelectingTarget()) {
            drawSelectionArrows(g2);
        }

        g2.translate(-shakeX, -shakeY);

     
        battleSystem.getAnimationManager().render(g2, getWidth(), getHeight(), this);
    }

    private void drawHeroes(Graphics2D g2) {
        Entity currentHero = battleSystem.getCurrentHero();

        for (int i = 0; i < heroes.size(); i++) {
            Entity h = heroes.get(i);
            if (!h.isAlive()) continue;

            int x = 50 + (i * 60);
            int y = 80 + (i * 120);
            int offset = (battleSystem.isPlayerPhase() && h == currentHero) ? (int) (Math.sin(animationAngle) * 10) : 0;

            Image img;
            int drawH = 140;
            int drawW;

            if (h instanceof Flamme) {
                img = flammeImg;
                drawW = (int) (drawH * (621.0 / 1331.0));
            } else if (h instanceof Tching) {
                img = tchingImg;
                drawW = (int) (drawH * (703.0 / 1614.0));
            } else if (h instanceof Bob) {
                img = ((Bob) h).isTransformed() ? darkBobImg : bobImg;
                drawW = (int) (drawH * (1000.0 / 1900.0));
            } else {
                img = null;
                drawW = 80;
            }

            if (img != null) g2.drawImage(img, x, y + offset, drawW, drawH, this);

            boolean hovered = (h == hoveredEntity);
            healthBar.draw(g2, x + drawW / 2, y + offset - 10, h, hovered);

            if (hovered && h instanceof UltimateCapable) {
                drawUltimateCountdown(g2, (UltimateCapable) h, x, y + offset - 55);
            }

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 11));
            g2.drawString(h.getName(), x - 5, y + offset + drawH + 14);
        }
    }

    private void drawMonsters(Graphics2D g2) {
        List<Rectangle> boxes = getMonsterHitboxes();
        for (int i = 0; i < monsters.size(); i++) {
            Entity m = monsters.get(i);
            if (!m.isAlive()) continue;

            Rectangle r = boxes.get(i);


            Image img = (m instanceof chefGobelin) ? chefGobelin : enemyImg;
            if (img != null) g2.drawImage(img, r.x, r.y, r.width, r.height, this);

            boolean hovered = (m == hoveredEntity);
            healthBar.draw(g2, r.x + r.width / 2, r.y - 10, m, hovered);
        }
    }

    private void drawUltimateCountdown(Graphics2D g2, UltimateCapable u, int x, int y) {
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        String text = u.isUltimateReady()
                ? "ULTIME : prêt !"
                : "ULTIME : " + u.getUltiTicksRemaining() + " tour(s)";
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRoundRect(x - 5, y - 15, 150, 20, 8, 8);
        g2.setColor(u.isUltimateReady() ? Color.YELLOW : Color.LIGHT_GRAY);
        g2.drawString(text, x, y);
    }

    private void drawSelectionArrows(Graphics2D g2) {
        List<Rectangle> boxes = getMonsterHitboxes();
        for (int i = 0; i < monsters.size(); i++) {
            Entity m = monsters.get(i);
            if (!m.isAlive()) continue;
            Rectangle r = boxes.get(i);
            drawFigmaArrow(g2, r.x + r.width / 2, r.y - 20);
        }
    }

    private void drawFigmaArrow(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(167, 156, 255, 178));
        g2.fillPolygon(new int[]{x - 12, x + 12, x}, new int[]{y - 22, y - 22, y}, 3);
        g2.setColor(Color.YELLOW);
        g2.fillPolygon(new int[]{x - 10, x + 10, x}, new int[]{y - 20, y - 20, y + 2}, 3);
    }


    private List<Rectangle> getMonsterHitboxes() {
        List<Rectangle> hitboxes = new ArrayList<>();
        for (int i = 0; i < monsters.size(); i++) {
            hitboxes.add(new Rectangle(getWidth() - 200 - (i * 50), 100 + (i * 120), 120, 120));
        }
        return hitboxes;
    }

    private Map<Entity, Rectangle> allEntitiesWithBounds() {
        Map<Entity, Rectangle> map = new LinkedHashMap<>();
        for (int i = 0; i < heroes.size(); i++) {
            Entity h = heroes.get(i);
            if (!h.isAlive()) continue;
            int x = 50 + (i * 60);
            int y = 80 + (i * 120);
            map.put(h, new Rectangle(x, y, 100, 140));
        }
        List<Rectangle> monsterBoxes = getMonsterHitboxes();
        for (int i = 0; i < monsters.size(); i++) {
            if (monsters.get(i).isAlive()) map.put(monsters.get(i), monsterBoxes.get(i));
        }
        return map;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        battleSystem.update();
        repaint();
    }
}