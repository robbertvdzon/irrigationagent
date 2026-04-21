Project: Garden Irrigation Advisor

Doen:
[X] specs maken
[X] ADR's maken
[ ] IT tests met postgresql en wiremock
[ ] modulith gebruiken
[ ] events gebruiken (persisted)
[ ] saga's
[ ] alles async
[ ] spring AI gebruiken
[ ] laatste kotlin en spring versie

Goal:
Build a small modular application that decides whether to irrigate a garden zone based on weather data.
Before executing irrigation, the system notifies the user (via WhatsApp) and allows cancellation via a REST endpoint.


Architecture:
- Kotlin + Spring Boot
- Use Spring Modulith (modular monolith)
- Modules:
    - advisory
    - irrigation
    - notification


Core Flow:
1. Fetch weather forecast + past rainfall
2. Evaluate irrigation need
3. If irrigation is needed:
    - create IrrigationProposal
    - send notification (WhatsApp)
    - wait X minutes
4. If user cancels:
    - mark proposal as cancelled
5. If not cancelled:
    - start irrigation
6. complete irrigation
7. persist all events


Key Concepts:
- Domain-driven design (lightweight)
- Event-driven architecture
- Persist domain events (event log table)
- One saga/process manager:
  IrrigationProposal → Notification → Wait → Execute/Cancel


Events:
- WeatherEvaluated
- IrrigationProposed
- NotificationSent
- IrrigationCancelledByUser
- IrrigationStarted
- IrrigationCompleted


Commands:
- EvaluateIrrigation
- ProposeIrrigation
- CancelIrrigation
- StartIrrigation
- CompleteIrrigation


External Integrations:
- Weather API (forecast + past rain)
- Notification adapter (WhatsApp or mock)
- Irrigation adapter (HTTP or mock)


REST API:
- POST /zones/{id}/evaluate
- POST /proposals/{id}/cancel


Agent (optional):
- Generate human-readable explanation for irrigation decision


Persistence:
- Use relational DB (e.g. Postgres)
- Store:
    - current state
    - domain events


Other:
- Use ports & adapters pattern
- Keep modules isolated (no direct cross-module access)
- Use scheduled task for evaluation
- Implement simple retry for external calls
