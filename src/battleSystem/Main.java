package battleSystem;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import Entity.*;

public class Main {
    public static void main(String[] args) {

        List<Entity> heroes = new ArrayList<>();
        heroes.add(new Flamme("Flamme le Prince d'Hoturan", 400, 10, 10));
        heroes.add(new Tching("Tching", 350, 10, 5));
        heroes.add(new Bob("Bob le chef", 600, 10, 20));

        List<Entity> monsters = new ArrayList<>();
        monsters.add(new Gobelin("Gobelin", 150, 20, 10));
        monsters.add(new chefGobelin("Chef Gobelin", 300, 40, 20));

        JFrame frame = new JFrame("RPG Battle System - Team Fight");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);


        JeuImage gamePanel = new JeuImage(heroes, monsters);
        frame.add(gamePanel);

        frame.setVisible(true);

        System.out.println("Le combat graphique est lancé !");
    }
}