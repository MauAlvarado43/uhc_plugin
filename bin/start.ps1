# Automation Script: Master Start
Write-Host "--- UHC FULL AUTOMATION STARTING ---" -ForegroundColor Cyan

# 1. Setup the server (downloads Paper, TAB, etc.)
& "$PSScriptRoot/setup.ps1"

# 2. Build the plugin
& "$PSScriptRoot/build.ps1"

# 3. Run the server
& "$PSScriptRoot/run.ps1"
