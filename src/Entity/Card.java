package Entity;

import java.util.List;

import battleSystem.SoundPlayer;
import battleSystem.ai.CardFormContext;
import battleSystem.ai.CombatStats;
import battleSystem.ai.FormSelector;
import battleSystem.ai.RuleBasedFormSelector;
import battleSystem.animation.BackgroundAnimation;
import battleSystem.animation.UltimateAnimation;
import battleSystem.forms.CardForm;
import battleSystem.forms.CardFormAntiBoost;
import battleSystem.forms.CardFormAntiBrulure;
import battleSystem.forms.CardFormAntiProvocation;
import battleSystem.ultimate.Ultimate;
import battleSystem.ultimate.UltimateCard;

/**
 * Boss "Card" : carrure imposante, portails téléporteurs qui arrachent la forêt
 * pour l'abattre sur ses adversaires. S'adapte au style de jeu adverse en changeant
 * de forme de combat (anti-provocation / anti-brûlure / anti-boost / neutre).
 */
public class Card extends Entity implements UltimateCapable {

    // --- Seuils & cooldowns ---
    private static final float ULTI_HP_THRESHOLD = 0.50f;
    private static final int ULTI_COOLDOWN = 5;
    private static final float SUMMON_HP_THRESHOLD = 0.30f;
    private static final int SUMMON_COOLDOWN = 8;

    // --- Composition (mêmes patterns que Bob / Flamme / Tching) ---
    private final Ultimate ultimate = new UltimateCard();
    private final CombatStats combatStats = new CombatStats();
    private final List<CardForm> formesDisponibles;
    private FormSelector formSelector = new RuleBasedFormSelector();

    // --- État des formes ---
    private CardForm formeActuelle;
    private CardForm formeProchaine;

    // --- Progression / compteurs ---
    private int turnCounter = 0;
    private int atkStage = 0;
    private int defStage = 0;
    private int ultiCooldown = ULTI_COOLDOWN;
    private boolean seuilUltiFranchi = false;
    private int summonCooldown = 0;
    private Entity renfortActuel = null;

    public Card(String name, int hp, int attackPoints, int defPoints) {
        super(name, hp, attackPoints, defPoints);
        this.formeActuelle = new CardForm("Forme Neutre");
        this.formesDisponibles = List.of(
                formeActuelle,
                new CardFormAntiProvocation(),
                new CardFormAntiBrulure(),
                new CardFormAntiBoost()
        );
    }

    /** Permet de brancher plus tard votre sélecteur basé sur le modèle entraîné. */
    public void setFormSelector(FormSelector selector) {
        this.formSelector = selector;
    }

    // ------------------------------------------------------------------
    // Tour de jeu
    // ------------------------------------------------------------------

    @Override
    public void performTurn(Entity target) {
        turnCounter++;
        if (formeProchaine != null) {
            formeActuelle = formeProchaine;
        }

        System.out.println("\n=== Tour de " + getName() + " [" + formeActuelle.getNom() + "] — PV: "
                + getHp() + "/" + getMaxHp() + " ===");

        detecterComportementAdverse(target);

        if (canUseUltimate()) {
            declencherUltime(target);
        } else {
            choisirEtLancerAttaque(target);
        }
    }

    private void detecterComportementAdverse(Entity target) {
        if (target instanceof Bob && ((Bob) target).isTaunting()) {
            combatStats.registerTaunt();
        }
        if (target.isAttackBoosted() || target.isDefenseBoosted()) {
            combatStats.registerBuff();
        }
    }

    private void declencherUltime(Entity target) {
        System.out.println(getName() + " ouvre la totalité de la forêt derrière lui...");
        SoundPlayer.play("/assets/UltiSound.wav");
        useUltimate(target, List.of(target));
        resetUlti();
    }

    private void choisirEtLancerAttaque(Entity target) {
        int roll = RAND.nextInt(10) + 1;
        if (roll <= 6) {
            chargeDesPortailsSylvestres(target);
        } else {
            poigneImposante(target);
        }
    }

