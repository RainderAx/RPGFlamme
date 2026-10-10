package battleSystem.ai;

import java.util.List;

import battleSystem.forms.CardForm;

/**
 * Stratégie de choix de forme pour Card. Deux implémentations fournies :
 * - RuleBasedFormSelector : heuristique immédiatement utilisable.
 * - MLFormSelector : point d'extension pour votre modèle entraîné.
 */
public interface FormSelector {
    CardForm selectForm(CardFormContext context, CombatStats stats, List<CardForm> candidates);
}