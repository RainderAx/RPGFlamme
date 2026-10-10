package battleSystem.ai;

import java.util.List;

import battleSystem.forms.CardForm;

/**
 * Sélecteur heuristique : choisit la forme dont evaluerPertinence() est la plus haute.
 * Sert (1) de comportement par défaut jouable dès maintenant, et (2) de générateur
 * de données/"expert" si vous voulez faire de l'apprentissage par imitation.
 */
public class RuleBasedFormSelector implements FormSelector {
    @Override
    public CardForm selectForm(CardFormContext context, CombatStats stats, List<CardForm> candidates) {
        CardForm meilleure = candidates.get(0);
        double meilleurScore = Double.NEGATIVE_INFINITY;
        for (CardForm forme : candidates) {
            double score = forme.evaluerPertinence(stats, context.getHpPercent());
            if (score > meilleurScore) {
                meilleurScore = score;
                meilleure = forme;
            }
        }
        return meilleure;
    }
}