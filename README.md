# MineMastery

MineMastery est un plugin Minecraft qui ajoute un système de maîtrise pour les joueurs, leur permettant de gagner des points en minant des blocs. Ces points peuvent être utilisés pour progresser à travers différents niveaux de maîtrise, chacun offrant des récompenses uniques.

**Version actuelle** : 1.0

## Fonctionnalités

- **Système de Points**: Les joueurs gagnent des points de maîtrise chaque fois qu'ils minent un bloc.
- **Niveaux de Maîtrise**: Les joueurs peuvent progresser à travers 10 niveaux de maîtrise pour les gemmes et les métaux. Le niveau 0 est gratuit, et le coût des niveaux augmente jusqu'à 10 000 points.
- **Récompenses**: Chaque niveau offre des récompenses spécifiques qui peuvent être configurées.
- **Personnalisation**: Le nombre de points gagnés par bloc miné et les récompenses pour chaque niveau peuvent être personnalisés via le fichier de configuration.

### A venir

- **Sauvegarde des points**: Un fichier `stats.yml` permettra de sauvegarder les points des joueurs.
- **Ajout de commandes**: Des commandes qui pourraient permettre de modifier la configuration du plugin in-game seront ajoutées par la suite, telle que `/minemastery create [rank]`.
  
## Configuration

Voici un exemple de configuration par défaut :

```yaml
mastery:
  gem:
    points_per_block: 1
    level:
      0: 0
      1: 100
      2: 500
      3: 1000
      4: 2000
      5: 3000
      6: 4000
      7: 5000
      8: 6000
      9: 8000
      10: 10000
    rewards:
      1: ["give {player} dpick 1", "broadcast chat {player} a gagné 1"]
      2: ["give {player} dpick 2", "broadcast chat {player} a gagné 2"]
      3: ["give {player} dpick 3", "broadcast chat {player} a gagné 3"]
      # Ajoutez d'autres récompenses pour les niveaux supérieurs
  metal:
    points_per_block: 1
    level:
      0: 0
      1: 200
      2: 700
      3: 1500
      4: 3000
      5: 4500
      6: 6000
      7: 7500
      8: 9000
      9: 11000
      10: 13000
    rewards:
      1: ["give {player} dpick 11", "broadcast chat {player} a gagné 11"]
      2: ["give {player} dpick 22", "broadcast chat {player} a gagné 22"]
      3: ["give {player} dpick 33", "broadcast chat {player} a gagné 33"]
      # Ajoutez d'autres récompenses pour les niveaux supérieurs
```
## Commandes

- `/minemastery stats` : Affiche les statistiques de maîtrise du joueur.
- `/minemastery stats [joueur]` : Affiche les statistiques de maîtrise d'un joueur spécifique (requiert la permission minemastery.admin).
- `/minemastery top` : Affiche les meilleurs joueurs en termes de points de maîtrise.
- `/minemastery rewards` : Affiche les récompenses disponibles pour chaque niveau.
- `/minemastery setpoints [joueur] [points]` : Définit les points de maîtrise d'un joueur (requiert la permission minemastery.admin).
- `/minemastery addpoints [joueur] [points]` : Ajoute des points de maîtrise à un joueur (requiert la permission minemastery.admin).
- `/minemastery removepoints [joueur] [points]` : Retire des points de maîtrise d'un joueur (requiert la permission minemastery.admin).
- `/minemastery reload` : Recharge la configuration du plugin (requiert la permission minemastery.admin).

**Aliases**:
- `/mm`
- `/mmastery`
- `/minem`

## Dépendances

Ce plugin utilise `EZBlocks`. Veuillez vérifier son installation avant d'installer `MineMastery`.

## Installation

1. Téléchargez le fichier JAR du plugin.
2. Placez le fichier JAR dans le dossier `plugins` de votre serveur Minecraft.
3. Démarrez ou redémarrez votre serveur Minecraft.
4. Modifiez le fichier de configuration généré (`config.yml`) pour ajuster les paramètres selon vos besoins.
5. Utilisez les commandes du plugin pour gérer et visualiser les points de maîtrise des joueurs.

## Développement

Pour contribuer au développement de ce plugin :

1. Clonez ce dépôt : `git clone https://github.com/keksymc/minemastery.git`
2. Importez le projet dans votre IDE préféré.
3. Faites vos modifications et testez-les.
4. Soumettez une pull request pour examen.

## Licence

Ce projet est sous licence MIT. Consultez le fichier LICENSE pour plus de détails.

## Remerciements

Merci à tous ceux qui comptent contribuer au développement de ce plugin. Vos suggestions et améliorations sont toujours les bienvenues !
