package battleSystem.ultimate;

import java.util.List;

import Entity.Entity;
import battleSystem.animation.BackgroundAnimation;
import battleSystem.animation.CardBackgroundAnimation;
import battleSystem.animation.CardUltimateAnimation;
import battleSystem.animation.UltimateAnimation;

/**
 * Capacité ultime de Card : arrache l'intégralité d'une forêt à travers ses portails
 * et la fait s'effondrer sur sa cible. Dévastateur, à cible unique pour l'instant
 * (Card n'a accès qu'à sa cible assignée pendant le tour des monstres — voir README
 * pour une piste d'évolution en AoE).
 */
public class UltimateCard extends Ultimate {

    @Override
    public void execute(Entity caster, Entity target, List<Entity> allTargets) {
        System.out.println(caster.getName() + " déchire la totalité de la forêt et la fait s'effondrer "
                + "dans un cataclysme de portails : EFFONDREMENT SYLVESTRE ABSOLU !");
        int degats = (caster.getAttackPoints() * 5) + (caster.getDefPoints() * 2);
        target.takeDamage(degats);
    }

    @Override
    public UltimateAnimation createUltimateAnimation() {
        return new CardUltimateAnimation();
    }

    @Override
    public BackgroundAnimation createBackgroundAnimation() {
        return new CardBackgroundAnimation();
    }

    @Override
    public String getName() {
        return "Effondrement Sylvestre Absolu";
    }
}