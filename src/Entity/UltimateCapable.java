package Entity;

import java.util.List;

import battleSystem.animation.BackgroundAnimation;
import battleSystem.animation.UltimateAnimation;

/**
 * Contrat implémenté par toute entité capable de déclencher une capacité Ultime.
 * <p>
 * Le BattleSystem ne dépend JAMAIS d'une implémentation concrète (Bob, Flamme, Tching...) :
 * il appelle uniquement les méthodes de cette interface. Chaque implémentation sait, par
 * polymorphisme, exécuter sa propre logique et fabriquer ses propres animations.
 */
public interface UltimateCapable {

    /** Exécute les effets de jeu de l'ultime (dégâts, buffs, etc.). Logique pure, sans rendu. */
    void useUltimate(Entity target, List<Entity> allTargets);

    /** Fait avancer le décompte d'un tour (à appeler en fin de tour du héros). */
    void décompteUlti();

    /** true si l'ultime peut être déclenché ce tour-ci. */
    boolean isUltimateReady();

    /** Réinitialise le décompte après utilisation de l'ultime. */
    void resetUlti();

    int getUltiTicksRemaining();

    int getUltiTicksMax();

    String getUltimateName();

    /** Fabrique l'animation de premier plan spécifique à cette entité. */
    UltimateAnimation createUltimateAnimation();

    /** Fabrique l'animation de fond spécifique à cette entité. */
    BackgroundAnimation createBackgroundAnimation();
}
