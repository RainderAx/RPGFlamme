package Entity;

import javax.swing.ImageIcon;
import java.awt.Image;

public class Gobelin extends Entity {
    private Image gobelinImg;

    public Gobelin(String name, int hp, int attackPoints, int defPoints) {
        super(name, hp, attackPoints, defPoints);
        try {
            gobelinImg = new ImageIcon(getClass().getResource("/assets/gobelin.png")).getImage();
        } catch (Exception e) {
            System.err.println("Erreur de chargement de l'image gobelin : " + e.getMessage());
        }
    }

    @Override
    public void performTurn(Entity target) {
        this.attack(target);
    }

    @Override
    public void takeDamage(int amount) {
        int finalDamage;

        finalDamage = amount - (this.getDefPoints() / 2);
        if (finalDamage < 0) finalDamage = 0;
        this.setHp(this.getHp() - finalDamage);

        System.out.println(this.getName() + " perd " + finalDamage + " PV.");
    }

    public Image getGobelinImg() { return gobelinImg; }
}
