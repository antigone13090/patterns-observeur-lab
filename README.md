# MLOps Observer Lab (Spring Boot)

Mini-projet **MLOps / Observability** pour démontrer le pattern **Observer** sur un moteur de **déclenchement automatique d’actions** :

- alerte Slack (simulée)
- alerte email (simulée)
- rollback (simulé)
- réentraînement (simulé)
- ouverture ticket (simulée)
- mise à jour dashboard (simulée)

## Prérequis
- Java 17+
- Maven (`mvn`)  
  - Sur Debian/Kali : `sudo apt update && sudo apt install -y maven`
- (optionnel) `jq` pour formater le JSON

> Important : ne colle pas les balises Markdown du type ```bash dans ton terminal. Colle uniquement les lignes de commandes.

## Lancer en local (port 8091)

### 1) Tests
```bash
cd ~/Documents/projets_persos/patterns/observer/mlops-observer-lab
./mvnw -q test
```

### 2) Démarrer
```bash
./mvnw spring-boot:run
```

### 3) Tester la page web
```bash
curl -4 -s http://127.0.0.1:8091/ | head -n 20
```
Ou navigateur : `http://127.0.0.1:8091/`

### 4) Tester l’API
```bash
curl -4 -s -X POST "http://127.0.0.1:8091/api/mlops/event" \
  -H "Content-Type: application/json" \
  -d '{"modelId":"reco-v3","incidentType":"DRIFT","metricName":"psi_drift","value":0.35,"threshold":0.20,"notes":"batch=2026-01-01","forceSeverity":"ALERT"}' | jq .
```

### 5) Voir quel PID écoute
```bash
ss -lntp | rg ':8091\b' | rg java
```

### 6) Stopper
- Terminal où l’app tourne : `Ctrl+C`

Sinon tuer par PID :
```bash
# exemple: users:(("java",pid=12345,fd=...))
kill -15 12345
```

## Documentation
- `docs/EXPLICATIONS.md` : explication du pattern Observer + comment ajouter une action.
