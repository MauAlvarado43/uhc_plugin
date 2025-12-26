param(
    [string]$versionType = "hotfix"
)

function Update-Version {
    param (
        [string]$type
    )
    
    $pomPath = "pom.xml"
    [xml]$pom = Get-Content $pomPath
    
    $currentVersion = $pom.project.version
    Write-Host "Versión actual: $currentVersion" -ForegroundColor Cyan
    
    if ($type -eq "none") {
        Write-Host "No se modificará la versión" -ForegroundColor Yellow
        return
    }
    
    $versionParts = $currentVersion -split '\.'
    $major = [int]$versionParts[0]
    $minor = [int]$versionParts[1]
    $patch = [int]$versionParts[2]
    $hotfix = [int]$versionParts[3]
    
    switch ($type) {
        "major" {
            $major++
            $minor = 0
            $patch = 0
            $hotfix = 0
        }
        "minor" {
            $minor++
            $patch = 0
            $hotfix = 0
        }
        "patch" {
            $patch++
            $hotfix = 0
        }
        "hotfix" {
            $hotfix++
        }
    }
    
    $newVersion = "$major.$minor.$patch.$hotfix"
    $pom.project.version = $newVersion
    $pom.Save((Resolve-Path $pomPath))
    
    Write-Host "Nueva versión: $newVersion" -ForegroundColor Green
}

Update-Version -type $versionType

mvn clean package
