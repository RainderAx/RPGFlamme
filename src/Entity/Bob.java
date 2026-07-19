package Entity;

import java.util.List;
import java.util.Scanner;

import battleSystem.animation.BackgroundAnimation;
import battleSystem.animation.UltimateAnimation;
import battleSystem.ultimate.Ultimate;
import battleSystem.ultimate.UltimateBob;

public class Bob extends Entity implements UltimateCapable {
    private boolean isTaunting = false;
    private boolean isTransformed = false;
    private Scanner scanner = new Scanner(System.in);

    private static final int ULTI_MAX = 5;
    private int ultiTicks = ULTI_MAX;
    private boolean isUltimateReady = false;

    private final Ultimate ultimate = new UltimateBob();

    public Bob(String name, int hp, int attackPoints, int defPoints) {
        super(name, hp, attackPoints, defPoints);
    }

    @Override
    public void performTurn(Entity target) {
        this.isTaunting = false;

        System.out.println("\n--- Tour de " + getName() + " (PV: " + this.getHp() + ") ---");
        System.out.println("1. Coup du Marteau");
        System.out.println("2. Je suis pas ton pote ! (Provocation)");
        System.out.print("Votre choix : ");

        if (!scanner.hasNextInt()) {
            System.out.println("Entrée invalide !");
            scanner.next();
            return;
        }

        int choix = scanner.nextInt();
        if (choix == 2) {
            activerProvocation();
        } else {
            this.coupDuMarteau(target);
        }
    }

    public boolean isTaunting() {
        return isTaunting;
    }

    public boolean isTransformed() {
        return isTransformed;
    }

    /** Appelé par UltimateBob lors de l'exécution de l'ultime (composition, pas d'if/else dans le moteur). */
    public void activerTransformation() {
        this.isTransformed = true;
    }

    public void coupDuMarteau(Entity target) {
        int damage = 50 + target.getDefPoints();
        System.out.println(getName() + " frappe lourdement " + target.getName() + " !");
        target.takeDamage(damage);
        this.isTaunting = false;
    }
    
    public void perceuse(Entity target) {
    	this.setDefPoints(getDefPoints() + 10);
    	int damage = 80 +target.getDefPoints() + this.getDefPoints();
    	
    	target.takeDamage(damage);
    }
    public void activerProvocation() {
        System.out.println(getName() + " hurle : 'Tapez-moi si vous l'osez !'");
        this.isTaunting = true;
    }

    public void superProvoc() {
        int boost = this.getDefPoints() / 4;
        System.out.println(getName() + " hurle : 'Tapez-moi si vous l'osez !'");
        this.setDefPoints(getDefPoints() + boost);
        this.isTaunting = true;
    }

    @Override
    public void takeDamage(int amount) {
        if (this.isTaunting) {
            int finalDamage = (amount / 2) - this.getDefPoints();
            if (finalDamage < 0) finalDamage = 0;

            this.setHp(this.getHp() - finalDamage);
            System.out.println(this.getName() + " encaisse ! Perd " + finalDamage + " PV. (Reste : " + this.getHp() + ")");
        } else {
            super.takeDamage(amount);
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
