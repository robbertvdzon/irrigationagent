# ADR 0002: Event-Driven Architecture and Persistent Events

## Status
Accepted

## Context
The `README.md` specifies that we must use events, persist them, and everything should be asynchronous where possible.

## Decision
We use an **Event-Driven Architecture** where domain events are used for communication between modules. These events are stored in an `irrigation_events` (or similar) table in the database for auditing and potential replay functionality.

## Consequences
- Modules publish events via Spring's `ApplicationEventPublisher`.
- Events are handled asynchronously (`@Async` or Spring Modulith's event handling).
- There is a central table capturing all important status transitions.
