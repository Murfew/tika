# IFT3913 – Tâche 2 (HURLEY / MUSAPHUR)

## Étape 1 – Choix des classes

**Méthode.** Nous avons exécuté les tests existants de `tika-core` avec JaCoCo (couverture), puis PIT 1.30.0 sur les classes candidates avec ces seuls tests (analyse de mutation initiale). Un mutant est dit « vivant » quand aucun test existant ne le tue : il est soit *survivant* (le code est exécuté, mais aucun test ne détecte le changement), soit *sans couverture* (aucun test n'exécute la ligne).

**Classe retenue :** `org.apache.tika.io.EndianUtils` (module `tika-core`). Le sujet autorise entre une et trois classes. Nous n'en gardons qu'une, car la génération de tests avec un modèle exécuté localement sur CPU est lente (environ 1 min 30 à 3 min par requête, sans GPU) : `EndianUtils` offre déjà largement assez de matière.

| Classe | Lignes couvertes | Branches couvertes | Mutants générés | Tués | Survivants | Sans couverture | Vivants au total | Score initial |
|---|---|---|---|---|---|---|---|---|
| `EndianUtils` (retenue) | 31/121 (26 %) | 10/28 (36 %) | 206 | 38 | 14 | 154 | 168 | 18 % |
| `FilenameUtils` (écartée, voir ci-dessous) | 154/175 (88 %) | 86/110 (78 %) | 115 | 79 | 20 | 16 | 36 | 69 % |
| `CharsetUtils` (écartée) | 62/81 (77 %) | 25/32 (78 %) | 25 | 20 | 0 | 5 | 5 | 80 % |

`EndianUtils` a déjà des tests (4), mais n'est couverte qu'à 26 % des lignes. `FilenameUtils` (10 tests) répondait aussi au critère, mais n'a pas été retenue.

### `EndianUtils` : 168 mutants vivants
- Seules 4 méthodes sur 32 sont exercées. Aucun test ne couvre `readIntLE/BE`, `readLongLE/BE`, `readShortLE/BE`, `readUShortLE/BE`, ni les méthodes sur tableaux d'octets `getIntLE/BE`, `getLongLE`, `getShortLE/BE`, `getUShortLE/BE`, `getUIntLE/BE`, `getUByte`, `ubyteToInt`. Ces méthodes concentrent les 154 mutants sans couverture.
- Des mutants survivent dans des méthodes pourtant appelées par un test : `readUIntBE` (4), `readUIntLE` (3), `readUE7` (4) et `readIntME` (3). Les tests actuels les exécutent sans vérifier assez de valeurs (frontières, signe, octets de poids fort).
- La classe est surtout faite d'opérations sur les bits (décalages, masques, additions), qui produisent beaucoup de mutants arithmétiques (`MathMutator`) et de frontières (`ConditionalsBoundaryMutator`).

### `FilenameUtils` : 36 mutants vivants (classe écartée par manque de temps de génération)
- Vivants dans `getEmbeddedName` (8) et `getEmbeddedPath` (8), `getSanitizedEmbeddedFileName` (5) et `getSanitizedEmbeddedFilePath` (7), `getPrefixLength` (2), `resolveWithin` (3), `getSuffixFromPath` (1), `calculateExtension` (1) et `lookupExtension` (1).
- Ces méthodes traitent des chemins de fichiers imbriqués dans des documents : leurs branches non couvertes (par exemple 5 branches sur 8 pour `resolveWithin`) correspondent à des mutants que de nouveaux tests peuvent viser.

### Classe écartée : `CharsetUtils`
Elle n'a que 25 mutants, dont 5 seulement sont vivants (score initial de 80 %), et aucun mutant survivant. Elle offrirait trop peu de matière pour comparer les tests générés aux tests écrits à la main.

## Étape 2 – Installation de ChatUniTest et génération des tests

**Configuration (`tika-core/pom.xml`).** Plugin `io.github.zju-aces-ise:chatunitest-maven-plugin` 2.1.1, relié à un modèle ouvert exécuté localement avec Ollama 0.35.0 (`http://localhost:11434/v1/chat/completions`) : `qwen2.5-coder:7b` (CPU seulement : 8 cœurs, 15 Go de RAM, pas de GPU dédié). Commandes utilisées : `./mvnw -pl tika-core io.github.zju-aces-ise:chatunitest-maven-plugin:2.1.1:class -DselectClass=EndianUtils` (premier essai, arrêté après 5 méthodes par des délais d'attente dépassés), puis `...:method -DselectMethod=EndianUtils#<méthode>` pour chacune des 18 méthodes restantes, une à la fois.

Quatre ajustements ont été nécessaires :
1. **Alias du modèle.** Le plugin n'accepte qu'une liste fermée de noms (`gpt-*`, `code-llama`, `codeqwen:v1.5-chat`). Nous avons créé l'alias Ollama `codeqwen:v1.5-chat` qui pointe vers `qwen2.5-coder:7b` (`ollama cp qwen2.5-coder:7b codeqwen:v1.5-chat`). Le modèle réellement utilisé est donc Qwen2.5-Coder 7B.
2. **Mockito.** Les tests générés utilisent Mockito, absent de `tika-core` : `mockito-core` et `mockito-junit-jupiter` ajoutés en portée `test`.
3. **Prompts modifiés** (`tika-core/chatunitest-prompts/`, option `promptPath`). Le plugin ne donne pas au modèle la classe imbriquée `EndianUtils.BufferUnderrunException`, et le modèle l'importait depuis un paquet inexistant dans les 3 rounds de réparation. Nous avons ajouté à `initial_system.ftl` et `repair.ftl` deux phrases : (a) `BufferUnderrunException` est une classe imbriquée de `EndianUtils`, à écrire `EndianUtils.BufferUnderrunException` ; (b) chaque méthode de test doit déclarer `throws Exception`. **Ce sont des indications que nous avons données au modèle** ; sans elles, aucun test ne compilait.
4. **Réglages.** `testNumber=1`, `maxRounds=3`, un seul fil d'exécution. Une génération sur une seule méthode prenait de 4 à 23 minutes sur ce portable (environ 13 tokens/s en lecture de prompt) ; la génération des 18 dernières méthodes a duré 1 h 19.

## Étape 3 – Tests générés : localisation, compilation, corrections

**Où sont les tests ?** Les 25 fichiers tels que ChatUniTest les a produits, **non modifiés**, sont dans `tika-core/chatunitest-tests/org/apache/tika/io/`. Les mêmes fichiers, après nos corrections, sont dans `tika-core/src/test/java/org/apache/tika/io/` (noms `EndianUtils_<méthode>_<n>_0_Test.java`) : ce sont ceux que Maven exécute.

**Résultats bruts (avant toute correction)**
| | |
|---|---|
| Méthodes de `EndianUtils` ciblées | 23 noms de méthodes publiques (31 en comptant les surcharges) |
| Méthodes avec au moins un test qui compile | 20 (25 fichiers, 97 tests ; certaines méthodes ont des variantes surchargées) |
| Méthodes sans aucun test compilable (3 rounds de réparation épuisés) | 3 : `readIntLE`, `readLongLE`, `readUE7` |
| Tests qui passent tels quels | **70 / 97 (72 %)** |
| Tests qui échouent | **27 / 97 (28 %)**, répartis dans 14 fichiers sur 25 |
| Fichiers dont tous les tests passent tels quels | 11 / 25 |
| Corrections manuelles nécessaires | **27 tests corrigés (oracles)** ; 0 erreur de compilation ; mise en conformité du style des 25 fichiers (voir ci-dessous) |

Les tests générés **compilent sans intervention** (dans notre exécution, ChatUniTest n'a écrit de fichier que pour les tests qui compilaient), mais **27 tests ne passent pas** sans correction. Le message « compile and execute successfully » du plugin est trompeur : 12 des 14 fichiers en échec provenaient de méthodes pour lesquelles le plugin affichait ce message (les 2 autres : « generated successfully »). Après correction, les **97 tests passent** (`./mvnw -pl tika-core test -Dtest='EndianUtils_*'`).

**Conformité au style du projet (25 fichiers).** Les tests générés utilisaient des imports avec `*` (`org.mockito.*`, `org.junit.jupiter.api.*`, `Assertions.*`, `Mockito.*`), interdits par la configuration checkstyle de Tika (100 violations `AvoidStarImport`), ce qui aurait fait échouer le build et la GitHub Action. Nous les avons remplacés par des imports explicites, sans toucher au corps des tests. Par ailleurs, le build de Tika exécute automatiquement `spotless:apply`, qui a ajouté l'en-tête de licence Apache, trié les imports et retiré les imports inutilisés. Vérification sans désactiver aucune règle : `./mvnw -pl tika-core test -Dtest='EndianUtils*'` → 0 violation checkstyle, 101 tests réussis (97 générés + 4 d'origine) ; `./mvnw -pl tika-core verify -DskipTests` → 0 erreur forbiddenapis.

**Nature des 27 erreurs**
- **11 valeurs attendues fausses** : le modèle se trompe d'octets ou d'ordre (gros/petit-boutiste), ou lit trop peu d'octets (ex. `getIntBE` à l'offset 4 : attendu `0x55`, valeur réelle `0x22334455`).
- **10 mauvaises exceptions sur les méthodes `get*`** : le modèle suppose une `BufferUnderrunException` ; le code laisse remonter `ArrayIndexOutOfBoundsException`.
- **3 mauvaises exceptions sur les méthodes `read*`** : le modèle attend `IOException`, mais `BufferUnderrunException` étend `TikaException`.
- **2 appels par réflexion inutiles** (`readUShortBE`) : l'exception est enveloppée dans `InvocationTargetException`.
- **1 incompréhension de `InputStream.read()`** : l'octet `-1` du tableau est lu comme 255, ce n'est pas une fin de flux.

**Méthode de correction.** Chaque valeur attendue a été recalculée à la main à partir de la formule du code source (colonne « Pourquoi ») avant de lancer le test ; nous n'avons pas recopié la valeur observée. Quand le test supposait un comportement qui n'existe pas dans le code (exception inexistante), nous l'avons réorienté vers le comportement réel plutôt que de le supprimer, afin de garder le cas limite testé.

### Journal des 27 corrections

| # | Fichier / test | Échec constaté | Correction | Pourquoi |
|---|---|---|---|---|
| 1 | `EndianUtils_getIntBE_22_0_Test`<br>`testGetIntBE_withTooSmallData` | Aucune exception (le tableau de 4 octets suffit) | Tableau réduit à 3 octets et attend `ArrayIndexOutOfBoundsException` | `getIntBE` lit 4 octets : avec 3 octets, `data[3]` est hors limites. Le code lit `data[offset+i]` sans vérification : un tableau trop court ou un offset invalide lève `ArrayIndexOutOfBoundsException`, jamais `BufferUnderrunException` (réservée aux méthodes `read*` sur un flux). |
| 2 | `EndianUtils_getIntBE_22_0_Test`<br>`testGetIntBE_withEmptyData` | Exception attendue : `BufferUnderrunException`, levée : `ArrayIndexOutOfBoundsException` | Attend `ArrayIndexOutOfBoundsException` | Le code lit `data[offset+i]` sans vérification : un tableau trop court ou un offset invalide lève `ArrayIndexOutOfBoundsException`, jamais `BufferUnderrunException` (réservée aux méthodes `read*` sur un flux). |
| 3 | `EndianUtils_getIntBE_22_0_Test`<br>`testGetIntBE_withNegativeOffset` | Exception attendue : `BufferUnderrunException`, levée : `ArrayIndexOutOfBoundsException` | Attend `ArrayIndexOutOfBoundsException` | Le code lit `data[offset+i]` sans vérification : un tableau trop court ou un offset invalide lève `ArrayIndexOutOfBoundsException`, jamais `BufferUnderrunException` (réservée aux méthodes `read*` sur un flux). |
| 4 | `EndianUtils_getIntBE_22_0_Test`<br>`testGetIntBE_withOffsetExceedingDataLength` | Exception attendue : `BufferUnderrunException`, levée : `ArrayIndexOutOfBoundsException` | Attend `ArrayIndexOutOfBoundsException` | Le code lit `data[offset+i]` sans vérification : un tableau trop court ou un offset invalide lève `ArrayIndexOutOfBoundsException`, jamais `BufferUnderrunException` (réservée aux méthodes `read*` sur un flux). |
| 5 | `EndianUtils_getIntBE_22_0_Test`<br>`testGetIntBE_withValidData` | Attendu 0x11223344, obtenu 0x11 (17) | Données changées pour que les 4 premiers octets soient 11 22 33 44 | `getIntBE` = (b0<<24)+(b1<<16)+(b2<<8)+b3 sur les octets 0 à 3. Avec 00 00 00 11 on obtient 0x11 : le modèle avait confondu l'ordre des octets et les données. |
| 6 | `EndianUtils_getIntBE_22_0_Test`<br>`testGetIntBE_withOffset` | Attendu 0x55 (85), obtenu 0x22334455 | Valeur attendue : 0x22334455 | À l'offset 4, la méthode lit 4 octets (indices 4 à 7 : 22 33 44 55), pas un seul. 0x22<<24 + 0x33<<16 + 0x44<<8 + 0x55 = 0x22334455. |
| 7 | `EndianUtils_getIntLE_20_0_Test`<br>`testGetIntLE_withNegativeOffset` | Exception attendue : `BufferUnderrunException`, levée : `ArrayIndexOutOfBoundsException` ; assertion sur le message `Buffer underrun` fausse aussi | Attend `ArrayIndexOutOfBoundsException` ; assertion sur le message supprimée | Le code lit `data[offset+i]` sans vérification : un tableau trop court ou un offset invalide lève `ArrayIndexOutOfBoundsException`, jamais `BufferUnderrunException` (réservée aux méthodes `read*` sur un flux). Le message est celui du JDK, on ne l'asserte pas. |
| 8 | `EndianUtils_getIntLE_20_0_Test`<br>`testGetIntLE_withEmptyData` | Exception attendue : `BufferUnderrunException`, levée : `ArrayIndexOutOfBoundsException` ; assertion sur le message `Buffer underrun` fausse aussi | Attend `ArrayIndexOutOfBoundsException` ; assertion sur le message supprimée | Le code lit `data[offset+i]` sans vérification : un tableau trop court ou un offset invalide lève `ArrayIndexOutOfBoundsException`, jamais `BufferUnderrunException` (réservée aux méthodes `read*` sur un flux). Le message est celui du JDK, on ne l'asserte pas. |
| 9 | `EndianUtils_getIntLE_20_0_Test`<br>`testGetIntLE_withInsufficientData` | Exception attendue : `BufferUnderrunException`, levée : `ArrayIndexOutOfBoundsException` ; assertion sur le message `Buffer underrun` fausse aussi | Attend `ArrayIndexOutOfBoundsException` ; assertion sur le message supprimée | Le code lit `data[offset+i]` sans vérification : un tableau trop court ou un offset invalide lève `ArrayIndexOutOfBoundsException`, jamais `BufferUnderrunException` (réservée aux méthodes `read*` sur un flux). Le message est celui du JDK, on ne l'asserte pas. |
| 10 | `EndianUtils_getIntLE_21_0_Test`<br>`testGetIntLEWithOffset` | Attendu 0x08070605, obtenu 0x06050403 | Valeur attendue : 0x06050403 | À l'offset 2, `getIntLE` lit les indices 2 à 5 (03 04 05 06) en petit-boutiste : (b3<<24)+(b2<<16)+(b1<<8)+b0 = 0x06050403. |
| 11 | `EndianUtils_getShortBE_16_0_Test`<br>`testGetShortBE_withOffsetAndSmallData` | Exception attendue : `BufferUnderrunException`, levée : `ArrayIndexOutOfBoundsException` | Attend `ArrayIndexOutOfBoundsException` | `getShortBE(data, offset)` appelle `getUShortBE`, qui lit `data[offset]` et `data[offset+1]` sans vérification : `ArrayIndexOutOfBoundsException`. |
| 12 | `EndianUtils_getShortBE_16_0_Test`<br>`testGetShortBE_withOffsetAndEmptyData` | Exception attendue : `BufferUnderrunException`, levée : `ArrayIndexOutOfBoundsException` | Attend `ArrayIndexOutOfBoundsException` | `getShortBE(data, offset)` appelle `getUShortBE`, qui lit `data[offset]` et `data[offset+1]` sans vérification : `ArrayIndexOutOfBoundsException`. |
| 13 | `EndianUtils_getShortBE_16_0_Test`<br>`testGetShortBE_withEmptyData` | Exception attendue : `BufferUnderrunException`, levée : `ArrayIndexOutOfBoundsException` | Attend `ArrayIndexOutOfBoundsException` | `getShortBE(data, offset)` appelle `getUShortBE`, qui lit `data[offset]` et `data[offset+1]` sans vérification : `ArrayIndexOutOfBoundsException`. |
| 14 | `EndianUtils_getUIntBE_26_0_Test`<br>`testGetUIntBE` | Attendu 1, obtenu 0 | Le 1 est placé dans le 4e octet (indice 3) au lieu du 8e | `getUIntBE(data)` ne lit que les octets 0 à 3 (offset 0). Le 1 placé à l'indice 7 était ignoré ; avec 00 00 00 01 le résultat est bien 1. |
| 15 | `EndianUtils_getUIntLE_25_0_Test`<br>`testGetUIntLEWithEdgeCase` | Attendu 0x01000000 (16777216), obtenu 1 | Valeur attendue : 1L | Petit-boutiste : l'octet 0 (01) est le moins significatif. (b3<<24)+(b2<<16)+(b1<<8)+b0 = 1. |
| 16 | `EndianUtils_getUIntLE_25_0_Test`<br>`testGetUIntLEWithNegativeNumber` | Attendu 0xFCFDFFFF, obtenu 0xFCFDFEFF (4244504319) | Valeur attendue : 0xFCFDFEFFL | Octets FF FE FD FC en petit-boutiste : (0xFC<<24)+(0xFD<<16)+(0xFE<<8)+0xFF = 0xFCFDFEFF. Le modèle avait oublié l'octet FE. |
| 17 | `EndianUtils_getUIntLE_25_0_Test`<br>`testGetUIntLEWithLargeNumber` | Attendu 0x80000000 (2147483648), obtenu 0 | Le 0x80 est placé à l'indice 3 au lieu de 7 | `getUIntLE(data, 0)` ne lit que les indices 0 à 3. Avec 80 comme 4e octet : 0x80<<24 = 0x80000000 (non signé grâce au masque `& 0xFFFFFFFFL`). |
| 18 | `EndianUtils_getUShortBE_18_0_Test`<br>`testGetUShortBEWithOffset` | Attendu 514, obtenu 515 | Valeur attendue : 515 | À l'offset 1 : octets 02 03. Gros-boutiste : (0x02<<8)+0x03 = 515. |
| 19 | `EndianUtils_getUShortLE_15_0_Test`<br>`testGetUShortLE` | Attendu 257, obtenu 513 | Valeur attendue : 513 | À l'offset 1 : octets 01 02. Petit-boutiste : (0x02<<8)+0x01 = 513. |
| 20 | `EndianUtils_getUShortLE_15_0_Test`<br>`testGetUShortLEWithOffset` | Attendu 772, obtenu 1027 | Valeur attendue : 1027 | À l'offset 3 : octets 03 04. Petit-boutiste : (0x04<<8)+0x03 = 1027. |
| 21 | `EndianUtils_readIntBE_7_0_Test`<br>`testReadIntBEIOException` | Exception attendue : `IOException`, levée : `BufferUnderrunException` | Attend `BufferUnderrunException` | Un flux trop court lève `EndianUtils.BufferUnderrunException`, qui étend `TikaException` et non `IOException`. |
| 22 | `EndianUtils_readIntME_8_0_Test`<br>`testReadIntME_IOException` | Exception attendue : `IOException`, levée : `BufferUnderrunException` | Attend `BufferUnderrunException` | Un flux trop court lève `EndianUtils.BufferUnderrunException`, qui étend `TikaException` et non `IOException`. |
| 23 | `EndianUtils_readLongBE_10_0_Test`<br>`testReadLongBEWithIOException` | Exception attendue : `IOException`, levée : `BufferUnderrunException` | Attend `BufferUnderrunException` | Un flux trop court lève `EndianUtils.BufferUnderrunException`, qui étend `TikaException` et non `IOException`. |
| 24 | `EndianUtils_readUIntLE_4_0_Test`<br>`testReadUIntLEWithMixedValues` | Attendu 0x030201FF, obtenu 0x0302FF01 (50528001) | Valeur attendue : 0x0302FF01L | Octets 01 FF 02 03 lus dans l'ordre : ch1=01, ch2=FF, ch3=02, ch4=03. (ch4<<24)+(ch3<<16)+(ch2<<8)+ch1 = 0x0302FF01. |
| 25 | `EndianUtils_readUShortBE_3_0_Test`<br>`testReadUShortBEWithEmptyStream` | Exception attendue : `IOException`, levée : `InvocationTargetException` (réflexion) | Appel direct, attend `BufferUnderrunException` ; test renommé | Flux vide : `read()` renvoie -1 donc `BufferUnderrunException`. Appel par réflexion : l'exception réelle est enveloppée dans `InvocationTargetException`. Or `readUShortBE` est publique et statique : appel direct. |
| 26 | `EndianUtils_readUShortBE_3_0_Test`<br>`testReadUShortBEWithBufferUnderrun` | `InvocationTargetException` au lieu de `BufferUnderrunException` (réflexion) | Appel direct de `readUShortBE` | Un seul octet dans le flux : le 2e `read()` renvoie -1. Appel par réflexion : l'exception réelle est enveloppée dans `InvocationTargetException`. Or `readUShortBE` est publique et statique : appel direct. |
| 27 | `EndianUtils_readUShortLE_2_0_Test`<br>`testReadUShortLE_withNegativeByte` | Exception attendue, aucune levée | Attend la valeur 255 | `InputStream.read()` renvoie un octet non signé (0 à 255) : l'octet -1 est lu comme 255, pas comme fin de flux (-1 réel). (ch2<<8)+ch1 = (0<<8)+255 = 255. |

## Étapes 5 à 8 – Analyse de mutation avec PIT

**Configuration.** `pitest-maven` 1.30.0 et `pitest-junit5-plugin` 1.2.3 dans `tika-core/pom.xml`, mutateurs par défaut, `targetClasses` = `org.apache.tika.io.EndianUtils`, `targetTests` = `org.apache.tika.io.EndianUtils*Test` (tests d'origine `EndianUtilsTest` et tests générés `EndianUtils_*_Test`).

- Avant (tests d'origine seulement) : `./mvnw -pl tika-core test-compile org.pitest:pitest-maven:mutationCoverage -DexcludedTestClasses='org.apache.tika.io.EndianUtils_*' -DreportsDirectory=$PWD/tika-core/target/pit-avant`
- Après (tests d'origine et tests générés corrigés) : `./mvnw -pl tika-core test-compile org.pitest:pitest-maven:mutationCoverage -DreportsDirectory=$PWD/tika-core/target/pit-apres`

Les rapports HTML complets sont archivés dans [`rapports-pit/avant/`](rapports-pit/avant/index.html) et [`rapports-pit/apres/`](rapports-pit/apres/index.html). GitHub affiche le code source des fichiers HTML : pour voir les rapports mis en forme, cloner le dépôt et ouvrir `index.html` dans un navigateur.

### Scores

| | Mutants | Tués | Survivants | Sans couverture | **Score de mutation** | Couverture des lignes |
|---|---|---|---|---|---|---|
| Tests d'origine (4 tests) | 206 | 38 | 14 | 154 | **18 %** | 31/121 (26 %) |
| + tests générés (101 tests) | 206 | 140 | 28 | 38 | **68 %** | 100/121 (83 %) |

**Les tests générés ne détectent pas tous les mutants.** Ils tuent 102 nouveaux mutants, qui étaient tous *sans couverture* auparavant. En revanche, ils ne tuent **aucun** des 14 mutants qui survivaient déjà aux tests d'origine, et ils font apparaître 14 nouveaux survivants (du code désormais exécuté, mais dont le changement n'est pas vérifié). Aucun mutant tué auparavant n'est perdu.

| Mutateur | Total | Tués avant | Tués après |
|---|---|---|---|
| `MathMutator` (décalages, additions, `&`/`\|`) | 139 | 27 | 94 |
| `PrimitiveReturnsMutator` (retour remplacé par 0) | 31 | 4 | 27 |
| `NegateConditionalsMutator` | 14 | 7 | 12 |
| `ConditionalsBoundaryMutator` (`<` devient `<=`) | 13 | 0 | 1 |
| `IncrementsMutator` (`i++` devient `i--`) | 9 | 0 | 6 |

### Mutants détectés par les tests générés, et pourquoi

Le facteur commun : **les tests générés comparent la valeur exacte reconstituée à partir d'octets tous différents**. `EndianUtils` assemble des entiers par masques, décalages et additions ; toute altération de cet assemblage change le résultat, et une assertion `assertEquals` sur la valeur exacte le détecte.

- **`MathMutator` (67 nouveaux).** Exemple : dans `getIntBE`, `data[i++] & 0xFF` devient `data[i++] | 0xFF` (ligne 386) ; l'octet lu vaut alors `0xFF` (ou `-1` si l'octet est négatif), quelle que soit la donnée. Il est tué par `EndianUtils_getIntBE_23_0_Test#testGetIntBEWithNegativeNumber`, qui attend une valeur précise. De même, remplacer `b0 << 24` par `b0 >> 24` ou un `+` par un `-` produit un autre entier. À l'inverse, un test dont tous les octets valent `00` ne détecterait pas la plupart de ces mutants, puisque décaler ou additionner des zéros donne toujours zéro.
- **`PrimitiveReturnsMutator` (23 nouveaux).** `return …;` devient `return 0;`. Ce mutant est tué dès qu'un test attend une valeur non nulle, par exemple `EndianUtils_getIntBE_22_0_Test#testGetIntBE_withValidData` (attendu `0x11223344`). Comme presque chaque méthode `get*`/`read*` a reçu au moins un test de valeur non nulle, ces mutants sont détectés.
- **`IncrementsMutator` (6 nouveaux).** Dans `getIntBE`/`getIntLE`, `i++` devient `i--` : la méthode relit le mauvais octet (ou un indice négatif, d'où une exception). Tué par les tests de valeur sur 4 octets distincts (`EndianUtils_getIntBE_23_0_Test#testGetIntBEWithNegativeNumber`).
- **`NegateConditionalsMutator` (5 nouveaux).** La condition de fin de flux `(ch1 | ch2 | …) < 0` est inversée : la méthode lève une exception sur un flux complet et n'en lève pas sur un flux trop court. Tué par les tests de flux trop court, par exemple `EndianUtils_readIntBE_7_0_Test#testReadIntBEBufferUnderrun` (un flux de 3 octets doit lever `BufferUnderrunException`).
- **`ConditionalsBoundaryMutator` (1 nouveau).** Dans `getLongLE`, la condition de boucle `j >= offset` (ligne 446) devient `j > offset` : le premier octet n'est plus lu. Tué par `EndianUtils_getLongLE_28_0_Test#testGetLongLE`.

### Mutants encore vivants (66) : analyse

| Groupe | Nombre | Cause | Comment le tuer |
|---|---|---|---|
| `readIntLE`, `readLongLE` sans couverture | 12 + 24 | ChatUniTest n'a produit aucun test compilable pour ces méthodes (étape 3). | Tests de valeur et de flux trop court, comme pour `readIntBE`/`readLongBE`. |
| `readShortBE` : `return` remplacé par 0, sans couverture | 1 | Le seul test généré ne couvre que le cas où `read()` lève une `IOException`. | Un test de valeur sur 2 octets. |
| `getUIntLE(byte[])` : `return` remplacé par 0, sans couverture | 1 | Seule la surcharge `getUIntLE(byte[], int)` est appelée. | Appeler la surcharge à un argument. |
| Frontière `(… ) < 0` devient `<= 0` dans `readIntBE`, `readIntME`, `readLongBE`, `readUIntBE`, `readUIntLE`, `readUShortBE`, `readUShortLE` | 7 | Aucun test ne lit des octets **tous nuls** : le OU de valeurs nulles vaut 0, et seul le mutant lèverait une exception. | Un flux d'octets `00` : l'original renvoie 0, le mutant lève `BufferUnderrunException`. |
| `\|` devient `&` dans la condition de fin de flux | 15 | Un flux standard renvoie `-1` puis toujours `-1` : le dernier octet lu vaut `-1` dès qu'il manque des données, et un OU avec `-1` reste négatif. Les mutants qui ne touchent pas le **dernier** `\|` sont donc équivalents pour un `InputStream` normal. | Le dernier `\|` de `readUIntBE` se tue avec un flux de 3 octets. Il survit parce que le test **écrit à la main** `EndianUtilsTest#testReadUIntBE` appelle par erreur `readUIntLE` dans son cas « flux trop court » (copier-coller) : la fin de flux de `readUIntBE` n'a jamais été testée. Les autres ne se tuent qu'avec un flux simulé (Mockito) qui renvoie `-1` **puis** une donnée. |
| `getIntBE`/`getIntLE` : le dernier `i++` devient `i--` | 2 | La valeur de `i` après la dernière lecture n'est jamais utilisée. | **Mutants équivalents** : aucun test ne peut les tuer. |
| `readUE7` (frontières et incrément, lignes 235 et 246) | 4 | Déjà survivants avec les tests d'origine ; ChatUniTest n'a pas généré de test compilable pour `readUE7`. | Un octet `00` seul (ligne 246), `81 00` (ligne 235, `>= 0`), et une suite de 7 octets de continuation (limite `read++ < 6`). |

Ces mutants, sauf les mutants équivalents, sont la base des tests écrits à la main de l'étape 9.

## Déclaration d'utilisation de l'IA – partie de Hurley (étapes 1, 2, 3, 5 à 8)

Cette déclaration porte sur l'IA utilisée **pour faire le travail**. Elle est distincte de l'IA **étudiée** dans la tâche (ChatUniTest avec Qwen2.5-Coder, décrite aux étapes 2 et 3).

**Outil utilisé.** Claude Code (Anthropic), un assistant de programmation qui agit dans le terminal et l'éditeur : modèles Claude Sonnet 5.5, puis Claude Opus 5.5. Les commits auxquels il a contribué portent la mention `Co-Authored-By: Claude …`.

**Utilisation de l'IA.** L'assistant a servi d'outil d'exécution et de guide, sous ma direction. Je fixais l'objectif de chaque étape et je choisissais parmi les options proposées. Je vérifiais aussi le résultat avant de l'intégrer.

Il a été utilisé pour :
- exécuter les commandes (Maven, JaCoCo, PIT, ChatUniTest) et lire leurs rapports ;
- installer et configurer Ollama et le plugin ChatUniTest, puis diagnostiquer les échecs de génération (classes non compilées, Mockito absent, classe imbriquée inconnue du modèle) ;
- corriger les 27 oracles faux des tests générés et remplacer les imports en `*` (checkstyle) ;
- classer les mutants de PIT et en expliquer les causes ;
- rédiger une première version des sections de ce README, que j'ai relue et corrigée ;
- proposer la suite du travail et les options possibles à chaque étape.

**Contrôles sur le travail de l'IA.** Chaque valeur attendue corrigée à l'étape 3 a été recalculée par une réimplémentation indépendante des formules de `EndianUtils`, sans utiliser la sortie Java. Tous les chiffres de ce README proviennent des rapports de Maven, de Surefire et de PIT (archivés dans `rapports-pit/`), et non d'estimations de l'IA. Les erreurs de l'IA repérées pendant la vérification ont été corrigées avant les commits. Par exemple : une affirmation inexacte sur les verdicts du plugin, et une commande de vérification mal décrite.
