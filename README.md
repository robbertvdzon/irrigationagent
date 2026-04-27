Project: Garden Irrigation Advisor

Todo:
[X] create specs
[X] create ADRs
[X] IT tests with postgresql and wiremock
[X] use modulith
[X] everything async (Kotlin Coroutines + R2DBC + WebClient)
[X] use events (persisted)
[ ] sagas
[ ] use spring AI
[ ] latest kotlin and spring version

Goal:
Build a small modular application that decides whether to irrigate a garden zone based on weather data.
Before executing irrigation, the system notifies the user (via WhatsApp) and allows cancellation via a REST endpoint.


Architecture:
- Kotlin + Spring Boot
- Use Spring Modulith (modular monolith)
- Modules:
    - weatherforecast
    - rainhistory
    - advisory
    - irrigation
    - agent
    - dashboard


Core Flow:
1. Scheduler triggers advice generation at 06:00
2. Fetch weather forecast + past rainfall (7 days)
3. Evaluate irrigation need based on rain/temperature rules
4. Save advice (PENDING) and publish IrrigationProposed event
5. Scheduler triggers execution at 07:30
6. If advice > 0 minutes: start irrigation and mark advice as EXECUTED


Key Concepts:
- Domain-driven design (lightweight)
- Event-driven architecture
- Persist domain events (event log table)
- One saga/process manager:
  IrrigationProposal → Notification → Wait → Execute/Cancel


Events:
- IrrigationProposed


External Integrations:
- Weather API (forecast + past rain)
- Irrigation adapter (HTTP or mock)


REST API:
- GET  /                      — dashboard UI
- GET  /advice-fragment       — HTMX fragment: today's advice
- POST /calculate-advice      — trigger advice calculation for today
- POST /execute-now           — execute today's advice immediately
- PUT  /update-advice?minutes — manually override today's advice duration
- GET  /api/dashboard/data    — JSON: all dashboard data in one call


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
