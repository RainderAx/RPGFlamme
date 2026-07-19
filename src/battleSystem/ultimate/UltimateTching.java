package battleSystem.ultimate;

import java.util.List;

import Entity.Entity;
import battleSystem.animation.BackgroundAnimation;
import battleSystem.animation.TchingBackgroundAnimation;
import battleSystem.animation.TchingUltimateAnimation;
import battleSystem.animation.UltimateAnimation;

/**
 * Capacité ultime de Tching : onde de choc de zone touchant tous les ennemis vivants.
 * La cible cliquée sert uniquement à valider le déclenchement via la flèche de sélection ;
 * l'effet réel s'applique à {@code allTargets}.
 */
public class UltimateTching extends Ultimate {

    @Override
    public void execute(Entity caster, Entity target, List<Entity> allTargets) {
        int degats = caster.getAttackPoints() * 2;
        System.out.println(caster.getName() + " déclenche une ONDE DE CHOC dévastatrice !");
        for (Entity t : allTargets) {
            if (t.isAlive()) {
                System.out.println("L'onde frappe " + t.getName());
                t.takeDamage(degats);
            }
        }
    }

    @Override
    public UltimateAnimation createUltimateAnimation() {
        return new TchingUltimateAnimation();
    }

    @Override
    public BackgroundAnimation createBackgroundAnimation() {
        return new TchingBackgroundAnimation();
    }

    @Override
    public String getName() {
        return "Onde de Choc";
    }
}
