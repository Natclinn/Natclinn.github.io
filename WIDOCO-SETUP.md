# WIDOCO setup (Windows)

This repository includes a script to generate ontology documentation with WIDOCO.

## Prerequisites

- Java 8+ (you already have Java 17)
- Optional: Docker

## Quick start

From the repository root:

```powershell
./scripts/generate-widoco.ps1
```

This will:

- Download WIDOCO JAR version 1.4.25 to `.tools/widoco/` (first run only)
- Auto-select the JAR asset matching your local Java version
- Generate documentation from `ontology.owl`
- Write output to `ncl/`
- Generate both French and English pages by default (`fr-en`)
- Always enable OOPS and WebVowl

## Useful options

Generate in a custom folder:

```powershell
./scripts/generate-widoco.ps1 -OutputFolder docs
```

Generate a single-page file for local browser preview:

```powershell
./scripts/generate-widoco.ps1 -UniteSections
```

Generate multilingual documentation (example French + English):

```powershell
./scripts/generate-widoco.ps1 -Languages fr-en
```

Use Docker instead of Java JAR:

```powershell
./scripts/generate-widoco.ps1 -UseDocker
```

Use Docker in multilingual mode:

```powershell
./scripts/generate-widoco.ps1 -UseDocker -Languages fr-en
```

## Publish on GitHub Pages

Recommended structure:

- Keep generated files in `ncl/`
- Link your root `index.html` to `ncl/index-en.html`

After regeneration:

1. Review changes
2. Commit
3. Push

Example:

```powershell
git add .
git commit -m "Generate WIDOCO documentation"
git push
```

## Archive a stable release

To keep old versions available:

1. Generate latest docs in `ncl/`
2. Copy them to a version folder (example `ncl/1.0.0`)
3. Update the `versions` array in `index.html`

Example copy command:

```powershell
New-Item -ItemType Directory -Force -Path .\ncl\1.0.0 | Out-Null
Copy-Item -Recurse -Force .\ncl\* .\ncl\1.0.0
```

Then, in `index.html`, set:

```javascript
var versions = [
	{ label: "1.0.0", href: "ncl/1.0.0/index-en.html" }
];
```
