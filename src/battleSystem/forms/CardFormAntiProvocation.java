package battleSystem.forms;

import Entity.Bob;
import Entity.Entity;
import battleSystem.ai.CombatStats;

/** Forme punissant extrêmement les adversaires qui abusent de la provocation de Bob. */
public class CardFormAntiProvocation extends CardForm {

    public CardFormAntiProvocation() {
        super("Forme Anti-Provocation");
    }

    @Override
    public double multiplicateurDegatsInfliges() {
        return 1.15;
    }

    @Override
    public int bonusPunitionProvocation(CombatStats stats, Entity cible) {
        if (cible instanceof Bob && ((Bob) cible).isTaunting()) {
            return stats.getTauntCountLast5Turns() * 18; // punition extrême, volontairement disproportionnée
        }
        return 0;
    }

    @Override
    public double evaluerPertinence(CombatStats stats, float hpRatio) {
        return stats.getTauntCountLast5Turns() * 6.0;
    }
}