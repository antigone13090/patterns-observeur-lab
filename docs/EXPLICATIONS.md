# Explication (FR) — MLOps Observer Lab

## Objectif
Cas pro typique : tu surveilles un modèle et tu veux déclencher automatiquement :
- Slack/email,
- rollback,
- réentraînement,
- ouverture ticket,
- mise à jour dashboard.

Ici, tout est **simulé** (pas d’API Slack réelle). Le but est l’architecture.

---

## Pattern Observer : où ?
- Publisher : `EventPublisher` (gère la liste, notifie)
- Observer : `EventObserver` (contrat)
- Observers : `observer/impl/*` (une classe par action)

---

## Pipeline d’exécution
1. `POST /api/mlops/event` reçoit la requête.
2. `MonitoringService` construit un `MonitoringEvent` (sévérité calculée ou forcée pour la démo).
3. `EventPublisher.publish(event)` notifie tous les observers.
4. Chaque observer renvoie un `ActionResult` (OK ou SKIPPED).
5. L’API retourne l’événement + la liste des actions.

---

## Ajouter une action (ex : “Update StatusPage”)
1. Créer une classe `StatusPageObserver implements EventObserver`.
2. L’abonner dans `MlopsConfig.eventPublisher()` avec `p.subscribe(new StatusPageObserver(...))`.

---

## Fichiers clés
- `src/main/java/com/acme/mlops/observer/EventPublisher.java`
- `src/main/java/com/acme/mlops/observer/EventObserver.java`
- `src/main/java/com/acme/mlops/observer/impl/*`
- `src/main/java/com/acme/mlops/web/MonitoringController.java`
