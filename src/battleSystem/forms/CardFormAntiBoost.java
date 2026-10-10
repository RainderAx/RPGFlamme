package battleSystem.forms;

import Entity.Entity;
import battleSystem.ai.CombatStats;

/** Forme punissant extrêmement les adversaires qui abusent des boosts de statistiques. */
public class CardFormAntiBoost extends CardForm {

    public CardFormAntiBoost() {
        super("Forme Anti-Boost");
    }

    @Override
    public double multiplicateurDegatsInfliges() {
        return 1.10;
    }

    @Override
    public int bonusPunitionBoost(CombatStats stats, Entity cible) {
        if (cible.isAttackBoosted() || cible.isDefenseBoosted()) {
            return stats.getBuffCountLast5Turns() * 18; // punition extrême
        }
        return 0;
    }

    @Override
    public double evaluerPertinence(CombatStats stats, float hpRatio) {
        return stats.getBuffCountLast5Turns() * 6.0;
    }
}