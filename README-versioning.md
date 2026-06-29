# README-versioning.md

Guide debutant pour creer une nouvelle version d'ontologie avec `CreateNatclinnTbox` puis la publier correctement sur GitHub Pages.

## 1) Prerequis

- Windows + PowerShell
- Java 17+
- Maven
- Git
- (Optionnel) Docker pour WIDOCO

Depuis la racine du repo, verifier rapidement:

```powershell
java -version
mvn -version
git --version
```

## 2) Principe general

Le process exact du projet est:

1. Mettre a jour la version dans [src/ontologyManagement/CreateNatclinnTbox.java](src/ontologyManagement/CreateNatclinnTbox.java)
2. Regenerer `NewGeneratedOntology/ontology.owl`
3. Copier ce fichier vers `ontology.owl` a la racine
4. Regenerer la doc WIDOCO dans `ncl/`
5. Creer l'archive de version `ncl/X.Y.Z/`
6. Verifier que `index.html` existe dans `ncl/X.Y.Z/` (cree automatiquement par le script de release)
7. Mettre a jour les liens publics
8. Commit + push

## 3) Etape par etape (copier/coller)

### Etape A - Choisir la nouvelle version

Exemple dans ce guide: `1.1.0`.

### Etape B - Mettre a jour la version dans le code Java

Ouvrir [src/ontologyManagement/CreateNatclinnTbox.java](src/ontologyManagement/CreateNatclinnTbox.java) et modifier au minimum:

- `OWL.versionIRI` -> `ncl + "ncl/1.1.0"`
- `OWL.versionInfo` -> `"1.1.0"`

Exemple attendu:

```java
ont.addProperty(OWL.versionIRI, om.createResource(ncl + "ncl/1.1.0"));
ont.addProperty(OWL.versionInfo, "1.1.0");
```

### Etape C - Generer l'ontologie avec CreateNatclinnTbox

Script officiel:

```powershell
./scripts/run-create-natclinn-tbox.ps1
```

Ce script execute Maven (`clean compile exec:java`) avec la classe principale configuree dans [pom.xml](pom.xml):

- `ontologyManagement.CreateNatclinnTbox`

Sortie attendue:

- `NewGeneratedOntology/ontology.owl`

### Etape D - Copier la nouvelle ontologie pour WIDOCO

Le script WIDOCO lit par defaut `ontology.owl` a la racine. Il faut donc le remplacer:

```powershell
Copy-Item .\NewGeneratedOntology\ontology.owl .\ontology.owl -Force
```

### Etape E - Regenerer la doc WIDOCO courante dans ncl/

Script officiel:

```powershell
./scripts/generate-widoco.ps1
```

Options utiles:

```powershell
./scripts/generate-widoco.ps1 -Languages fr-en
./scripts/generate-widoco.ps1 -UseDocker
```

Sortie attendue:

- pages courantes dans `ncl/` (index-en.html, index-fr.html, ressources, provenance, etc.)

### Etape F - Archiver cette release dans ncl/X.Y.Z

Script officiel:

```powershell
./scripts/release-widoco-version.ps1 -Version 1.1.0
```

Sortie attendue:

- dossier `ncl/1.1.0/` cree avec une copie du contenu courant de `ncl/`
- fichier `ncl/1.1.0/index.html` cree automatiquement (redirection vers FR/EN)

### Etape G - IMPORTANT: verifier index.html dans ncl/X.Y.Z

Pourquoi: sans `index.html`, l'URL `https://natclinn.github.io/ncl/1.1.0` peut afficher `readme.md` au lieu de la doc.

Normalement, le script `release-widoco-version.ps1` le cree automatiquement.

Si besoin (fallback manuel), creer un index de redirection:

```powershell
@'
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>NCL 1.1.0</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <script>
    (function () {
      var lang = (navigator.language || navigator.userLanguage || "en").toLowerCase();
      var target = lang.indexOf("fr") === 0 ? "index-fr.html" : "index-en.html";
      window.location.replace(target);
    })();
  </script>
  <noscript><meta http-equiv="refresh" content="0; url=index-en.html"></noscript>
</head>
<body>Redirecting...</body>
</html>
'@ | Set-Content -Encoding UTF8 .\ncl\1.1.0\index.html
```

### Etape H - Mettre a jour les liens publics vers la nouvelle version

Verifier et mettre a jour au minimum:

- [ncl/index-fr.html](ncl/index-fr.html)
- [ncl/index-en.html](ncl/index-en.html)

Lien recommande:

- Francais: `https://natclinn.github.io/ncl/1.1.0/index-fr.html`
- Anglais: `https://natclinn.github.io/ncl/1.1.0/index-en.html`

## 4) Verification locale avant push

Lancer un serveur local:

```powershell
python -m http.server 8000
```

Puis tester:

- http://localhost:8000/ncl/index-fr.html
- http://localhost:8000/ncl/1.1.0
- http://localhost:8000/ncl/1.1.0/index-fr.html
- http://localhost:8000/ncl/1.1.0/index-en.html

## 5) Publication GitHub Pages

```powershell
git status
git add ontology.owl ncl scripts README-versioning.md src/ontologyManagement/CreateNatclinnTbox.java
git commit -m "Release ontology 1.1.0"
git push
```

Attendre 1 a 3 minutes, puis tester:

- https://natclinn.github.io
- https://natclinn.github.io/ncl/1.1.0

## 6) Checklist rapide

- [ ] `versionIRI` et `versionInfo` mis a jour
- [ ] `NewGeneratedOntology/ontology.owl` regenere
- [ ] `ontology.owl` racine remplace
- [ ] `ncl/` regenere avec WIDOCO
- [ ] `ncl/X.Y.Z/` cree via script
- [ ] `ncl/X.Y.Z/index.html` ajoute
- [ ] Liens dans `ncl/index-fr.html` et `ncl/index-en.html` mis a jour
- [ ] Test local OK
- [ ] Push fait

## 7) Depannage rapide

- Si `https://.../ncl/X.Y.Z` affiche un README:
  - verifier la presence de `ncl/X.Y.Z/index.html`
- Si la doc ne reflète pas la derniere ontologie:
  - verifier que `ontology.owl` racine a bien ete remplace avant `generate-widoco.ps1`
- Si Maven echoue:
  - verifier Java/Maven avec `java -version` et `mvn -version`



Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass; .\scripts\run-create-natclinn-tbox.ps1

Copy-Item .\NewGeneratedOntology\ontology.owl .\ontology.owl -Force

./scripts/generate-widoco.ps1 

./scripts/release-widoco-version.ps1 -Version 1.0.2