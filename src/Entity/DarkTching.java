package Entity;

public class DarkTching extends Tching {

    private int demoralisationsAppliquees = 0;
    private static final int MAX_DEMORALISATION = 5;

    public DarkTching(String name, int hp, int attackPoints, int defPoints) {
        super(name, hp, attackPoints, defPoints);
    }

    @Override
    public void performTurn(Entity target) {
        chooseAttack(target);
    }

    public void chooseAttack(Entity target) {
        float hpRatio = (float) getHp() / getMaxHp();

        if (hpRatio < 0.3f && dice.nextInt(2) == 0) {
            coupDesespere(target);
            return;
        }

        int rnd = dice.nextInt(10) + 1;

        if (rnd <= 3) coupRapide(target);                    // 30%
        else if (rnd <= 6) techniqueDuTigre(target);          // 30%
        else if (rnd <= 8) superCoup(target);                 // 20%
        else if (rnd == 9) demoralisationIntensive(target);   // 10%
        else antiTigre(target);                               // 10%
    }

    public void coupDesespere(Entity target) {
        System.out.println(getName() + " frappe dans un dernier élan désespéré !");
        target.takeDamage(40);
    }

    public void superCoup(Entity target) {
        System.out.println(getName() + " lance un Super Coup !");
        int damage = getAttackPoints() * 2;
        target.takeDamage(damage);
    }

    public void demoralisationIntensive(Entity target) {
        if (demoralisationsAppliquees >= MAX_DEMORALISATION) {
            System.out.println(target.getName() + " est déjà au maximum de démoralisation !");
            return;
        }
        System.out.println(getName() + " utilise Démoralisation Intensive sur " + target.getName());
        target.setAttackPoints(Math.max(0, target.getAttackPoints() - 20));
        target.setDefPoints(Math.max(0, target.getDefPoints() - 10));
        demoralisationsAppliquees++;
    }

    public void antiTigre(Entity target) {
        System.out.println(getName() + " déchaîne la Technique du Tigre des Ombres sur " + target.getName() + " !");
        int degats = (target.getAttackPoints() + (target.getDefPoints() / 4));
        target.takeDamage(degats);

        int recul = (int) (getMaxHp() * 0.05);
        setHp(getHp() - recul);
        System.out.println(getName() + " est rongé par sa propre noirceur (-" + recul + " PV) !");
    }
}