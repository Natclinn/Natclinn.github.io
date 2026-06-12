param(
    [switch]$Quiet
)

$ErrorActionPreference = "Stop"

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$repoRoot = Resolve-Path (Join-Path $scriptDir "..")
Push-Location $repoRoot

try {
    if ($Quiet) {
        mvn -q clean compile exec:java
    }
    else {
        mvn clean compile exec:java
    }

    Write-Host "Generation terminee: NewGeneratedOntology/ontology.owl"
}
finally {
    Pop-Location
}
