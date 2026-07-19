package battleSystem;

import Entity.Entity;

/**
 * Permet à la vue (JeuImage) de réagir aux événements du moteur de combat
 * sans jamais prendre de décision métier elle-même.
 */
public interface BattleSystemListener {
    void onTargetSelectionStarted(String actionType);
    void onTargetSelectionCancelled();
    void onUltimateStarted(Entity caster);
    void onUltimateEnded(Entity caster);
    void onTurnChanged(Entity currentHero, boolean isPlayerPhase);
    void onBattleEnded(boolean victory);
}
