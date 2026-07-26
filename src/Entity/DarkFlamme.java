package Entity;

import java.util.Random;

/**
 * Clone maléfique de Flamme. Reprend certaines techniques de l'original
 * de façon corrompue (malédiction, brasier désespéré, vol de vie).
 */
public class DarkFlamme extends Flamme {

    private static final Random dice = new Random();

    public DarkFlamme(String name, int hp, int attackPoints, int defPoints) {
        super(name, hp, attackPoints, defPoints);
    }

    @Override
    public void performTurn(Entity target) {
        // Si DarkFlamme était en train de préparer une attaque au tour précédent,
        // la méthode héritée de Flamme gère la finalisation de l'attaque chargée.
        if (isPreparing) {
            finaliserPreparationMentale(target);
            return;
        }

        chooseAttack(target);
    }

    public void chooseAttack(Entity target) {
        float hpRatio = (float) getHp() / getMaxHp();

        // Attaque de dernier recours sous les 30% de PV (50% de chance d'activation)
        if (hpRatio < 0.3f && dice.nextInt(2) == 0) {
            brasierDesespere(target);
            return;
        }

        int rnd = dice.nextInt(10) + 1;

        if (rnd <= 3) {
            briquet(target);                        // 30% : Attaque standard + Brûlure
        } else if (rnd <= 5) {
            encensDuTigre();                         // 20% : Boost ATK +50
        } else if (rnd <= 7) {
            preparationMentale();                    // 20% : Préparation d'une grosse attaque
        } else if (rnd <= 9) {
            flammeMaudite(target);                   // 20% : Malédiction réduisant ATK + Brûlure
        } else {
            flammeVampiriqueRegen(target);           // 10% : Dégâts + vol de vie
        }
    }

    public void brasierDesespere(Entity target) {
        System.out.println(getName() + " entre en éruption et déchaîne un Brasier Désespéré !");

        int degats = getAttackPoints() * 2;
        if (target.getBurnTicks() > 0) {
            degats += 45;
            System.out.println("Les flammes existantes amplifient l'explosion !");
        }
        target.takeDamage(degats);
        target.setBurn(10);
    }

    public void flammeMaudite(Entity target) {
        System.out.println(getName() + " projette une Flamme Maudite sur " + target.getName() + " !");
        target.setAttackPoints(Math.max(0, target.getAttackPoints() - 10));
        target.setBurn(target.getBurnTicks() + 4);
    }

    public void flammeVampiriqueRegen(Entity target) {
        System.out.println("Les flammes de " + getName() + " le régénèrent !");
        this.setBurn(this.getBurnTicks() + 2);

        int damage = 20 + (target.getBurnTicks() * this.getBurnTicks());
        target.takeDamage(damage);

        int soin = damage / 2;
        this.setHp(this.getHp() + soin);
        System.out.println(getName() + " récupère " + soin + " PV grâce au vol de vie !");
    }
}