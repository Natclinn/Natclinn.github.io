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

function New-VersionIndexFile {
        param(
                [Parameter(Mandatory = $true)]
                [string]$DestinationFolder,
                [Parameter(Mandatory = $true)]
                [string]$Version
        )

        $indexPath = Join-Path $DestinationFolder "index.html"
        $html = @"
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>NCL $Version</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script>
        (function () {
            var lang = (navigator.language || navigator.userLanguage || "en").toLowerCase();
            var target = lang.indexOf("fr") === 0 ? "index-fr.html" : "index-en.html";
            window.location.replace(target);
        })();
    </script>
    <noscript>
        <meta http-equiv="refresh" content="0; url=index-en.html">
    </noscript>
</head>
<body>
    Redirecting...
</body>
</html>
"@

        Set-Content -Path $indexPath -Value $html -Encoding UTF8
        Write-Host "Version index created: $indexPath"
}

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

New-VersionIndexFile -DestinationFolder $targetPath -Version $Version

Write-Host "Release copied to: $targetPath"
