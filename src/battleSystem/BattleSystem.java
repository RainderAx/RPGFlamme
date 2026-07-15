package battleSystem;

import Entity.Entity;
import Entity.Bob;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class BattleSystem {
    private Scanner scanner = new Scanner(System.in);
    private Random rand = new Random();

    public void startBattle(List<Entity> heroes, List<Entity> monsters) {
        while (isTeamAlive(heroes) && isTeamAlive(monsters)) {
            
            // --- TOUR DES HÉROS ---
            for (Entity hero : heroes) {
                if (hero.isAlive() && isTeamAlive(monsters)) {
                    System.out.println("\nC'est au tour de " + hero.getName());
                    Entity target = chooseTarget(monsters, "Choisissez un ennemi à attaquer :");
                    hero.performTurn(target);
                    hero.applyPostTurnEffects();
                }
            }

            // --- TOUR DES MONSTRES ---
            for (Entity monster : monsters) {
                if (monster.isAlive() && isTeamAlive(heroes)) {
                    // Les monstres cherchent si Bob provoque
                    Entity target = findTargetForMonster(heroes);
                    
                    System.out.println("\n" + monster.getName() + " s'apprête à attaquer " + target.getName());
                    monster.performTurn(target);
                    monster.applyPostTurnEffects();
                }
            }
        }
        
        displayResult(heroes);
    }

    // Logique pour trouver qui attaquer (Provocation de Bob ou Hasard)
    private Entity findTargetForMonster(List<Entity> heroes) {
        // 1. On cherche si un Bob vivant utilise sa provocation
        for (Entity h : heroes) {
            if (h instanceof Bob && h.isAlive() && ((Bob) h).isTaunting()) {
                return h;
            }
        }
        // 2. Sinon, on prend un héros vivant au hasard
        List<Entity> aliveHeroes = heroes.stream().filter(Entity::isAlive).toList();
        return aliveHeroes.get(rand.nextInt(aliveHeroes.size()));
    }

    private boolean isTeamAlive(List<Entity> team) {
        for (Entity e : team) {
            if (e.isAlive()) return true;
        }
        return false;
    }

    private Entity chooseTarget(List<Entity> targets, String message) {
        System.out.println(message);
        for (int i = 0; i < targets.size(); i++) {
            Entity t = targets.get(i);
            if (t.isAlive()) {
                System.out.println(i + ". " + t.getName() + " (" + t.getHp() + " PV)");
            }
        }
        int choix = scanner.nextInt();
        return targets.get(choix);
    }

    private void displayResult(List<Entity> heroes) {
        if (isTeamAlive(heroes)) {
            System.out.println("\nVICTOIRE ! Les héros ont triomphé !");
        } else {
            System.out.println("\nDEFAITE... Tous les héros sont tombés.");
        }
    }
}