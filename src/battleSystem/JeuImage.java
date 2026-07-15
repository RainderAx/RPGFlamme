package battleSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import Entity.*;
import java.io.OutputStream;
import java.io.PrintStream;

public class JeuImage extends JPanel implements ActionListener {

    private Image background, flammeImg, tchingImg, bobImg, enemyImg;
    private List<Entity> heroes;
    private List<Entity> monsters;
    private Entity target;
    
    private int heroIndexTurn = 0;
    private boolean isPlayerPhase = true;

    private JLabel lblStatus;
    private JTextArea txtConsole; 
    private JPanel pnlActions;    
    private double animationAngle = 0;

    public JeuImage(List<Entity> h, List<Entity> m) {
        this.heroes = h;
        this.monsters = m;
        this.target = monsters.get(0);
        
        setLayout(null);

        // Chargement des images
        try {
            background = new ImageIcon(JeuImage.class.getResource("/assets/Background_Forest.jpg")).getImage();
            flammeImg  = new ImageIcon(JeuImage.class.getResource("/assets/flamme.png")).getImage();
            tchingImg  = new ImageIcon(JeuImage.class.getResource("/assets/tching.png")).getImage();
            bobImg     = new ImageIcon(JeuImage.class.getResource("/assets/Bob.png")).getImage();
            enemyImg   = new ImageIcon(JeuImage.class.getResource("/assets/Sprite_Monster.png")).getImage();
        } catch (Exception e) {
            System.err.println("Erreur de chargement des images : " + e.getMessage());
        }

        // --- ZONE DE TEXTE (TERMINAL) ---
        txtConsole = new JTextArea();
        txtConsole.setEditable(false);
        txtConsole.setBackground(new Color(0, 0, 0, 180));
        txtConsole.setForeground(Color.GREEN);
        txtConsole.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(txtConsole);
        scrollPane.setBounds(20, 420, 300, 130);
        add(scrollPane);

        // Redirection de System.out vers txtConsole
        redirectSystemOut();

        // --- PANNEAU DES ACTIONS ---
        pnlActions = new JPanel();
        pnlActions.setLayout(new FlowLayout());
        pnlActions.setBounds(330, 480, 450, 80);
        pnlActions.setOpaque(false);
        add(pnlActions);

        // --- BOUTONS DE CIBLE ---
        for (int i = 0; i < monsters.size(); i++) {
            Entity currentM = monsters.get(i);
            JButton btnCible = new JButton(currentM.getName());
            btnCible.setBounds(650, 50 + (i * 45), 120, 30);
            btnCible.addActionListener(e -> {
                if (currentM.isAlive()) {
                    target = currentM;
                    updateStatus();
                }
            });
            add(btnCible);
        }

        lblStatus = new JLabel("", SwingConstants.CENTER);
        lblStatus.setBounds(0, 10, 800, 30);
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblStatus);
        
        updateActions(); 
        updateStatus();

