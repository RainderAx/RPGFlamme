package Entity;

import java.util.Random;
import java.util.Scanner;
import java.util.List;

public class Tching extends Entity {
    private Scanner scanner = new Scanner(System.in);
    protected static final Random dice = new Random();
    protected int ultiTicks =5;
    protected boolean isUltimateReady = false;
        
    public Tching(String name, int hp, int attackPoints, int defPoints) {
        super(name, hp, attackPoints, defPoints);
    }
    
    @Override
    public void performTurn(Entity target) {
        System.out.println("\n--- Tour de " + getName() + " (PV: " + this.getHp() + ") ---");
        System.out.println("1. Coup Rapide");
        System.out.println("2. Technique du Tigre (Attaques Répétées)");
        System.out.println("3. Entraînement Intensif (Boost permanent)");
        
        // Sécurité pour le scanner si l'utilisateur ne tape pas un nombre
        if (!scanner.hasNextInt()) {
            System.out.println("Entrée invalide !");
            scanner.next(); 
            return;
        }

        int choix = scanner.nextInt();
        
        switch (choix) {
            case 1:
                this.coupRapide(target);
                break;
            case 2:
                this.techniqueDuTigre(target);
                break;
            case 3:
                this.entrainementIntensif();
                break;
            default:
                System.out.println("Choix invalide, vous perdez votre tour !");
        }
    }
    
    public void coupRapide(Entity target) {
        System.out.println(getName() + " envoie un coup rapide sur " + target.getName() + " !");
        this.attack(target);
    }
    
    public void techniqueDuTigre(Entity target) {
        System.out.println(getName() + " déchaîne la Technique du Tigre sur " + target.getName() + " !");
        
        
        int degatsPrecedents = this.attack(target);
        
        // Calcul de la puissance des griffures bonus
        int griffe = (degatsPrecedents / 3) + target.getDefPoints();
        
        System.out.println("Série de griffures rapides...");
        for (int i = 0; i < 3; i++) {
            // On vérifie si la cible est toujours en vie avant de frapper
            if (!target.isAlive()) {
                System.out.println(target.getName() + " est déjà au sol !");
                break; 
            }

            int jet = dice.nextInt(3); // 0, 1 ou 2
            
            if (jet == 0) {
                System.out.println("La griffure n°" + (i+1) + " rate !");
            } else {
                int coup = griffe * jet;
                if (jet == 2) System.out.print("[CRITIQUE] ");
                target.takeDamage(coup);
            }
        }
    }
    
    public void entrainementIntensif() {
        System.out.println(getName() + " s'entraîne intensément ! ATK+10, DEF+10.");
        this.setAttackPoints(this.getAttackPoints() + 10);
        this.setDefPoints(this.getDefPoints() + 10);
    }
    
    public void décompteUlti() {
    	if (ultiTicks > 0) {
    		ultiTicks--;
    	}else {
    		System.out.println("Ultimate Disponible");
    		isUltimateReady = true;
    	}
    }
    
    public void ultim (Entity target) {
    	if (isUltimateReady == true) {
    		ultiTicks =  5;
    		isUltimateReady = false;
    	}
    }
    
    public void zoneAttack(List<Entity> targets) {
        System.out.println(this.getName() + " lance une attaque de ZONE dévastatrice !");
        
        // On calcule les dégâts une seule fois pour tout le monde
        int damage = this.getAttackPoints() * 2; 

        // La boucle parcourt tous les ennemis présents dans la liste
        for (Entity target : targets) {
            // On vérifie toujours si l'ennemi est en vie avant de frapper
            if (target.isAlive()) {
                System.out.println("L'onde de choc frappe " + target.getName());
                target.takeDamage(damage);
            }
        }
    }
}