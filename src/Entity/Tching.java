package Entity;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import battleSystem.animation.BackgroundAnimation;
import battleSystem.animation.UltimateAnimation;
import battleSystem.ultimate.Ultimate;
import battleSystem.ultimate.UltimateTching;

public class Tching extends Entity implements UltimateCapable {
    private Scanner scanner = new Scanner(System.in);
    protected static final Random dice = new Random();

    private static final int ULTI_MAX = 5;
    private int ultiTicks = ULTI_MAX;
    private boolean isUltimateReady = false;

    private final Ultimate ultimate = new UltimateTching();

    public Tching(String name, int hp, int attackPoints, int defPoints) {
        super(name, hp, attackPoints, defPoints);
    }

    @Override
    public void performTurn(Entity target) {
        System.out.println("\n--- Tour de " + getName() + " (PV: " + this.getHp() + ") ---");
        System.out.println("1. Coup Rapide");
        System.out.println("2. Technique du Tigre (Attaques Répétées)");
        System.out.println("3. Entraînement Intensif (Boost permanent)");

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
        int griffe = (degatsPrecedents / 3) + target.getDefPoints();

        System.out.println("Série de griffures rapides...");
        for (int i = 0; i < 3; i++) {
            if (!target.isAlive()) {
                System.out.println(target.getName() + " est déjà au sol !");
                break;
            }

            int jet = dice.nextInt(3);

            if (jet == 0) {
                System.out.println("La griffure n°" + (i + 1) + " rate !");
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

    public void zoneAttack(List<Entity> targets) {
        System.out.println(this.getName() + " lance une attaque de ZONE dévastatrice !");
        int damage = this.getAttackPoints() * 2;
        for (Entity target : targets) {
            if (target.isAlive()) {
                System.out.println("L'onde de choc frappe " + target.getName());
                target.takeDamage(damage);
            }
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
