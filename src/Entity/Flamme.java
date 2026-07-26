package Entity;

import java.util.List;
import java.util.Scanner;

import battleSystem.animation.BackgroundAnimation;
import battleSystem.animation.UltimateAnimation;
import battleSystem.ultimate.Ultimate;
import battleSystem.ultimate.UltimateFlamme;

public class Flamme extends Entity implements UltimateCapable {
    private Scanner scanner = new Scanner(System.in);
    protected int boostAtkTicks = 0;
    protected boolean isPreparing = false; 
    private static final int ULTI_MAX = 4;
    private int ultiTicks = ULTI_MAX;
    private boolean isUltimateReady = false;

    private final Ultimate ultimate = new UltimateFlamme();

    public Flamme(String name, int hp, int attackPoints, int defPoints) {
        super(name, hp, attackPoints, defPoints);
    }

    @Override
    public void performTurn(Entity target) {
     
        if (isPreparing) {
            finaliserPreparationMentale(target);
            return;
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

        if (boostAtkTicks > 0) {
            boostAtkTicks--;
            if (boostAtkTicks == 0) {
                this.setAttackPoints(this.getAttackPoints() - 50);
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
        target.setBurn(target.getBurnTicks() + 2);
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

        int degatsBase = (this.getAttackPoints() * 2) + (int) (target.getDefPoints() * 0.2);

        if (target.getBurnTicks() > 0) {
            degatsBase += (this.getHp() * 10) / 100;
            System.out.println("Bonus de dégâts appliqué car l'ennemi brûle !");
        }

        target.takeDamage(degatsBase);
        this.isPreparing = false;
    }

    public void checkUlti(Entity target) {
        if (this.isAlive() && target.getArrow()) {
            if (target.getHp() < 15) {
                System.out.println("OVERKILL !");
                target.takeDamage(target.getHp() + target.getDefPoints());
            } else {
                System.out.println("La flèche brûle" + this.getName());   
                target.setBurn(target.getBurnTicks() + 1);
            }
            target.setArrow(false);
        }
    }
    

    // ------------------------------------------------------------------
    // UltimateCapable — délégation à l'objet Ultimate (composition)
    // ------------------------------------------------------------------

    @Override
    public void useUltimate(Entity target, List<Entity> allTargets) {
        ultimate.execute(this, target, allTargets);
    }

    @Override
    public void décompteUlti() {
        if (ultiTicks > 0) ultiTicks--;
        if (ultiTicks == 0) isUltimateReady = true;
    }

    @Override
    public boolean isUltimateReady() {
        return isUltimateReady;
    }

    @Override
    public void resetUlti() {
        this.ultiTicks = ULTI_MAX;
        this.isUltimateReady = false;
    }

    @Override
    public int getUltiTicksRemaining() {
        return ultiTicks;
    }

    @Override
    public int getUltiTicksMax() {
        return ULTI_MAX;
    }

    @Override
    public String getUltimateName() {
        return ultimate.getName();
    }

    @Override
    public UltimateAnimation createUltimateAnimation() {
        return ultimate.createUltimateAnimation();
    }

    @Override
    public BackgroundAnimation createBackgroundAnimation() {
        return ultimate.createBackgroundAnimation();
    }
}
