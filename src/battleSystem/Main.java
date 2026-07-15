package battleSystem;

import Entity.*;
import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Création de l'équipe de héros
        List<Entity> heroes = new ArrayList<>();
        heroes.add(new Flamme("Flamme le Prince d'Hoturan", 400, 30, 10));
        heroes.add(new Tching("Tching", 350, 30, 5));
        heroes.add(new Bob("Bob le chef", 600, 20, 20));

        // 2. Création de l'équipe de monstres
        List<Entity> monsters = new ArrayList<>();
        monsters.add(new Fighter("Encens Tching", 150, 15, 5));
        monsters.add(new Fighter("Encens Flamme", 250, 18, 10));
        monsters.add(new Fighter("Encens Bob", 500, 25, 15));

        // 3. Configuration de la fenêtre (JFrame)
        JFrame frame = new JFrame("RPG Battle System - Team Fight");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800); 
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        JeuImage gamePanel = new JeuImage(heroes, monsters);
        
        
        frame.add(gamePanel);

        frame.setVisible(true);
        
        System.out.println("Le combat graphique est lance !");
    }
}