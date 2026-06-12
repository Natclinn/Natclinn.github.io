param(
    [Parameter(Mandatory = $true)]
    [ValidatePattern('^\d+\.\d+\.\d+$')]
    [string]$Version,

    [string]$SourceFolder = "ncl"
)

$ErrorActionPreference = "Stop"

$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path
$sourcePath = Join-Path $repoRoot $SourceFolder
$targetPath = Join-Path $sourcePath $Version

if (-not (Test-Path $sourcePath)) {
    throw "Source folder not found: $sourcePath"
}

if (Test-Path $targetPath) {
    throw "Version already exists: $targetPath"
}

New-Item -ItemType Directory -Path $targetPath | Out-Null

# Copy current WIDOCO output to the new version folder,
# excluding already versioned directories (x.y.z).
Get-ChildItem -LiteralPath $sourcePath -Force |
    Where-Object {
        -not ($_.PSIsContainer -and $_.Name -match '^\d+\.\d+\.\d+$')
    } |
    ForEach-Object {
        Copy-Item -LiteralPath $_.FullName -Destination $targetPath -Recurse -Force
    }

Write-Host "Release copied to: $targetPath"
