@echo off
setlocal enabledelayedexpansion

set SERVER_DIR=test_server
set PLUGINS_DIR=%SERVER_DIR%\plugins
set SPIGOT_JAR=%SERVER_DIR%\spigot.jar
set EULA_FILE=%SERVER_DIR%\eula.txt

set PLUGIN_JAR=
for %%F in (target\*.jar) do (
    set PLUGIN_JAR=%%F
)

if "%PLUGIN_JAR%"=="" (
    echo No plugin found in target\*.jar
    exit /b
)

if not exist "%SERVER_DIR%" (
    mkdir "%SERVER_DIR%"
    mkdir "%PLUGINS_DIR%"
)

if not exist "%SPIGOT_JAR%" (

    echo spigot.jar not found. Downloading and building with BuildTools...

    if not exist "%SERVER_DIR%\buildtools" (
        mkdir "%SERVER_DIR%\buildtools"
    )

    curl -o "%SERVER_DIR%\buildtools\BuildTools.jar" https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar
    
    pushd "%SERVER_DIR%\buildtools"
    java -jar BuildTools.jar --rev latest --compile spigot
    for %%J in (spigot-*.jar) do (
        move /Y "%%J" "..\spigot.jar" >nul 2>&1
    )
    popd
)

copy /Y "%PLUGIN_JAR%" "%PLUGINS_DIR%\"
for %%F in (libs\*.jar) do (
    copy /Y "%%F" "%PLUGINS_DIR%\"
)

cd "%SERVER_DIR%"
start /B cmd /C "java -Xmx2G -Xms2G -jar spigot.jar"

rem Accept EULA
if exist "%EULA_FILE%" (
    for /f "tokens=1,2 delims==" %%A in ('findstr /i "eula" "%EULA_FILE%"') do (
        if "%%B"=="false" (
            echo Accepting the EULA...
            echo eula=true > "%EULA_FILE%"
        )
    )
) else (
    echo eula=true > "%EULA_FILE%"
)
