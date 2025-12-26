# Automation Script: Setup Test Server
Write-Host "Setting up UHC Test Server..." -ForegroundColor Cyan

# Define version and file paths
$paperVersion = "1.21.11"
$serverDir = "test-server"
$pluginsDir = "$serverDir/plugins"

# Create directories if they don't exist
if (!(Test-Path $pluginsDir)) {
  New-Item -ItemType Directory -Force -Path $pluginsDir
}

# 1. Download PaperMC
Write-Host "Fetching latest Paper build for $paperVersion..."
try {
  $apiUrl = "https://api.papermc.io/v2/projects/paper/versions/$paperVersion"
  $builds = Invoke-RestMethod -Uri "$apiUrl/builds"
  $latestBuild = $builds.builds[-1].build
  $downloadName = $builds.builds[-1].downloads.application.name
  $downloadUrl = "https://api.papermc.io/v2/projects/paper/versions/$paperVersion/builds/$latestBuild/downloads/$downloadName"
    
  Write-Host "Downloading $downloadName..." -ForegroundColor Green
  Invoke-WebRequest -Uri $downloadUrl -OutFile "$serverDir/paper.jar"
}
catch {
  Write-Error "Failed to download Paper. Check your internet connection or the PaperMC API."
}

# 2. Download Essential Plugins (TAB, SkinRestorer, PlaceholderAPI)
Write-Host "Downloading dependency plugins..."
# TAB Plugin
$tabUrl = "https://github.com/NEZNAMY/TAB/releases/download/5.4.0/TAB.v5.4.0.jar"
try {
  Write-Host "Downloading TAB..." -ForegroundColor Green
  Invoke-WebRequest -Uri $tabUrl -OutFile "$pluginsDir/TAB.jar"
}
catch {
  Write-Warning "Could not download TAB plugin."
}

# SkinsRestorer
$srUrl = "https://github.com/SkinsRestorer/SkinsRestorer/releases/download/15.9.1/SkinsRestorer.jar"
try {
  Write-Host "Downloading SkinsRestorer..." -ForegroundColor Green
  Invoke-WebRequest -Uri $srUrl -OutFile "$pluginsDir/SkinsRestorer.jar"
}
catch {
  Write-Warning "Could not download SkinsRestorer plugin."
}

# PlaceholderAPI
$papiUrl = "https://github.com/PlaceholderAPI/PlaceholderAPI/releases/download/2.11.7/PlaceholderAPI-2.11.7.jar"
try {
  Write-Host "Downloading PlaceholderAPI..." -ForegroundColor Green
  Invoke-WebRequest -Uri $papiUrl -OutFile "$pluginsDir/PlaceholderAPI.jar"
}
catch {
  Write-Warning "Could not download PlaceholderAPI plugin."
}

# 3. Accept EULA
Set-Content -Path "$serverDir/eula.txt" -Value "eula=true"

Write-Host "Server setup complete! Run run.ps1 to start the server." -ForegroundColor Cyan
