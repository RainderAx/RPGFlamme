package battleSystem.ultimate;

import java.util.List;

import Entity.Entity;
import battleSystem.animation.BackgroundAnimation;
import battleSystem.animation.FlammeBackgroundAnimation;
import battleSystem.animation.FlammeUltimateAnimation;
import battleSystem.animation.UltimateAnimation;


public class UltimateFlamme extends Ultimate {

    @Override
    public void execute(Entity caster, Entity target, List<Entity> allTargets) {
        int degats = caster.getAttackPoints() * 3;
        System.out.println(caster.getName() + " plante une FLECHE ARDENTE dans " + target.getName() + " !");
        target.setArrow(true);
        target.takeDamage(degats);
        target.setBurn(8);
    }
  
    public void checkUlti(Entity caster, Entity target) {
        if (caster.isAlive() && target.getArrow()) {
            if (target.getHp() < 15 * target.getMaxHp() / 100) {
                System.out.println("OVERKILL !");
                target.takeDamage(target.getHp() + target.getDefPoints());
            } else {
                System.out.println("La flèche brûle " + target.getName());
                target.setBurn(target.getBurnTicks() + 1);
            }
            target.setArrow(false);
        }
    }


    @Override
    public UltimateAnimation createUltimateAnimation() {
        return new FlammeUltimateAnimation();
    }

    @Override
    public BackgroundAnimation createBackgroundAnimation() {
        return new FlammeBackgroundAnimation();
    }

    @Override
    public String getName() {
        return "Flèche Ardente";
    }
}
