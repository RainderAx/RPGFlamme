package Entity;

public class Fighter extends Entity {
    public Fighter(String name, int hp, int attackPoints, int defPoints) {
        super(name, hp, attackPoints, defPoints);
    }

    @Override
    public void performTurn(Entity target) {
        this.attack(target);
    }
}
