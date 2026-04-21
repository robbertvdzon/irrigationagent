# Junie Guidelines for Garden Irrigation Advisor

## Project Documentation
All architecture decisions and specifications must be consulted before implementing changes:
- **ADRs:** See `docs/adr/` for accepted design choices.
- **Specs:** See `docs/specs/` for functional and technical specifications.

## Rules for the Agent
1. **Modules:** Respect module boundaries as defined in the specs. Do not use direct cross-module imports unless strictly necessary for the Spring Modulith API.
2. **Events:** Use domain events for communication between modules. Persist important domain events in the database.
3. **Sagas:** The flow from evaluation to notification to execution is managed by a saga/process manager.
4. **Language:** Communicate with the user in English. Code comments and documentation must also be in English.
5. **New Decisions:** Record new architectural choices in a new ADR file in `docs/adr/`.
