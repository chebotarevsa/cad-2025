#!/usr/bin/env bash
set -euo pipefail

# Простая утилита деплоя WAR в Tomcat
# usage: TOMCAT_HOME=/opt/homebrew/opt/tomcat/libexec ./scripts/deploy.sh

if [ -z "${TOMCAT_HOME:-}" ]; then
  echo "TOMCAT_HOME is not set. Example: export TOMCAT_HOME=/opt/homebrew/opt/tomcat/libexec"
  exit 1
fi

WAR=$(ls build/libs/*.war 2>/dev/null | head -n1 || true)
if [ -z "$WAR" ]; then
  echo "WAR not found in build/libs. Run: ./gradlew clean war" >&2
  exit 2
fi

echo "Deploying $WAR to $TOMCAT_HOME/webapps/"
cp "$WAR" "$TOMCAT_HOME/webapps/"

# Try brew services restart first (if available)
if command -v brew >/dev/null 2>&1 && brew services list | grep -q "tomcat.*started"; then
  echo "Restarting Tomcat via brew services"
  brew services restart tomcat
else
  echo "Restarting Tomcat via scripts in $TOMCAT_HOME/bin"
  "$TOMCAT_HOME/bin/shutdown.sh" || true
  sleep 1
  "$TOMCAT_HOME/bin/startup.sh"
fi

echo "Deployed. Check http://localhost:8080/"
