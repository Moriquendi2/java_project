# Team 7: Hao Yang, Zexuan Li, Haolin Chen.
$ErrorActionPreference = 'Stop'

$projectRoot = $PSScriptRoot
$sourceRoot = Join-Path $projectRoot 'src'
$outputRoot = Join-Path $projectRoot 'out'
$driverJar = Join-Path $projectRoot 'lib\mysql-connector-j-9.7.0.jar'
$sourceFiles = @(Get-ChildItem -Path $sourceRoot -Recurse -Filter '*.java' | ForEach-Object FullName)

if ($sourceFiles.Count -eq 0) {
    throw "No Java source files were found under $sourceRoot."
}

New-Item -ItemType Directory -Path $outputRoot -Force | Out-Null
& javac --release 21 -encoding UTF-8 -cp $driverJar -d $outputRoot $sourceFiles
if ($LASTEXITCODE -ne 0) {
    throw "javac failed with exit code $LASTEXITCODE."
}

Write-Host "Compiled $($sourceFiles.Count) Java source files into out\."
