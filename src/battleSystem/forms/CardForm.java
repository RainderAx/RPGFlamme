package battleSystem.forms;

import Entity.Bob;
import Entity.Entity;
import battleSystem.ai.CombatStats;

/**
 * Forme de combat de Card. Classe de base CONCRÈTE : instanciée telle quelle,
 * elle représente la "Forme Neutre" (réglages modérés sur les 3 axes de punition).
 * Les 3 sous-classes héritent de ces réglages modérés et poussent un seul axe à l'extrême.
 */
public class CardForm {
    private final String nom;

    public CardForm(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    /** Multiplicateur appliqué aux dégâts infligés par Card dans cette forme. */
    public double multiplicateurDegatsInfliges() {
        return 1.0;
    }

    /** Résistance (0.0 à 1.0) aux dégâts de brûlure subis par Card. */
    public double resistanceBrulure(CombatStats stats) {
        return 0.10;
    }

    /** Bonus de soin de fin de tour lié à la punition anti-brûlure. */
    public int bonusPunitionBrulure(CombatStats stats) {
        return stats.getBurnCountLast5Turns() * 2;
    }

    /** Bonus de dégâts (additif) contre une cible en train de provoquer (ex: Bob). */
    public int bonusPunitionProvocation(CombatStats stats, Entity cible) {
        if (cible instanceof Bob && ((Bob) cible).isTaunting()) {
            return stats.getTauntCountLast5Turns() * 4;
        }
        return 0;
    }

    /** Bonus de dégâts (additif) contre une cible qui abuse des boosts de stats. */
    public int bonusPunitionBoost(CombatStats stats, Entity cible) {
        if (cible.isAttackBoosted() || cible.isDefenseBoosted()) {
            return stats.getBuffCountLast5Turns() * 4;
        }
        return 0;
    }

    /** Score de pertinence utilisé par le sélecteur de forme (heuristique ou IA). */
    public double evaluerPertinence(CombatStats stats, float hpRatio) {
        return 1.0;
    }
}