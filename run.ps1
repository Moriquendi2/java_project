# Team 7: Hao Yang, Zexuan Li, Haolin Chen.
# 中文：先编译项目，再使用编译结果和 JDBC 驱动启动 Swing 应用。
$ErrorActionPreference = 'Stop'

# 中文：确定项目目录并执行统一构建脚本；构建失败时传回错误码。
$projectRoot = $PSScriptRoot
& (Join-Path $projectRoot 'build.ps1')
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

# 中文：组合运行时类路径并启动停车控制台入口类。
$classPath = (Join-Path $projectRoot 'out') + ';' + (Join-Path $projectRoot 'lib\mysql-connector-j-9.7.0.jar')
& java -cp $classPath mia.parking.app.ParkingApplication
exit $LASTEXITCODE
