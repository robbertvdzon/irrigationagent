# ADR 0002: Event-Driven Architectuur en Persistente Events

## Status
Geaccepteerd

## Context
De `README.md` specificeert dat we events moeten gebruiken, deze moeten persisteren en alles asynchroon moet verlopen waar mogelijk.

## Beslissing
We gebruiken een **Event-Driven Architectuur** waarbij domein-events worden gebruikt voor communicatie tussen modules. Deze events worden opgeslagen in een `event_log` tabel in de database voor audit en mogelijke replay functionaliteit.

## Gevolgen
- Modules publiceren events via Spring's `ApplicationEventPublisher`.
- Events worden asynchroon afgehandeld (`@Async` of Spring Modulith's event handling).
- Er is een centrale tabel `irrigation_events` (of vergelijkbaar) die alle belangrijke statusovergangen vastlegt.
