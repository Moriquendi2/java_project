# Team 7: Hao Yang, Zexuan Li, Haolin Chen.
# 中文：使用 JDK 21 编译项目中的全部 Java 源码。
$ErrorActionPreference = 'Stop'

# 中文：确定项目目录、源码目录、输出目录和 JDBC 驱动路径。
$projectRoot = $PSScriptRoot
$sourceRoot = Join-Path $projectRoot 'src'
$outputRoot = Join-Path $projectRoot 'out'
$driverJar = Join-Path $projectRoot 'lib\mysql-connector-j-9.7.0.jar'
# 中文：收集源码；没有找到 Java 文件时立即停止构建。
$sourceFiles = @(Get-ChildItem -Path $sourceRoot -Recurse -Filter '*.java' | ForEach-Object FullName)

if ($sourceFiles.Count -eq 0) {
    throw "No Java source files were found under $sourceRoot."
}

# 中文：准备编译输出目录，并以 UTF-8 编码编译所有源码。
New-Item -ItemType Directory -Path $outputRoot -Force | Out-Null
& javac --release 21 -encoding UTF-8 -cp $driverJar -d $outputRoot $sourceFiles
# 中文：编译失败时返回明确错误，成功时报告输出位置。
if ($LASTEXITCODE -ne 0) {
    throw "javac failed with exit code $LASTEXITCODE."
}

Write-Host "Compiled $($sourceFiles.Count) Java source files into out\."
