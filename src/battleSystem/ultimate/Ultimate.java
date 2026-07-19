package battleSystem.ultimate;

import java.util.List;

import Entity.Entity;
import battleSystem.animation.BackgroundAnimation;
import battleSystem.animation.UltimateAnimation;

/**
 * Classe mère de toutes les capacités ultimes.
 * Chaque sous-classe encapsule sa propre logique ET ses propres animations :
 * le BattleSystem n'a jamais besoin de savoir laquelle il manipule (OCP).
 */
public abstract class Ultimate {
    public abstract void execute(Entity caster, Entity target, List<Entity> allTargets);

    public abstract UltimateAnimation createUltimateAnimation();

    public abstract BackgroundAnimation createBackgroundAnimation();

    public abstract String getName();
}
