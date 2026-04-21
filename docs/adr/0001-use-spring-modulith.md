# ADR 0001: Use of Spring Modulith

## Status
Accepted

## Context
The application should be built modularly to ensure maintainability. We want to use a "Modular Monolith" (Modulith) approach instead of microservices to keep complexity low.

## Decision
We use **Spring Modulith** to maintain and verify the modular structure.

## Consequences
- Modules must remain strictly separated.
- Direct access between modules is limited to the public API (package-private vs public).
- Event-driven communication is preferred over direct service calls between modules.
