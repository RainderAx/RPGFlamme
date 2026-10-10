package battleSystem.ai;

/**
 * Photo de l'état de combat à la fin d'un tour de Card. Ces champs correspondent
 * (presque) exactement aux colonnes de votre BattleTrainingLogger : c'est
 * volontaire, pour que le vecteur de features utilisé en jeu et celui utilisé
 * à l'entraînement soient identiques.
 */
public final class CardFormContext {
    private final int turn;
    private final float hpPercent;
    private final int atkStage;
    private final int defStage;
    private final int damageTakenLast3Turns;
    private final int damageDoneLast3Turns;
    private final int tauntCountLast5Turns;
    private final int burnCountLast5Turns;
    private final int buffCountLast5Turns;
    private final boolean ultimateReady;
    private final boolean summonReady;
    private final String currentFormName;

    public CardFormContext(int turn, float hpPercent, int atkStage, int defStage,
                            int damageTakenLast3Turns, int damageDoneLast3Turns,
                            int tauntCountLast5Turns, int burnCountLast5Turns, int buffCountLast5Turns,
                            boolean ultimateReady, boolean summonReady, String currentFormName) {
        this.turn = turn;
        this.hpPercent = hpPercent;
        this.atkStage = atkStage;
        this.defStage = defStage;
        this.damageTakenLast3Turns = damageTakenLast3Turns;
        this.damageDoneLast3Turns = damageDoneLast3Turns;
        this.tauntCountLast5Turns = tauntCountLast5Turns;
        this.burnCountLast5Turns = burnCountLast5Turns;
        this.buffCountLast5Turns = buffCountLast5Turns;
        this.ultimateReady = ultimateReady;
        this.summonReady = summonReady;
        this.currentFormName = currentFormName;
    }

    public int getTurn() { return turn; }
    public float getHpPercent() { return hpPercent; }
    public int getAtkStage() { return atkStage; }
    public int getDefStage() { return defStage; }
    public int getDamageTakenLast3Turns() { return damageTakenLast3Turns; }
    public int getDamageDoneLast3Turns() { return damageDoneLast3Turns; }
    public int getTauntCountLast5Turns() { return tauntCountLast5Turns; }
    public int getBurnCountLast5Turns() { return burnCountLast5Turns; }
    public int getBuffCountLast5Turns() { return buffCountLast5Turns; }
    public boolean isUltimateReady() { return ultimateReady; }
    public boolean isSummonReady() { return summonReady; }
    public String getCurrentFormName() { return currentFormName; }
}