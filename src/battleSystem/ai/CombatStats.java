package battleSystem.ai;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Version enrichie de votre CombatStats : les compteurs "LastNTurns" sont désormais
 * de VRAIES fenêtres glissantes (auparavant les compteurs s'accumulaient sans jamais
 * redescendre). Les noms de méthodes publiques n'ont pas changé : vos fichiers
 * loggerfight.java / loggerturn.java / BattleTrainingLogger.java restent compatibles
 * sans aucune modification.
 *
 * Nouveauté : appelez endTurn() une fois par tour pour faire glisser les fenêtres.
 * (Card le fait automatiquement dans applyPostTurnEffects().)
 */
public class CombatStats {

    private static final int DAMAGE_WINDOW = 3;
    private static final int EVENT_WINDOW = 5;

    private final Deque<Integer> damageTakenBuckets = new ArrayDeque<>();
    private final Deque<Integer> damageDoneBuckets = new ArrayDeque<>();
    private final Deque<Integer> tauntBuckets = new ArrayDeque<>();
    private final Deque<Integer> burnBuckets = new ArrayDeque<>();
    private final Deque<Integer> buffBuckets = new ArrayDeque<>();

    private int currentDamageTaken = 0;
    private int currentDamageDone = 0;
    private int currentTaunts = 0;
    private int currentBurns = 0;
    private int currentBuffs = 0;

    public void registerTaunt() { currentTaunts++; }
    public void registerBurn() { currentBurns++; }
    public void registerBuff() { currentBuffs++; }
    public void registerDamageTaken(int amount) { currentDamageTaken += amount; }
    public void registerDamageDone(int amount) { currentDamageDone += amount; }

    /** À appeler une fois par tour (fin de tour) pour faire glisser les fenêtres. */
    public void endTurn() {
        pushAndTrim(damageTakenBuckets, currentDamageTaken, DAMAGE_WINDOW);
        pushAndTrim(damageDoneBuckets, currentDamageDone, DAMAGE_WINDOW);
        pushAndTrim(tauntBuckets, currentTaunts, EVENT_WINDOW);
        pushAndTrim(burnBuckets, currentBurns, EVENT_WINDOW);
        pushAndTrim(buffBuckets, currentBuffs, EVENT_WINDOW);

        currentDamageTaken = 0;
        currentDamageDone = 0;
        currentTaunts = 0;
        currentBurns = 0;
        currentBuffs = 0;
    }

    private void pushAndTrim(Deque<Integer> deque, int value, int windowSize) {
        deque.addLast(value);
        while (deque.size() > windowSize) deque.removeFirst();
    }

    private int sum(Deque<Integer> deque) {
        int total = 0;
        for (int v : deque) total += v;
        return total;
    }

    public int getDamageTakenLast3Turns() { return sum(damageTakenBuckets); }
    public int getDamageDoneLast3Turns() { return sum(damageDoneBuckets); }
    public int getTauntCountLast5Turns() { return sum(tauntBuckets); }
    public int getBurnCountLast5Turns() { return sum(burnBuckets); }
    public int getBuffCountLast5Turns() { return sum(buffBuckets); }
}