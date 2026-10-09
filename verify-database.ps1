$ErrorActionPreference = "Stop"

$projectRoot = Split-Path $PSScriptRoot -Parent
$driver = Join-Path $projectRoot "lib\sqlite-jdbc-3.53.4.0.jar"
$source = Join-Path $PSScriptRoot "VerifyDatabase.java"
$classes = Join-Path $PSScriptRoot "classes"

if (-not (Test-Path $driver)) {
    throw "SQLite JDBC driver not found: $driver"
}

if (-not (Test-Path $source)) {
    throw "VerifyDatabase.java not found: $source"
}

New-Item -ItemType Directory -Path $classes -Force | Out-Null

Push-Location $projectRoot
try {
    javac -encoding UTF-8 -cp $driver -d $classes $source

    if ($LASTEXITCODE -ne 0) {
        throw "Java compilation failed."
    }

    java -cp "$classes;$driver" VerifyDatabase

    if ($LASTEXITCODE -ne 0) {
        throw "Database verification failed."
    }
}
finally {
    Pop-Location
}