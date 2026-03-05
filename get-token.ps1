# -------------------------------------------------------
# get-token.ps1 — Obtain a Keycloak JWT token
# For Windows PowerShell users
# -------------------------------------------------------
# Usage:
#   .\get-token.ps1 <username> <password>
#
# Examples:
#   .\get-token.ps1 owner-user owner123
#   .\get-token.ps1 reporter-user reporter123
# -------------------------------------------------------

param(
  [string]$Username,
  [string]$Password
)

$KeycloakUrl = "http://localhost:9090"
$Realm = "ums"
$ClientId = "ums-client"
$ClientSecret = "ums-client-secret"

if (-not $Username -or -not $Password) {
  Write-Host ""
  Write-Host "Usage: .\get-token.ps1 <username> <password>"
  Write-Host ""
  Write-Host "Available users:"
  Write-Host "  owner-user       / owner123"
  Write-Host "  operator-user    / operator123"
  Write-Host "  maintainer-user  / maintainer123"
  Write-Host "  developer-user   / developer123"
  Write-Host "  reporter-user    / reporter123"
  Write-Host ""
  exit 1
}

$Body = "client_id=$ClientId&client_secret=$ClientSecret&grant_type=password&username=$Username&password=$Password"
$Url = "$KeycloakUrl/realms/$Realm/protocol/openid-connect/token"

try {
  $Response = Invoke-RestMethod -Method Post -Uri $Url -Body $Body -ContentType "application/x-www-form-urlencoded"
  Write-Host ""
  Write-Host "Token obtained successfully. Copy the value below:"
  Write-Host ""
  Write-Host $Response.access_token
  Write-Host ""
} catch {
  Write-Host ""
  Write-Host "Error: could not obtain token. Is Keycloak running on $KeycloakUrl?"
  Write-Host $_.Exception.Message
  Write-Host ""
  exit 1
}
