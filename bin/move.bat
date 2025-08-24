@echo off

set SERVER_DIR=test_server
set PLUGINS_DIR=%SERVER_DIR%\plugins

set PLUGIN_JAR=
for %%F in (target\*.jar) do (
    set PLUGIN_JAR=%%F
)

if "%PLUGIN_JAR%"=="" (
    echo No se encontró un plugin compilado en target\*.jar
    exit /b
)

if not exist "%SERVER_DIR%" (
    mkdir "%SERVER_DIR%"
    mkdir "%PLUGINS_DIR%"
)

copy /Y "%PLUGIN_JAR%" "%PLUGINS_DIR%\"
for %%F in (libs\*.jar) do (
    copy /Y "%%F" "%PLUGINS_DIR%\"
)