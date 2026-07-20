package battleSystem.ultimate;

import java.util.List;

import Entity.Bob;
import Entity.Entity;
import battleSystem.animation.BackgroundAnimation;
import battleSystem.animation.BobBackgroundAnimation;
import battleSystem.animation.BobUltimateAnimation;
import battleSystem.animation.UltimateAnimation;

public class UltimateBob extends Ultimate {

    @Override
    public void execute(Entity caster, Entity target, List<Entity> allTargets) {
        if (caster instanceof Bob) {
            ((Bob) caster).activerTransformation();
        }
        int degats = (caster.getDefPoints() * 2) + target.getDefPoints();
        System.out.println(caster.getName() + " déchaîne SUPER BOB sur " + target.getName() + " !");
        target.takeDamage(degats);
    }

    @Override
    public UltimateAnimation createUltimateAnimation() {
        return new BobUltimateAnimation();
    }

    @Override
    public BackgroundAnimation createBackgroundAnimation() {
        return new BobBackgroundAnimation();
    }

    @Override
    public String getName() {
        return "Super Bob";
    }
}
