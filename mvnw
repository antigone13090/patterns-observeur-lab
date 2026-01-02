#!/usr/bin/env bash
set -euo pipefail

if ! command -v mvn >/dev/null 2>&1; then
  echo "Erreur: Maven (mvn) introuvable."
  echo "Installe-le puis relance :"
  echo "  sudo apt update && sudo apt install -y maven"
  exit 1
fi

exec mvn "$@"
