package battleSystem.ultimate;

import java.util.List;

import Entity.Entity;
import battleSystem.animation.BackgroundAnimation;
import battleSystem.animation.FlammeBackgroundAnimation;
import battleSystem.animation.FlammeUltimateAnimation;
import battleSystem.animation.UltimateAnimation;

/** Capacité ultime de Flamme : immense flèche ardente qui plante et brûle la cible. */
public class UltimateFlamme extends Ultimate {

    @Override
    public void execute(Entity caster, Entity target, List<Entity> allTargets) {
        int degats = caster.getAttackPoints() * 3;
        System.out.println(caster.getName() + " plante une FLECHE ARDENTE dans " + target.getName() + " !");
        target.setArrow(true);
        target.takeDamage(degats);
        target.setBurn(8);
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
