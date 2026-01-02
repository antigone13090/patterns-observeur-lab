#!/usr/bin/env bash
set -euo pipefail

export PATH="$HOME/.local/bin:$PATH"

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$ROOT"

echo "[1/4] Build + SBOM"
mvn -q -DskipTests package
mvn -q -DskipTests org.cyclonedx:cyclonedx-maven-plugin:makeAggregateBom
test -f target/bom.json

echo "[2/4] SCA (Trivy SBOM) : fail si HIGH/CRITICAL"
trivy sbom --quiet --scanners vuln --severity HIGH,CRITICAL --exit-code 1 target/bom.json
echo "OK Trivy: aucune vuln HIGH/CRITICAL."

echo "[3/4] SAST (Semgrep offline via Docker) : fail si ERROR"
if [ -f scripts/security/semgrep/java-gate.yml ]; then
  docker run --rm -v "$ROOT:/src" -w /src returntocorp/semgrep \
    semgrep scan --config scripts/security/semgrep/java-gate.yml --error
  echo "OK Semgrep."
else
  echo "Semgrep: config absente (scripts/security/semgrep/java-gate.yml) -> skip."
fi

echo "[4/4] DAST (ZAP baseline) : APP_URL requis"
APP_URL="${APP_URL:-}"
ZAP_OUT_DIR="${ZAP_OUT_DIR:-$ROOT/zap-out}"
ZAP_UID="${ZAP_UID:-1000}"
ZAP_GID="${ZAP_GID:-1000}"
ZAP_CONF_PATH="$ROOT/scripts/security/zap.conf"

if [ -z "$APP_URL" ]; then
  echo "APP_URL non défini -> skip ZAP."
  echo "OK: scan terminé."
  exit 0
fi

# 4.1) Vérifs reachability (host + conteneur)
curl -fsSI "$APP_URL/" >/dev/null || { echo "APP_URL injoignable depuis l'hôte: $APP_URL" >&2; exit 2; }
docker run --rm --network host curlimages/curl:8.6.0 -fsSI "$APP_URL/" >/dev/null \
  || { echo "APP_URL injoignable depuis un conteneur (--network host): $APP_URL" >&2; exit 2; }

# 4.2) Préparer dossier de sortie + droits compatibles ZAP
rm -rf "$ZAP_OUT_DIR"
mkdir -p "$ZAP_OUT_DIR"

# Sur certaines machines, ZAP écrit avec uid=1000 (user 'zap')
# chown not needed (container runs as current uid/gid)
chmod -R u+rwX,g+rwX "$ZAP_OUT_DIR" 2>/dev/null || true

# 4.3) Vérif que le volume est inscriptible par un conteneur
docker run --rm -v "$ZAP_OUT_DIR:/zap/wrk" alpine sh -lc 'echo ok >/zap/wrk/_write_test' >/dev/null

# 4.4) Exécuter ZAP
# --autooff => évite les soucis de chemins/report
# -w /zap/wrk => les sorties zap.html/zap.json sont RELATIVES au workdir
# -c zap.conf => ignore 10049/10109 (informatif)
if [ -f "$ZAP_CONF_PATH" ]; then
  docker run --rm --network host --user "$ZAP_UID:$ZAP_GID" \
    -v "$ZAP_OUT_DIR:/zap/wrk" -w /zap/wrk \
    -v "$ZAP_CONF_PATH:/zap/wrk/zap.conf:ro" \
    ghcr.io/zaproxy/zaproxy:stable \
    zap-baseline.py --autooff -t "$APP_URL" -m 2 -T 15 -c zap.conf -r zap.html -J zap.json -s
else
  docker run --rm --network host --user "$ZAP_UID:$ZAP_GID" \
    -v "$ZAP_OUT_DIR:/zap/wrk" -w /zap/wrk \
    ghcr.io/zaproxy/zaproxy:stable \
    zap-baseline.py --autooff -t "$APP_URL" -m 2 -T 15 -r zap.html -J zap.json -s
fi

test -f "$ZAP_OUT_DIR/zap.json"
test -f "$ZAP_OUT_DIR/zap.html"
echo "ZAP reports: $ZAP_OUT_DIR/zap.html et $ZAP_OUT_DIR/zap.json"

echo "OK: scan terminé."
