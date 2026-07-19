package Entity;

import java.util.List;
import java.util.Random;

public class chefGobelin extends Entity {
    private final Random rand = new Random();

    public chefGobelin(String name, int hp, int attackPoints, int defPoints) {
        super(name, hp, attackPoints, defPoints);
    }

    @Override
    public void performTurn(Entity target) {
        this.attack(target);
    }

    public void invocation(List<Entity> activeEntities) {
        int invoc = rand.nextInt(6) + 1;

        if (invoc == 6) {
            int vie = this.getHp() / 10;
            int attaque = this.getDefPoints();
            int defense = this.getDefPoints() / 10;

            Gobelin minion = new Gobelin("Sbire Gobelin", vie, attaque, defense);
            activeEntities.add(minion);
            System.out.println(getName() + " a invoqué un petit gobelin !");
        }
    }

    @Override
    public int attack(Entity target) {
        if (rand.nextInt(10) == 9) {
            this.setAttackPoints(this.getAttackPoints() + 10);
            System.out.println(getName() + " entre en rage ! ATK +10");
        }

        if (rand.nextInt(10) == 9) {
            this.setDefPoints(this.getDefPoints() + 10);
            System.out.println(getName() + " entre en rage ! Def +10");
        }

        if (rand.nextInt(100) == 99) {
            this.setAttackPoints(this.getAttackPoints() + 20);
            this.setDefPoints(this.getDefPoints() + 20);
            System.out.println("PUISSANCE MAXIMALE pour le Chef !");
        }

        return super.attack(target);
    }
}
