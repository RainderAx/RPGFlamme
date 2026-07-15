package Entity;

import java.util.Scanner;

public class Flamme extends Entity {
    private Scanner scanner = new Scanner(System.in);
    protected int boostAtkTicks = 0;
    protected boolean isPreparing = false; // Pour la Préparation Mentale
    protected int ultiTicks = 5; //décompte de tour avant de pouvoir lancer l'attaque ultime
    protected boolean isUltimateReady = false;
    

    public Flamme(String name, int hp, int attackPoints, int defPoints) {
        super(name, hp, attackPoints, defPoints);
    }

    @Override
    public void performTurn(Entity target) {
        // Si on préparait une attaque au tour précédent, on déclenche l'effet
        if (isPreparing) {
            finaliserPreparationMentale(target);
            return; // Le tour est consommé par l'attaque spéciale
        }

        System.out.println("\n--- Tour de " + getName() + " (PV: " + this.getHp() + ") ---");
        System.out.println("1. Briquet (Dégâts + Brûlure)");
        System.out.println("2. Encens du tigre (Boost ATK)");
        System.out.println("3. Préparation mentale (Attaque chargée)");

        int choix = scanner.nextInt();

        switch (choix) {
            case 1:
                this.briquet(target);
                break;
            case 2:
                this.encensDuTigre();
                break;
            case 3:
                this.preparationMentale();
                break;
            default:
                System.out.println("Choix invalide, vous perdez votre tour !");
        }
        
        // Gestion du décompte du boost à la fin du tour
        if (boostAtkTicks > 0) {
            boostAtkTicks--;
            if (boostAtkTicks == 0) {
                this.setAttackPoints(this.getAttackPoints() - 50); // On retire le bonus
                System.out.println("Le boost de l'Encens du Tigre prend fin.");
            }
        }
    }
    
    public boolean isPreparing() {
        return isPreparing;
    }
    
    public void briquet(Entity target) {
        System.out.println(getName() + " utilise son briquet sur " + target.getName() + " !");
        this.attack(target); 
        target.setBurn(5);
    }

    public void encensDuTigre() {
        if (boostAtkTicks > 0) {
            System.out.println("Le boost est déjà actif ! (Encore " + boostAtkTicks + " tours)");
        } else {
            System.out.println(getName() + " utilise son encens ! ATK +50 pendant 3 tours.");
            this.setAttackPoints(this.getAttackPoints() + 50);
            this.boostAtkTicks = 3;
        }
    }

    public void preparationMentale() {
        System.out.println(getName() + " se concentre... L'attaque frappera au prochain tour !");
        this.isPreparing = true;
    }

    public void finaliserPreparationMentale(Entity target) {
        System.out.println(getName() + " relâche sa force mentale !");
        
        // Calcul des dégâts selon ta formule :
        // ((ATK*2) + (TargetDef*0.2) + BonusBrulure(10% HP))
        int degatsBase = (this.getAttackPoints() * 2) + (int)(target.getDefPoints() * 0.2);
        
        if (target.getBurnTicks() > 0) {
            degatsBase += (this.getHp() * 10) / 100;
            System.out.println("Bonus de dégâts appliqué car l'ennemi brûle !");
        }

        target.takeDamage(degatsBase);
        this.isPreparing = false; // Reset l'état
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
        	int planteFleche = this.getAttackPoints()*2;
        	target.setArrow(true);
        	target.takeDamage(planteFleche);
        	ultiTicks = 5;
        	isUltimateReady = false;
    	}

    }
    
    public void checkUlti(Entity target) {
        // Vérifie si le lanceur est en vie et si la cible a une flèche
        if (this.isAlive() && target.getArrow()) {
            
            if (target.getHp() < 15) {
                System.out.println("OVERKILL !");
                target.takeDamage(target.getHp() + target.getDefPoints()); 
            } else {
               
                System.out.println("La flèche brûle" + getName());
                target.setBurn(target.getBurnTicks() + 1);
            }
            
            target.setArrow(false); 
        }
    }
}