    // ------------------------------------------------------------------
    // Attaques
    // ------------------------------------------------------------------

    public void chargeDesPortailsSylvestres(Entity target) {
        System.out.println(getName() + " déchire des portails béants au-dessus de la canopée et fait "
                + "s'écraser une pluie d'arbres déracinés sur " + target.getName() + " !");
        int base = (getAttackPoints() * 3) + (getDefPoints() / 2);
        int degats = calculerDegatsAvecForme(base, target);
        target.takeDamage(degats);
        combatStats.registerDamageDone(degats);
    }

    public void poigneImposante(Entity target) {
        System.out.println(getName() + " referme sa poigne imposante autour de " + target.getName()
                + " et resserre implacablement !");
        int base = (getAttackPoints() * 2) + getDefPoints();
        int degats = calculerDegatsAvecForme(base, target);
        target.takeDamage(degats);
        combatStats.registerDamageDone(degats);
    }

    private int calculerDegatsAvecForme(int degatsDeBase, Entity cible) {
        double total = degatsDeBase * formeActuelle.multiplicateurDegatsInfliges();
        total += formeActuelle.bonusPunitionProvocation(combatStats, cible);
        total += formeActuelle.bonusPunitionBoost(combatStats, cible);
        return Math.max(0, (int) Math.round(total));
    }

    // ------------------------------------------------------------------
    // Invocation de renforts (sous 30% HP)
    // ------------------------------------------------------------------

    public void invoquerRenfort(List<Entity> renforts) {
        if (!canSummon()) return;

        int vieBase = (getHp() / 10) + 50;
        int atkBase = (getAttackPoints() / 10) + 5;
        int defBase = (getDefPoints() / 10) + 5;

        Entity renfort;
        int choix = RAND.nextInt(3);
        switch (choix) {
            case 0:
                renfort = new DarkFlamme("Écho Sylvestre de Flamme", vieBase, atkBase + 10, defBase);
                break;
            case 1:
                renfort = new DarkTching("Écho Sylvestre de Tching", vieBase, atkBase, defBase + 6);
                break;
            default:
                renfort = new DarkBob("Écho Sylvestre de Bob", vieBase + 25, atkBase, defBase);
                break;
        }

        renforts.add(renfort);
        renfortActuel = renfort;
        summonCooldown = SUMMON_COOLDOWN;
        System.out.println(getName() + " déchire un portail et arrache " + renfort.getName()
                + " du cœur de la forêt !");
    }

    public boolean canSummon() {
        boolean seuilFranchi = getHpPercent() <= SUMMON_HP_THRESHOLD;
        boolean pasDeRenfortVivant = (renfortActuel == null) || !renfortActuel.isAlive();
        return seuilFranchi && pasDeRenfortVivant && summonCooldown <= 0;
    }

    // ------------------------------------------------------------------
    // Fin de tour : régénération, croissance de stats, seuils, forme suivante
    // ------------------------------------------------------------------

    @Override
    public void applyPostTurnEffects() {
        if (burnTicks > 0) {
            int degatsBruts = (getHp() * (5 + burnTicks)) / 100;
            double resistance = formeActuelle.resistanceBrulure(combatStats);
            int degatsFinaux = (int) Math.round(degatsBruts * (1.0 - resistance));
            setHp(getHp() - Math.max(0, degatsFinaux));
            burnTicks--;
            System.out.println(getName() + " encaisse " + degatsFinaux + " PV de brûlure (résistance "
                    + (int) (resistance * 100) + "%).");
        }

        int soin = calculerSoin();
        setHp(getHp() + soin);
        System.out.println(getName() + " régénère " + soin + " PV. (" + getHp() + "/" + getMaxHp() + ")");

        croissanceStatsAleatoire();

        if (getHpPercent() <= ULTI_HP_THRESHOLD) seuilUltiFranchi = true;
        décompteUlti();
        if (summonCooldown > 0) summonCooldown--;

        combatStats.endTurn();

        formeProchaine = formSelector.selectForm(construireContexte(), combatStats, formesDisponibles);
        System.out.println(getName() + " envisage de passer en [" + formeProchaine.getNom() + "] au prochain tour.");
    }

