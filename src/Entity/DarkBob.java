package Entity;

import java.util.Random;


public class DarkBob extends Bob {

    private static final Random dice = new Random();

    public DarkBob(String name, int hp, int attackPoints, int defPoints) {
        super(name, hp, attackPoints, defPoints);
    }

    @Override
    public void performTurn(Entity target) {
    
        this.setHp(this.getHp() + 10);
        chooseAttack(target);
    }

    public void chooseAttack(Entity target) {
        float hpRatio = (float) getHp() / getMaxHp();

        // Attaque de dernier recours sous les 30% de PV (50% de chance d'activation)
        if (hpRatio < 0.3f && dice.nextInt(2) == 0) {
            boostDesespere();
            return;
        }

        int rnd = dice.nextInt(10) + 1;

        if (rnd <= 3) {
            coupDuMarteau(target);                        // 30% : 
        } else if (rnd <= 5) {
            coupDeCarreau(target);                         // 20% : 
        } else if (rnd <= 7) {
            coupDeClou(target);                    // 20% : 
        } else if (rnd <= 9) {
            perceuse(target);                   // 20% : 
        } else {
            coupDeSalopio(target);           // 10% 
        }
    }

    public void boostDesespere() {
        System.out.println(getName() + " se concentre et entre dans un état de puissance désespérée !");
        this.setAttackPoints(this.getAttackPoints() + 20);
        this.setDefPoints(this.getDefPoints() + 20);
    }

    public void CoupDeCarreau(Entity target) {
        System.out.println(getName() + " utilise Coup de Carreau sur " + target.getName() + " !");
        int damage = (target.getHp() / 4) + (this.getAttackPoints()/2)+ (target.getDefPoints()/2);
        target.takeDamage(damage);

    }

    public void coupDeClou(Entity target) {
        System.out.println(getName() + " utilise Coup de Clou sur " + target.getName() + " !");
        int damage = (target.getMaxHp() / 10);
        target.takeDamage(damage);
    }

    public void coupDeSalopio(Entity target) {
        System.out.println(getName() + " utilise Coup de Salopio sur " + target.getName() + " !");
        int damage = ((target.getHp() / 4) + (this.getAttackPoints()/2)+ (target.getDefPoints()/2)) + (target.getMaxHp() / 10) + ((80 +target.getDefPoints() + this.getDefPoints()) + 50) ;
        int salopioDamage = damage / 5; 
        int contreCoupDamage = damage / 10;
        target.takeDamage(salopioDamage);
        this.takeDamage(contreCoupDamage);
        System.out.println(getName() + " subit un contre-coup de " + contreCoupDamage + " PV en raison de l'attaque puissante !");
    }
}