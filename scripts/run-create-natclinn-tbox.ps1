param(
    [switch]$Quiet
)

$ErrorActionPreference = "Stop"

function Resolve-MavenCommand {
    $candidateNames = @("mvn", "mvn.cmd", "mvn.ps1")

    foreach ($candidateName in $candidateNames) {
        $command = Get-Command $candidateName -ErrorAction SilentlyContinue
        if ($command) {
            return $command.Source
        }
    }

    foreach ($mavenHomeVariable in @("MAVEN_HOME", "M2_HOME")) {
        $mavenHome = [Environment]::GetEnvironmentVariable($mavenHomeVariable)
        if (-not $mavenHome) {
            continue
        }

        foreach ($candidatePath in @(
            (Join-Path $mavenHome "bin\mvn.cmd"),
            (Join-Path $mavenHome "bin\mvn.ps1"),
            (Join-Path $mavenHome "bin\mvn")
        )) {
            if (Test-Path $candidatePath) {
                return $candidatePath
            }
        }
    }

    throw "Maven introuvable. Installez Apache Maven puis ajoutez son dossier bin au PATH, ou definissez MAVEN_HOME/M2_HOME."
}

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$repoRoot = Resolve-Path (Join-Path $scriptDir "..")
Push-Location $repoRoot

try {
    $mavenCommand = Resolve-MavenCommand

    if ($Quiet) {
        & $mavenCommand -q clean compile exec:java
    }
    else {
        & $mavenCommand clean compile exec:java
    }

    Write-Host "Generation terminee: NewGeneratedOntology/ontology.owl"
}
finally {
    Pop-Location
}
