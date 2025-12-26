# Automation Script: Run Test Server
Write-Host "Updating plugin and starting server..." -ForegroundColor Cyan

$serverDir = "test-server"
$pluginsDir = "$serverDir/plugins"

# 1. Build the project if needed (optional, but good for automation)
# Write-Host "Building project..."
# & ./bin/build.ps1

# 2. Copy the latest jar to the server
$jarPath = Get-ChildItem -Path "target" -Filter "uhc-*.jar" | Sort-Object LastWriteTime -Descending | Select-Object -First 1

if ($jarPath) {
  Write-Host "Copying $($jarPath.Name) to server..." -ForegroundColor Green
  Copy-Item -Path $jarPath.FullName -Destination "$pluginsDir/UHCPlugin.jar" -Force
}
else {
  Write-Error "No JAR found in target folder. Did you run build.ps1?"
  exit
}

# 3. Start the server
Write-Host "Starting Paper server..." -ForegroundColor Yellow
cd $serverDir
java -Xmx2G -jar paper.jar nogui
