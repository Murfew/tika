# IFT3913 – Tâche 2 (HURLEY / MUSAPHUR)

> Ce fichier documente la tâche 2. Le README original d'Apache Tika se trouve plus bas, après la section « README original d'Apache Tika ».

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

---

# README original d'Apache Tika

Welcome to Apache Tika  <https://tika.apache.org/>
=================================================

[![license](https://img.shields.io/github/license/apache/tika.svg?maxAge=2592000)](http://www.apache.org/licenses/LICENSE-2.0)
[![Jenkins](https://img.shields.io/jenkins/s/https/ci-builds.apache.org/job/Tika/job/tika-main-jdk17.svg?maxAge=3600)](https://ci-builds.apache.org/job/Tika/job/tika-main-jdk17/)
[![Jenkins tests](https://img.shields.io/jenkins/t/https/ci-builds.apache.org/job/Tika/job/tika-main-jdk17.svg?maxAge=3600)](https://ci-builds.apache.org/job/Tika/job/tika-main-jdk17/lastBuild/testReport/)
[![Maven Central](https://img.shields.io/maven-central/v/org.apache.tika/tika.svg?maxAge=86400)](http://search.maven.org/#search|ga|1|g%3A%22org.apache.tika%22)

Apache Tika(TM) is a toolkit for detecting and extracting metadata and structured text content from various documents using existing parser libraries.

Tika is a project of the [Apache Software Foundation](https://www.apache.org).

Apache Tika, Tika, Apache, the Apache feather logo, and the Apache Tika project logo are trademarks of The Apache Software Foundation.

Quick Start
===========

**Parse a file in Java:**

```java
import org.apache.tika.Tika;

Tika tika = new Tika();
String text = tika.parseToString(new File("document.pdf"));
System.out.println(text);
```

**From the command line:**

```bash
java -jar tika-app-*.jar --text document.pdf
```

**Maven dependency:**

```xml
<dependency>
    <groupId>org.apache.tika</groupId>
    <artifactId>tika-parsers-standard-package</artifactId>
    <version>4.x.y</version>
    <type>pom</type>
</dependency>
```

Getting Started
===============
Pre-built binaries of Apache Tika standalone applications are available
from https://tika.apache.org/download.html . Pre-built binaries of all the
Tika jars can be fetched from Maven Central or your favourite Maven mirror.

**Tika 2.X and support for Java 8 reached End of Life (EOL) in April, 2025. 
See [Tika Roadmap 2.x, 3.x and beyond](https://cwiki.apache.org/confluence/display/TIKA/Tika+Roadmap+--+2.x%2C+3.x+and+Beyond).** 

Tika is based on **Java 17** and uses the [Maven 3](https://maven.apache.org) build system.
**N.B.** [Docker](https://www.docker.com/products/personal) is used for tests in tika-integration-tests. If Docker is not installed, those tests are skipped.

To build Tika from source, use the following command in the main directory:

    ./mvnw clean install

The Maven wrapper (`mvnw`) is included in the repository and will automatically download
the correct Maven version if needed. On Windows, use `mvnw.cmd` instead.

The build consists of a number of components, including a standalone runnable jar that you can use to try out Tika features. You can run it like this:

    java -jar tika-app/target/tika-app-*.jar --help


To build a specific project (for example, tika-server-standard):

    ./mvnw clean install -am -pl :tika-server-standard

If the ossindex-maven-plugin is causing the build to fail because a dependency
has now been discovered to have a vulnerability:

    ./mvnw clean install -Dossindex.skip


Faster Builds
=============

**Fast profile** - Use `-Pfast` to skip tests, checkstyle, and spotless:

    ./mvnw clean install -Pfast

**Parallel builds** - Add `-T1C` to build with 1 thread per CPU core:

    ./mvnw clean install -Pfast -T1C

**Maven Daemon (mvnd)** - Keeps a warm JVM running for 2-3x faster rebuilds:

```bash
# Install: https://github.com/apache/maven-mvnd
# macOS: brew install mvndaemon/tap/mvnd

# Use exactly like mvn
mvnd clean install -Pfast
mvnd test -pl :tika-core
```

**Combine both** for maximum speed during development:

    mvnd clean install -Pfast -T1C


Reproducible Builds
===================

Apache Tika supports [reproducible builds](https://reproducible-builds.org/). This means
that building the same source code with the same JDK version should produce
byte-for-byte identical artifacts, regardless of the build machine or time.

Key configuration:
- `project.build.outputTimestamp` is set in `tika-parent/pom.xml`
- All Maven plugins are configured to produce deterministic output

To verify the build plan supports reproducibility:

    ./mvnw artifact:check-buildplan

To verify two builds produce identical artifacts:

    ./mvnw clean install -DskipTests
    mv ~/.m2/repository/org/apache/tika tika-build-1
    ./mvnw clean install -DskipTests
    diff -r tika-build-1 ~/.m2/repository/org/apache/tika


Maven Dependencies
==================

Apache Tika provides *Bill of Material* (BOM) artifact to align Tika module versions and simplify version management. 
To avoid convergence errors in your own project, import this
bom or Tika's parent pom.xml in your dependency management section.

If you use Apache Maven:

```xml
<project>
  <dependencyManagement>
    <dependencies>
      <dependency>
       <groupId>org.apache.tika</groupId>
       <artifactId>tika-bom</artifactId>
       <version>4.x.y</version>
       <type>pom</type>
       <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <dependencies>
    <dependency>
      <groupId>org.apache.tika</groupId>
      <artifactId>tika-parsers-standard-package</artifactId>
      <type>pom</type>
      <!-- version not required since BOM included -->
    </dependency>
  </dependencies>
</project>
```

For Gradle:

```kotlin
dependencies {
  implementation(platform("org.apache.tika:tika-bom:4.x.y"))

  // version not required since bom (platform in Gradle terms)
  implementation("org.apache.tika:tika-parsers-standard-package@pom")
}
```

Migrating to 4.x
================
TBD

Contributing
============
See [CONTRIBUTING.md](CONTRIBUTING.md) and https://tika.apache.org/contribute.html

[![contributors](https://contributors-img.web.app/image?repo=apache/tika)](https://github.com/apache/tika/graphs/contributors)

Building from a Specific Tag
============================
Let's assume that you want to build the 3.0.1 tag:
```
0. Download and install hub.github.com
1. git clone https://github.com/apache/tika.git
2. cd tika
3. git checkout 3.0.1
4. ./mvnw clean install
```

If a new vulnerability has been discovered between the date of the
tag and the date you are building the tag, you may need to build with:

```
4. ./mvnw clean install -Dossindex.skip
```

If a local test is not working in your environment, please notify
 the project at dev@tika.apache.org. As an immediate workaround,
 you can turn off individual tests with e.g.:

```
4. ./mvnw clean install -Dossindex.skip -Dtest=\!UnpackerResourceTest#testPDFImages
```

License (see also LICENSE.txt)
==============================

Collective work: Copyright 2011 The Apache Software Foundation.

Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements.  See the NOTICE file distributed with this work for additional information regarding copyright ownership.  The ASF licenses this file to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at

<https://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.

Apache Tika includes a number of subcomponents with separate copyright notices and license terms. Your use of these subcomponents is subject to the terms and conditions of the licenses listed in the LICENSE.txt file.

Export Control
==============

This distribution includes cryptographic software.  The country in which you currently reside may have restrictions on the import, possession, use, and/or re-export to another country, of encryption software.  BEFORE using any encryption software, please  check your country's laws, regulations and policies concerning the import, possession, or use, and re-export of encryption software, to  see if this is permitted.  See <http://www.wassenaar.org/> for more information.

The U.S. Government Department of Commerce, Bureau of Industry and Security (BIS), has classified this software as Export Commodity Control Number (ECCN) 5D002.C.1, which includes information security software using or performing cryptographic functions with asymmetric algorithms.  The form and manner of this Apache Software Foundation distribution makes it eligible for export under the License Exception ENC Technology Software Unrestricted (TSU) exception (see the BIS Export Administration Regulations, Section 740.13) for both object code and source code.

The following provides more details on the included cryptographic software:

Apache Tika uses the Bouncy Castle generic encryption libraries for extracting text content and metadata from encrypted PDF files.  See <http://www.bouncycastle.org/> for more details on Bouncy Castle.  

Mailing Lists
=============

* user@tika.apache.org - About using Tika
* dev@tika.apache.org - About developing Tika

Subscribe by sending a message to `{list}-subscribe@tika.apache.org`.

Issue Tracker
=============

https://issues.apache.org/jira/browse/TIKA

Security
========

See [SECURITY.md](SECURITY.md) and https://tika.apache.org/security.html
