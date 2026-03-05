#!/bin/sh
# -------------------------------------------------------
# get-token.sh — Obtain a Keycloak JWT token
# Cross-platform: auto-detects OS and available tools
# Works on macOS, Linux, Git Bash, WSL
# -------------------------------------------------------
# Usage:
#   ./get-token.sh <username> <password>
#
# Examples:
#   ./get-token.sh owner-user owner123
#   ./get-token.sh reporter-user reporter123
# -------------------------------------------------------

KEYCLOAK_URL="http://localhost:9090"
REALM="ums"
CLIENT_ID="ums-client"
CLIENT_SECRET="ums-client-secret"

USERNAME=$1
PASSWORD=$2

if [ -z "$USERNAME" ] || [ -z "$PASSWORD" ]; then
  echo ""
  echo "Usage: ./get-token.sh <username> <password>"
  echo ""
  echo "Available users:"
  echo "  owner-user       / owner123"
  echo "  operator-user    / operator123"
  echo "  maintainer-user  / maintainer123"
  echo "  developer-user   / developer123"
  echo "  reporter-user    / reporter123"
  echo ""
  exit 1
fi

# ---- Detect HTTP client ----
detect_http_client() {
  if command -v curl >/dev/null 2>&1; then
    echo "curl"
  elif command -v wget >/dev/null 2>&1; then
    echo "wget"
  else
    echo "none"
  fi
}

# ---- Detect JSON parser ----
detect_json_parser() {
  if command -v jq >/dev/null 2>&1; then
    echo "jq"
  else
    echo "grep"
  fi
}

# ---- Perform HTTP request ----
do_request() {
  CLIENT=$1
  URL="$KEYCLOAK_URL/realms/$REALM/protocol/openid-connect/token"
  DATA="client_id=$CLIENT_ID&client_secret=$CLIENT_SECRET&grant_type=password&username=$USERNAME&password=$PASSWORD"

  case "$CLIENT" in
    curl)
      curl -s -X POST "$URL" -d "$DATA"
      ;;
    wget)
      wget -q -O - --post-data="$DATA" "$URL"
      ;;
  esac
}

# ---- Extract token ----
extract_token() {
  PARSER=$1
  RESPONSE=$2

  case "$PARSER" in
    jq)
      echo "$RESPONSE" | jq -r '.access_token'
      ;;
    grep)
      echo "$RESPONSE" | grep -o '"access_token":"[^"]*"' | cut -d'"' -f4
      ;;
  esac
}

# ---- Main ----
HTTP_CLIENT=$(detect_http_client)
JSON_PARSER=$(detect_json_parser)

if [ "$HTTP_CLIENT" = "none" ]; then
  echo ""
  echo "Error: neither curl nor wget found."
  echo "Please install curl: https://curl.se/download.html"
  echo ""
  exit 1
fi

RESPONSE=$(do_request "$HTTP_CLIENT")

if [ -z "$RESPONSE" ]; then
  echo ""
  echo "Error: no response from Keycloak. Is it running on $KEYCLOAK_URL?"
  echo ""
  exit 1
fi

TOKEN=$(extract_token "$JSON_PARSER" "$RESPONSE")

if [ -z "$TOKEN" ] || [ "$TOKEN" = "null" ]; then
  echo ""
  echo "Error: could not extract token. Check username and password."
  echo ""
  echo "Raw response:"
  echo "$RESPONSE"
  exit 1
fi

echo ""
echo "Token obtained successfully. Copy the value below:"
echo ""
echo "$TOKEN"
echo ""
