# FlammeLeJV — RPG au tour par tour en Java Swing

Un RPG tactique en équipe, développé en **Java (Swing)**, adapté du manga *[Flamme](https://www.mangadraft.com/manga/flamme.4)* de Hoki Aru.

Plongez dans une aventure inédite au format **Light Novel**, où vous retrouvez Flamme et ses compagnons dans des combats au tour par tour riches en mécaniques :

* **Combats dynamiques :** capacités ultimes animées, systèmes de brûlure, de provocation et de transformation.
* **Boss adaptatif (Card) :** un boss intelligent conçu sur mesure pour l'entraînement d'IA.

---

##  Fonctionnalités

-  **Combat en équipe** : phase des héros, puis phase des monstres.
-  **Sélection de cible graphique** : des flèches apparaissent au-dessus des ennemis, on clique pour choisir.
-  **Capacités Ultimes** avec décompte (cooldown), animation de fond + animation de premier plan, tremblements d'écran et son.
-  **Barres de vie stylisées** (dégradé, survol agrandi, icônes de statut : brûlure, ATK/DEF boostée ou réduite).
-  **Console intégrée** : la sortie `System.out` est redirigée vers un panneau stylisé dans le jeu.
-  **Architecture modulaire** : ajouter un héros avec un ultime = créer de nouvelles classes, sans toucher au moteur.
-  **Boss Card + IA** : un boss qui change de forme selon le style de jeu du joueur, avec un logger CSV pour entraîner un modèle.

---

##  Les personnages

### Héros

| Héros | Rôle | Actions | Ultime |
|---|---|---|---|
| **Flamme** | Dégâts / brûlure | Briquet (dégâts + brûlure), Encens du Tigre (ATK +50), Préparation mentale (attaque chargée) | **Flèche Ardente** : dégâts ×3, flèche plantée, brûlure, *OVERKILL* si la cible est faible |
| **Tching** | Combattant polyvalent | Coup Rapide, Technique du Tigre (attaques répétées), Entraînement intensif (ATK/DEF +10) | **Onde de Choc** : dégâts de zone sur tous les ennemis |
| **Bob** | Tank | Marteau, Provocation ; une fois transformé : Super Provoc, Perceuse | **Super Bob** : transformation temporaire + gros dégâts basés sur la défense |

### Monstres

| Monstre | Description |
|---|---|
| **Gobelin** | Ennemi de base |
| **Chef Gobelin** | Entre en rage aléatoirement (ATK/DEF) et peut invoquer des sbires |
| **DarkFlamme / DarkTching / DarkBob** | Versions sombres des héros, avec une IA de choix d'attaque pondérée et une attaque de dernier recours sous 30 % de PV |
| **Card** (boss) | Boss à formes multiples — voir [`CardBossReadMe.md`](CardBossReadMe.md) |

---

## Architecture

```
src/
├── module-info.java
├── Entity/                      # Modèle : héros et monstres
│   ├── Entity.java              #   classe de base (PV, ATK, DEF, brûlure, flèche…)
│   ├── UltimateCapable.java     #   interface des entités ayant un ultime
│   ├── Flamme.java · Tching.java · Bob.java
│   ├── DarkFlamme.java · DarkTching.java · DarkBob.java
│   ├── Gobelin.java · chefGobelin.java · Fighter.java
│   └── Card.java                #   boss adaptatif
└── battleSystem/
    ├── Main.java                # Point d'entrée
    ├── BattleSystem.java        # Moteur de combat (logique pure)
    ├── BattleSystemListener.java# Contrat moteur → vue
    ├── JeuImage.java            # Vue Swing (rendu + entrées souris)
    ├── HealthBar.java · ActionButton.java · ButtonUlti.java
    ├── ActionMenuPanel.java · StyledConsole.java · SoundPlayer.java
    ├── ultimate/                # Logique des ultimes (Strategy)
    │   ├── Ultimate.java        #   classe mère abstraite
    │   └── UltimateBob / UltimateFlamme / UltimateTching
    ├── animation/               # Moteur d'animation
    │   ├── Animation.java · AnimationManager.java
    │   ├── BackgroundAnimation.java · UltimateAnimation.java
    │   └── Bob* / Flamme* / Tching* (fond + premier plan)
    ├── forms/                   # Formes du boss Card
    └── ai/                      # Sélecteurs de forme, stats de combat, logger CSV
```

### Principes de conception

- **Séparation logique / vue** : `BattleSystem` gère les tours, les cibles, les ultimes ; `JeuImage` ne fait que dessiner et relayer les clics. La vue est notifiée via `BattleSystemListener`.
- **Polymorphisme (Strategy)** : chaque héros délègue son ultime à un objet `Ultimate`. Le moteur appelle uniquement `UltimateCapable` (`useUltimate`, `createUltimateAnimation`, …) sans jamais connaître le héros concret (**OCP**).
- **Cycle de vie d'un ultime** : clic sur *Ultime* → choix de la cible → verrouillage des commandes → son + animations → exécution des effets à la fin de l'animation → fin du tour du héros.
- **Animations génériques** : `AnimationManager` pilote un fond (`BackgroundAnimation`) et un premier plan (`UltimateAnimation`) sans rien savoir du jeu.

---

##  Lancer le projet

### Prérequis

- **JDK 16 ou plus** (le code utilise `Stream.toList()`)
- Un IDE Java (Eclipse, IntelliJ…) ou la ligne de commande
- Le module `java.desktop` (déjà déclaré dans `module-info.java`)

### Ressources

Les images et sons sont chargés depuis le classpath, dans un dossier `/assets` (à placer dans les ressources du projet) :

```
assets/
├── Background_Forest.jpg
├── flamme.png · tching.png · Bob.png
├── Sprite_BoB_transfo.png · BoB_Ultimate_frame.png · Flamme_Ultimate.png
├── Sprite_Monster.png · Chef_Gobelin.png · gobelin.png
├── Sprite_Tching_Shadow.png · Sprite_Flamme_Shadow.png · Sprite_Bob_Shadow.png
├── burnt.png
└── UltiSound.wav
```

### Exécution

Depuis l'IDE, lancer la classe `battleSystem.Main`.

Ou en ligne de commande (adapter les chemins) :

```bash
javac -d bin $(find src -name "*.java")
cp -r assets bin/        # les ressources doivent être sur le classpath
java -cp bin battleSystem.Main
```

---

## Comment jouer

1. C'est au héros actif (celui qui « flotte ») de jouer.
2. Cliquer sur **Attaquer** pour ouvrir ses actions, ou sur **Ultime** quand il est disponible.
3. Cliquer sur un monstre (flèche jaune) pour le cibler.
4. Quand tous les héros ont joué, les monstres répliquent.
5. Victoire quand tous les monstres sont tombés ; défaite si toute l'équipe est KO.

>  Survolez un personnage pour afficher son nom, ses PV et le décompte de son ultime.

### Décompte des ultimes

| Héros | Tours avant l'ultime |
|---|---|
| Bob | 1 |
| Tching | 5 |
| Flamme | 4 |

---

## ➕ Ajouter un nouveau héros avec un ultime

1. Créer `MonHeros extends Entity implements UltimateCapable` (dans `Entity/`).
2. Créer `UltimateMonHeros extends Ultimate` (dans `battleSystem/ultimate/`) avec `execute(...)`, `getName()` et les deux fabriques d'animations.
3. Créer `MonHerosBackgroundAnimation` et `MonHerosUltimateAnimation` (dans `battleSystem/animation/`).
4. L'ajouter à la liste des héros dans `Main`.

Le `BattleSystem` n'a pas besoin d'être modifié pour l'ultime lui-même.

---

##  Boss adaptatif & entraînement IA

Le boss **Card** change de forme (Neutre, Anti-Provocation, Anti-Brûlure, Anti-Boost) selon les habitudes du joueur. Le choix est délégué à un `FormSelector` :

- `RuleBasedFormSelector` : heuristique jouable immédiatement.
- `MLFormSelector` : point d'extension pour un modèle entraîné.

Chaque tour peut être consigné dans `battle_training_dataset.csv` via `BattleTrainingLogger` (PV, stades ATK/DEF, dégâts sur 3 tours, nombre de provocations / brûlures / boosts sur 5 tours, forme courante, forme choisie, résultat du combat).



---

##  Pistes d'amélioration

- [ ] Brancher l'animation d'ultime de Card côté tour des monstres
- [ ] Ultime de zone pour Card (faire transiter la liste des héros)
- [ ] Écrire les logs d'entraînement en fin de combat uniquement (résultat connu)
- [ ] Entraîner et intégrer `MLFormSelector`
- [ ] Plus de héros, de monstres et d'ultimes
- [ ] Redimensionnement dynamique de l'interface (cf. prototype `zoneDetest`)

---

