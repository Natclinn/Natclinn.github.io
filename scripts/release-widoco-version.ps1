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

function Add-VersionNavBanner {
    param(
        [Parameter(Mandatory = $true)]
        [string]$HtmlFile,
        [Parameter(Mandatory = $true)]
        [string]$CurrentVersion,
        [Parameter(Mandatory = $true)]
        [string[]]$AllVersions,
        [string]$Lang = "en"
    )

    if (-not (Test-Path $HtmlFile)) { return }

    # Build navigation links
    $links = $AllVersions | Sort-Object {
        $parts = $_ -split '\.'; [int]$parts[0]*10000 + [int]$parts[1]*100 + [int]$parts[2]
    } -Descending | ForEach-Object {
        $v = $_
        $href = "../$v/index-$Lang.html"
        $label = "v$v"
        if ($v -eq $CurrentVersion) {
            "<span class='ncl-nav-current'>$label</span>"
        } else {
            "<a href='$href'>$label</a>"
        }
    }
    $linksHtml = $links -join ""

    $latestHref = "../index-$Lang.html"
    $latestLink = "<a href='$latestHref'>latest</a>"

    $banner = @"
<!-- NCL version nav injected by release-widoco-version.ps1 -->
<div id="ncl-version-nav" style="position:fixed;top:0;left:0;right:0;z-index:9999;background:#2c3e50;color:#ecf0f1;font-family:sans-serif;font-size:13px;padding:5px 16px;display:flex;align-items:center;gap:8px;box-shadow:0 2px 6px rgba(0,0,0,.4);">
  <strong style="margin-right:6px;letter-spacing:.5px;">NCL</strong>
  <span style="opacity:.6;margin-right:4px;">version:</span>
  $linksHtml
  <span style="margin:0 6px;opacity:.4;">|</span>
  $latestLink
  <span id="ncl-nav-close" onclick="document.getElementById('ncl-version-nav').style.display='none'" style="margin-left:auto;cursor:pointer;opacity:.6;font-size:16px;line-height:1;" title="Close">&#x2715;</span>
</div>
<style>
  #ncl-version-nav a{color:#3498db;text-decoration:none;padding:2px 6px;border-radius:3px;}
  #ncl-version-nav a:hover{background:rgba(255,255,255,.15);}
  #ncl-version-nav .ncl-nav-current{color:#2ecc71;font-weight:bold;padding:2px 6px;border-radius:3px;background:rgba(46,204,113,.15);}
  body{padding-top:32px !important;}
</style>
<!-- end NCL version nav -->
"@

    $content = Get-Content $HtmlFile -Raw
    # Inject right after <body (handles <body> and <body ...>)
    if ($content -match '(?i)<body[^>]*>') {
        $content = $content -replace '(?i)(<body[^>]*>)', "`$1`n$banner"
        [System.IO.File]::WriteAllText($HtmlFile, $content, (New-Object System.Text.UTF8Encoding($false)))
        Write-Host "Version nav injected: $HtmlFile"
    } else {
        Write-Host "Warning: <body> not found in $HtmlFile, skipping nav injection."
    }
}

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

# Collect all versioned subfolders (including the one just created)
$allVersions = @(Get-ChildItem -LiteralPath $sourcePath -Directory |
    Where-Object { $_.Name -match '^\d+\.\d+\.\d+$' } |
    Select-Object -ExpandProperty Name)

# Inject version navigation bar into the archived HTML pages
foreach ($lang in @("en", "fr")) {
    $htmlFile = Join-Path $targetPath "index-$lang.html"
    Add-VersionNavBanner -HtmlFile $htmlFile -CurrentVersion $Version -AllVersions $allVersions -Lang $lang
}

Write-Host "Release copied to: $targetPath"
