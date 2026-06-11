param(
    [string]$OntologyFile = "ontology.owl",
    [string]$OutputFolder = "ncl",
    [string]$WidocoVersion = "1.4.25",
    [string]$Languages = "fr-en",
    [switch]$UseDocker,
    [switch]$UniteSections
)

$ErrorActionPreference = "Stop"
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12

$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path
$ontologyPath = Join-Path $repoRoot $OntologyFile
$outputPath = Join-Path $repoRoot $OutputFolder

if (-not (Test-Path $ontologyPath)) {
    throw "Ontology file not found: $ontologyPath"
}

New-Item -ItemType Directory -Force -Path $outputPath | Out-Null
# Keep regeneration deterministic by clearing previous generated content.
Get-ChildItem -Path $outputPath -Force -ErrorAction SilentlyContinue | Remove-Item -Recurse -Force

$widocoArgs = @(
    "-ontFile", $ontologyPath,
    "-outFolder", $outputPath,
    "-rewriteAll",
    "-oops",
    "-webVowl"
)

if ($UniteSections) {
    $widocoArgs += "-uniteSections"
}

if ($Languages) {
    $widocoArgs += @("-lang", $Languages)
}

Write-Host "WIDOCO options forced: OOPS=ON, WebVOWL=ON"
Write-Host "Selected languages: $Languages"

if ($UseDocker) {
    $image = "ghcr.io/dgarijo/widoco:v$WidocoVersion"
    Write-Host "Using Docker image: $image"
    docker pull $image | Out-Host

    $containerIn = "/usr/local/widoco/in"
    $containerOut = "/usr/local/widoco/out"

    $dockerArgs = @(
        "run", "--rm", "-t",
        "-v", "$repoRoot`:$containerIn",
        "-v", "$repoRoot`:$containerOut",
        $image,
        "-ontFile", "in/$OntologyFile",
        "-outFolder", "out/$OutputFolder",
        "-rewriteAll",
        "-oops",
        "-webVowl"
    )

    if ($UniteSections) { $dockerArgs += "-uniteSections" }
    if ($Languages) {
        $dockerArgs += @("-lang", $Languages)
    }

    & docker @dockerArgs
}
else {
    $toolsDir = Join-Path $repoRoot ".tools/widoco"
    New-Item -ItemType Directory -Force -Path $toolsDir | Out-Null

    $releaseApi = "https://api.github.com/repos/dgarijo/Widoco/releases/tags/v$WidocoVersion"
    $releaseData = Invoke-RestMethod -Uri $releaseApi

    $jarAssets = @($releaseData.assets | Where-Object { $_.name -like "*.jar" })
    if ($jarAssets.Count -eq 0) {
        throw "No JAR assets found for WIDOCO version $WidocoVersion"
    }

    $preferredSuffixes = @("_JDK-17.jar", "_JDK-14.jar", "_JDK-11.jar")
    $selectedAsset = $null
    foreach ($suffix in $preferredSuffixes) {
        $selectedAsset = $jarAssets | Where-Object { $_.name -like "*$suffix" } | Select-Object -First 1
        if ($selectedAsset) { break }
    }
    if (-not $selectedAsset) {
        $selectedAsset = $jarAssets | Select-Object -First 1
    }

    $jarName = $selectedAsset.name
    $jarPath = Join-Path $toolsDir $jarName

    if ((Test-Path $jarPath) -and ((Get-Item $jarPath).Length -lt 100000)) {
        Remove-Item $jarPath -Force
    }

    if (-not (Test-Path $jarPath)) {
        $url = $selectedAsset.browser_download_url
        Write-Host "Downloading WIDOCO from: $url"

        $downloaded = $false

        for ($attempt = 1; $attempt -le 2; $attempt++) {
            try {
                Invoke-WebRequest -Uri $url -OutFile $jarPath
                $downloaded = $true
                break
            }
            catch {
                Write-Host "Invoke-WebRequest failed (attempt $attempt): $($_.Exception.Message)"
            }
        }

        if (-not $downloaded) {
            Write-Host "Falling back to curl.exe"
            & curl.exe -L $url -o $jarPath
            if ($LASTEXITCODE -ne 0) {
                throw "Failed to download WIDOCO JAR with curl.exe"
            }
            $downloaded = $true
        }

        if (-not (Test-Path $jarPath)) {
            throw "WIDOCO JAR download failed: $jarPath"
        }

        if ((Get-Item $jarPath).Length -lt 100000) {
            throw "Downloaded WIDOCO JAR seems invalid (file too small): $jarPath"
        }
    }

    Write-Host "Using JAR: $jarPath"
    & java -jar $jarPath @widocoArgs
}

# Some WIDOCO runs place content under an extra doc/ subfolder.
$generatedRoot = $outputPath
$docSubfolder = Join-Path $outputPath "doc"
if ((Test-Path (Join-Path $docSubfolder "index-en.html")) -or (Test-Path (Join-Path $docSubfolder "index.html"))) {
    Get-ChildItem -Path $docSubfolder -Force | Move-Item -Destination $outputPath -Force
    Remove-Item -Path $docSubfolder -Recurse -Force
    $generatedRoot = $outputPath
}

if (-not (Test-Path (Join-Path $generatedRoot "index-en.html")) -and -not (Test-Path (Join-Path $generatedRoot "index.html"))) {
    throw "WIDOCO execution finished but no index file was found in $generatedRoot"
}

Write-Host "WIDOCO documentation generated in: $generatedRoot"
