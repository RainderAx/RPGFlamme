package battleSystem;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

import Entity.Bob;
import Entity.Entity;
import Entity.Flamme;
import Entity.Tching;
import Entity.UltimateCapable;
import battleSystem.animation.AnimationManager;


public class BattleSystem {

    private final List<Entity> heroes;
    private final List<Entity> monsters;
    private final AnimationManager animationManager = new AnimationManager();
    private final Random rand = new Random();

    private int heroIndexTurn = 0;
    private boolean isPlayerPhase = true;
    private boolean isSelectingTarget = false;
    private boolean commandsLocked = false;
    private String pendingAction = "";
    private Entity target;

    private BattleSystemListener listener;

    public BattleSystem(List<Entity> heroes, List<Entity> monsters) {
        this.heroes = heroes;
        this.monsters = monsters;
        this.target = monsters.isEmpty() ? null : monsters.get(0);
    }

    public void setListener(BattleSystemListener listener) {
        this.listener = listener;
    }

    public void update() {
        animationManager.update();
    }


    public void prepareAction(String actionType) {
        if (commandsLocked || !isPlayerPhase) return;

        Entity currentHero = getCurrentHero();
        if (currentHero == null) return;

        if ("ULTI".equals(actionType)) {
            if (!(currentHero instanceof UltimateCapable) || !((UltimateCapable) currentHero).isUltimateReady()) {
                return;
            }
        }

        this.isSelectingTarget = true;
        this.pendingAction = actionType;
        if (listener != null) listener.onTargetSelectionStarted(actionType);
    }

    public void cancelTargetSelection() {
        if (!isSelectingTarget) return;
        isSelectingTarget = false;
        pendingAction = "";
        if (listener != null) listener.onTargetSelectionCancelled();
    }


    public Entity trySelectTarget(Point click, List<Rectangle> monsterHitboxes) {
        if (!isSelectingTarget || commandsLocked) return null;

        for (int i = 0; i < monsters.size(); i++) {
            Entity m = monsters.get(i);
            if (m.isAlive() && i < monsterHitboxes.size() && monsterHitboxes.get(i).contains(click)) {
                isSelectingTarget = false;
                target = m;
                executePendingAction(m);
                return m;
            }
        }
        return null;
    }
    private void triggerArrowChecks() {
        for (Entity h : heroes) {
            if (h instanceof Flamme) {
                Flamme flamme = (Flamme) h;
                for (Entity m : monsters) {
                    if (m.isAlive()) {
                        flamme.checkUlti(m);
                    }
                }
            }
        }
    }

    private void executePendingAction(Entity chosenTarget) {
        Entity currentHero = getCurrentHero();
        if (currentHero == null) return;
        commandsLocked = true;

        if ("ULTI".equals(pendingAction)) {
            launchUltimate(currentHero, chosenTarget);
        } else if ("ATTACK".equals(pendingAction)) {
            currentHero.attack(chosenTarget);
            commandsLocked = false;
            finishHeroTurn();
        } else {
     
            executeHeroSpecificAction(currentHero, chosenTarget, pendingAction);
            commandsLocked = false;
            finishHeroTurn();
        }
    }

    private void executeHeroSpecificAction(Entity hero, Entity target, String action) {
        if (hero instanceof Flamme) {
            Flamme f = (Flamme) hero;
            switch (action) {
                case "BRIQUET": f.briquet(target); break;
                case "ENCENS": f.encensDuTigre(); break;
                case "PREP_MENTALE": f.preparationMentale(); break;
            }
        } else if (hero instanceof Bob) {
            Bob b = (Bob) hero;
            switch (action) {
                case "MARTEAU": b.coupDuMarteau(target); break;
                case "PROVOC": b.activerProvocation(); break;
                case "SUPER_PROVOC": b.superProvoc(); break;
                case "PERCEUSE": b.perceuse(target);break;
            }
        } else if (hero instanceof Tching) {
            Tching t = (Tching) hero;
            switch (action) {
                case "RAPIDE": t.coupRapide(target); break;
                case "TIGRE": t.techniqueDuTigre(target); break;
                case "ENTRAINEMENT": t.entrainementIntensif(); break;
                case "ZONE": t.zoneAttack(monsters); break;
            }
        }
    }

    private void launchUltimate(Entity caster, Entity chosenTarget) {
        UltimateCapable uCaster = (UltimateCapable) caster;

        
        SoundPlayer.play("/assets/UltiSound.wav");
        if (listener != null) listener.onUltimateStarted(caster);

        animationManager.play(
                uCaster.createBackgroundAnimation(),
                uCaster.createUltimateAnimation(),
                () -> {
                    uCaster.useUltimate(chosenTarget, monsters);
                    uCaster.resetUlti();
                    commandsLocked = false;
                    if (listener != null) listener.onUltimateEnded(caster);
                    finishHeroTurn();
                }
        );
    }

    private void finishHeroTurn() {
        Entity h = getCurrentHero();
        if (h == null) return;

        h.applyPostTurnEffects();
        if (h instanceof UltimateCapable) ((UltimateCapable) h).décompteUlti();
        triggerArrowChecks(); 

        heroIndexTurn++;

        if (endBattleIfNeeded()) return;

        if (heroIndexTurn >= heroes.size()) {
            isPlayerPhase = false;
            if (listener != null) listener.onTurnChanged(null, false);

            Timer pause = new Timer(1500, e -> monstersTurn());
            pause.setRepeats(false);
            pause.start();
        } else {
            if (listener != null) listener.onTurnChanged(getCurrentHero(), true);
        }
    }

    private void monstersTurn() {
        for (Entity m : monsters) {
            if (m.isAlive() && isTeamAlive(heroes)) {
                Entity victim = findTargetForMonsters();
                m.performTurn(victim);
                m.applyPostTurnEffects();
                triggerArrowChecks();
            }
        }

        heroIndexTurn = 0;
        isPlayerPhase = true;

        if (endBattleIfNeeded()) return;
        if (listener != null) listener.onTurnChanged(getCurrentHero(), true);
    }

    private boolean endBattleIfNeeded() {
        if (!isTeamAlive(heroes)) {
            isPlayerPhase = false;
            if (listener != null) listener.onBattleEnded(false);
            return true;
        }
        if (!isTeamAlive(monsters)) {
            isPlayerPhase = false;
            if (listener != null) listener.onBattleEnded(true);
            return true;
        }
        return false;
    }

    private Entity findTargetForMonsters() {
        for (Entity h : heroes) {
            if (h instanceof Bob && h.isAlive() && ((Bob) h).isTaunting()) return h;
        }
        List<Entity> alive = heroes.stream().filter(Entity::isAlive).toList();
        return alive.get(rand.nextInt(alive.size()));
    }

    private boolean isTeamAlive(List<Entity> team) {
        return team.stream().anyMatch(Entity::isAlive);
    }

    public Entity getCurrentHero() {
        return (heroIndexTurn < heroes.size()) ? heroes.get(heroIndexTurn) : null;
    }

    public List<Entity> getHeroes() { return heroes; }
    public List<Entity> getMonsters() { return monsters; }
    public Entity getTarget() { return target; }
    public boolean isPlayerPhase() { return isPlayerPhase; }
    public boolean isSelectingTarget() { return isSelectingTarget; }
    public boolean isCommandsLocked() { return commandsLocked; }
    public String getPendingAction() { return pendingAction; }
    public AnimationManager getAnimationManager() { return animationManager; }
}