        new Timer(16, this).start();
    }

    private void redirectSystemOut() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                txtConsole.append(String.valueOf((char) b));
                txtConsole.setCaretPosition(txtConsole.getDocument().getLength());
            }
        };
        System.setOut(new PrintStream(out, true));
    }

    private void updateStatus() {
        if (heroIndexTurn < heroes.size()) {
            Entity currentHero = heroes.get(heroIndexTurn);
            lblStatus.setText("Tour de : " + currentHero.getName() + " | Cible : " + target.getName());
        }
    }

    // Affiche les boutons spécifiques au héros actuel
    private void updateActions() {
        pnlActions.removeAll();
        if (!isPlayerPhase || heroIndexTurn >= heroes.size()) return;

        Entity currentHero = heroes.get(heroIndexTurn);
        
        if (currentHero instanceof Flamme) {
            Flamme f = (Flamme) currentHero;
            if (f.isPreparing()) {
                // Si elle est prête, on lance l'attaque SANS attendre de clic
                f.finaliserPreparationMentale(target);
                
                // On attend un petit peu pour que l'utilisateur puisse lire le texte 
                // puis on passe au tour suivant automatiquement
                Timer autoNext = new Timer(1500, e -> finirTourHeros());
                autoNext.setRepeats(false);
                autoNext.start();
                
                // On affiche un message d'attente sur l'interface
                lblStatus.setText(f.getName() + " déclenche son attaque spéciale !");
                return; // On sort de la méthode pour ne pas afficher de boutons
            }
        }
        
        // Ajout des boutons selon le type de héros
        if (currentHero instanceof Flamme) {
        	Flamme f = (Flamme) currentHero;
            ajouterBouton("Briquet", () -> f.briquet(target));
            ajouterBouton("Encens du Tigre", () -> f.encensDuTigre());
            ajouterBouton("Preparation mentale", () -> f.preparationMentale());
        } else if (currentHero instanceof Tching) {
        	Tching t = (Tching) currentHero;
            ajouterBouton("Coup Rapide", () -> t.coupRapide(target));
            ajouterBouton("Technique Tigre", () -> t.techniqueDuTigre(target));
            ajouterBouton("Entrainement Intensif", () -> t.entrainementIntensif());
        } else if (currentHero instanceof Bob) {
        	Bob b = (Bob) currentHero;
            ajouterBouton("Coup du Marteau", () -> b. coupDuMarteau(target));
            ajouterBouton("Provocation", () -> b.activerProvocation());
        }

        pnlActions.revalidate();
        pnlActions.repaint();
    }

    private void ajouterBouton(String nom, Runnable action) {
        JButton btn = new JButton(nom);
        btn.addActionListener(e -> {
            if (isPlayerPhase && target.isAlive()) {
                action.run();
                finirTourHeros();
            }
        });
        pnlActions.add(btn);
    }

    private void finirTourHeros() {
        heroes.get(heroIndexTurn).applyPostTurnEffects();
        heroIndexTurn++;

        if (heroIndexTurn >= heroes.size() || !isTeamAlive(monsters)) {
            isPlayerPhase = false;
            pnlActions.setVisible(false);
            Timer pause = new Timer(1500, e -> tourDesMonstres());
            pause.setRepeats(false);
            pause.start();
        } else {
            if (!heroes.get(heroIndexTurn).isAlive()) finirTourHeros();
            else {
                updateActions();
                updateStatus();
            }
        }
    }

    private void tourDesMonstres() {
        for (Entity m : monsters) {
            if (m.isAlive() && isTeamAlive(heroes)) {
                Entity victim = findTargetForMonsters();
                m.performTurn(victim);
                m.applyPostTurnEffects();
            }
        }
        heroIndexTurn = 0;
        isPlayerPhase = true;
        pnlActions.setVisible(true);
        updateActions();
        updateStatus();
    }

    private Entity findTargetForMonsters() {
        for (Entity h : heroes) {
            if (h instanceof Bob && h.isAlive() && ((Bob) h).isTaunting()) return h;
        }
        List<Entity> alive = heroes.stream().filter(Entity::isAlive).toList();
        return alive.get((int)(Math.random() * alive.size()));
    }

    private boolean isTeamAlive(List<Entity> team) {
        return team.stream().anyMatch(Entity::isAlive);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        animationAngle += 0.05;

        // --- DESSIN DES HÉROS ---
        for (int i = 0; i < heroes.size(); i++) {
            Entity h = heroes.get(i);
            if (h.isAlive()) {
                int x = 50 + (i * 60);  
                int y = 80 + (i * 120); 
                int offset = (isPlayerPhase && i == heroIndexTurn) ? (int)(Math.sin(animationAngle) * 10) : 0;
                
                Image img = null;
                int drawW = 100;
                int drawH = 140; // Nouvelle hauteur de base pour les héros (plus grands et élancés)

                // Calcul de la largeur en fonction du ratio de ton image originale
                if (h instanceof Flamme) {
                    img = flammeImg;
                    drawW = (int)(drawH * (621.0 / 1331.0)); // Garde les proportions de Flamme
                } else if (h instanceof Tching) {
                    img = tchingImg;
                    drawW = (int)(drawH * (703.0 / 1614.0)); // Garde les proportions de Tching
                } else if (h instanceof Bob) {
                    img = bobImg;
                    drawW = (int)(drawH * (1000.0 / 1900.0)); // Garde les proportions de Bob
                }

                if (img != null) {
                    g.drawImage(img, x, y + offset, drawW, drawH, this);
                }
                
                g.setColor(Color.WHITE);
                // J'ai centré un peu le texte par rapport à la nouvelle largeur
                g.drawString(h.getName() + " (" + h.getHp() + " PV)", x - 10, y + offset - 5);
            }
        }

        // --- DESSIN DES MONSTRES ---
        for (int i = 0; i < monsters.size(); i++) {
            Entity m = monsters.get(i);
            if (m.isAlive()) {
                int x = getWidth() - 200 - (i * 50);
                int y = 100 + (i * 120);
                
                // Le monstre est quasiment un carré (1749 x 1710)
                int monsterH = 120;
                int monsterW = (int)(monsterH * (1749.0 / 1710.0));

                g.drawImage(enemyImg, x, y, monsterW, monsterH, this);
                
                g.setColor(Color.WHITE);
                g.drawString(m.getName() + " : " + m.getHp() + " PV", x, y - 5);
                
                if (m == target) {
                    g.setColor(Color.RED);
                    // Le rectangle rouge s'adapte maintenant à la vraie taille du monstre
                    g.drawRect(x, y, monsterW, monsterH);
                }
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}