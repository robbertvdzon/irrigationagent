# ADR 0001: Gebruik van Spring Modulith

## Status
Geaccepteerd

## Context
De applicatie moet modulair worden opgebouwd om onderhoudbaarheid te garanderen. We willen een "Modular Monolith" (Modulith) benadering gebruiken in plaats van microservices om complexiteit laag te houden.

## Beslissing
We gebruiken **Spring Modulith** om de modulaire structuur te handhaven en te verifiëren. 

## Gevolgen
- Modules moeten strikt gescheiden blijven.
- Directe toegang tussen modules is beperkt tot de publieke API (package-private vs public).
- Event-driven communicatie heeft de voorkeur boven directe service calls tussen modules.
