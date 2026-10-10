package battleSystem.forms;

import battleSystem.ai.CombatStats;

/** Forme punissant extrêmement les adversaires qui multiplient les brûlures sur Card. */
public class CardFormAntiBrulure extends CardForm {

    public CardFormAntiBrulure() {
        super("Forme Anti-Brûlure");
    }

    @Override
    public double resistanceBrulure(CombatStats stats) {
        return Math.min(0.85, 0.20 + stats.getBurnCountLast5Turns() * 0.10); // plafonnée à 85%
    }

    @Override
    public int bonusPunitionBrulure(CombatStats stats) {
        return stats.getBurnCountLast5Turns() * 12; // Card se nourrit des flammes qu'on lui inflige
    }

    @Override
    public double evaluerPertinence(CombatStats stats, float hpRatio) {
        return stats.getBurnCountLast5Turns() * 6.0;
    }
}