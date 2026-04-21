# Junie Richtlijnen voor Garden Irrigation Advisor

## Project Documentatie
Alle architectuur-beslissingen en specificaties moeten worden geraadpleegd voordat wijzigingen worden doorgevoerd:
- **ADR's:** Zie `docs/adr/` voor geaccepteerde ontwerpkeuzes.
- **Specs:** Zie `docs/specs/` voor functionele en technische specificaties.

## Regels voor de Agent
1. **Modules:** Respecteer de modulegrenzen zoals gedefinieerd in de specs. Gebruik geen directe cross-module imports tenzij strikt noodzakelijk voor de Spring Modulith API.
2. **Events:** Gebruik domein-events voor communicatie tussen modules. Persisteer belangrijke domein-events in de database.
3. **Saga's:** De flow van evaluatie naar notificatie naar uitvoering wordt beheerd door een saga/process manager.
4. **Taal:** Communiceer met de gebruiker in het Nederlands. Code-commentaren mogen in het Engels blijven conform de bestaande stijl.
5. **Nieuwe Beslissingen:** Leg nieuwe architecturale keuzes vast in een nieuw ADR-bestand in `docs/adr/`.