    private int calculerSoin() {
        int soinBase = (getMaxHp() * 6) / 100 + (getDefPoints() / 4);
        int bonusAntiBrulure = formeActuelle.bonusPunitionBrulure(combatStats);
        return soinBase + bonusAntiBrulure;
    }

    private void croissanceStatsAleatoire() {
        int montant = RAND.nextInt(5) + 1; // 1 à 5
        if (RAND.nextBoolean()) {
            setAttackPoints(getAttackPoints() + montant);
            atkStage += montant;
            System.out.println(getName() + " se renforce : ATK +" + montant + " !");
        } else {
            setDefPoints(getDefPoints() + montant);
            defStage += montant;
            System.out.println(getName() + " se renforce : DEF +" + montant + " !");
        }
    }

    private CardFormContext construireContexte() {
        return new CardFormContext(
                turnCounter,
                getHpPercent(),
                atkStage,
                defStage,
                combatStats.getDamageTakenLast3Turns(),
                combatStats.getDamageDoneLast3Turns(),
                combatStats.getTauntCountLast5Turns(),
                combatStats.getBurnCountLast5Turns(),
                combatStats.getBuffCountLast5Turns(),
                canUseUltimate(),
                canSummon(),
                formeActuelle.getNom()
        );
    }

    // ------------------------------------------------------------------
    // Tracking des dégâts subis / brûlure (alimente CombatStats)
    // ------------------------------------------------------------------

    @Override
    public void takeDamage(int amount) {
        int avant = getHp();
        super.takeDamage(amount);
        combatStats.registerDamageTaken(Math.max(0, avant - getHp()));
    }

    @Override
    public void setBurn(int ticks) {
        if (ticks > this.burnTicks) {
            combatStats.registerBurn();
        }
        super.setBurn(ticks);
    }

    // ------------------------------------------------------------------
    // Accesseurs pour BattleTrainingLogger (noms alignés sur loggerturn/loggerfight)
    // ------------------------------------------------------------------

    public float getHpPercent() { return (float) getHp() / (float) getMaxHp(); }
    public int getAtkStage() { return atkStage; }
    public int getDefStage() { return defStage; }
    public boolean canUseUltimate() { return isUltimateReady(); }
    public String getCurrentFormName() { return formeActuelle.getNom(); }
    public String getSelectedFormName() {
        return (formeProchaine != null) ? formeProchaine.getNom() : formeActuelle.getNom();
    }

    // ------------------------------------------------------------------
    // UltimateCapable — délégation à l'objet Ultimate (composition)
    // ------------------------------------------------------------------

    @Override
    public void useUltimate(Entity target, List<Entity> allTargets) {
        ultimate.execute(this, target, allTargets);
    }

    @Override
    public void décompteUlti() {
        if (ultiCooldown > 0) ultiCooldown--;
    }

    @Override
    public boolean isUltimateReady() {
        return seuilUltiFranchi && ultiCooldown <= 0;
    }

    @Override
    public void resetUlti() {
        this.ultiCooldown = ULTI_COOLDOWN;
    }

    @Override
    public int getUltiTicksRemaining() { return ultiCooldown; }

    @Override
    public int getUltiTicksMax() { return ULTI_COOLDOWN; }

    @Override
    public String getUltimateName() { return ultimate.getName(); }

    @Override
    public UltimateAnimation createUltimateAnimation() { return ultimate.createUltimateAnimation(); }

    @Override
    public BackgroundAnimation createBackgroundAnimation() { return ultimate.createBackgroundAnimation(); }
}