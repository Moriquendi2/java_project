# Team 7: Hao Yang, Zexuan Li, Haolin Chen.
$ErrorActionPreference = 'Stop'

$projectRoot = $PSScriptRoot
& (Join-Path $projectRoot 'build.ps1')
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

$classPath = (Join-Path $projectRoot 'out') + ';' + (Join-Path $projectRoot 'lib\mysql-connector-j-9.7.0.jar')
& java -cp $classPath mia.parking.app.ParkingApplication
exit $LASTEXITCODE
