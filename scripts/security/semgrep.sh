#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/../.."

# Image semgrep
IMG="returntocorp/semgrep:latest"

# Pull best-effort (si déjà présent, c'est instant)
docker pull "$IMG" >/dev/null 2>&1 || true

# Scan "auto" (règles Semgrep maintenues par Semgrep)
# --error : exit code != 0 si findings de sévérité ERROR (ou erreurs de scan)
docker run --rm -v "$PWD:/src" -w /src "$IMG" \
  semgrep scan --config auto --error
