# Specificatie: Irrigatie Logica

## Doel
Bepalen of een tuin-zone beregend moet worden op basis van weersgegevens.

## Beslissingsregels
De huidige logica (onderhevig aan verandering) is:
1. Haal de weersverwachting voor vandaag op.
2. Haal de regen historie van de afgelopen 7 dagen op.
3. **Besluit:**
   - ALS de verwachte regen vandaag < 2.0 mm
   - EN de totale regen van de afgelopen week < 15.0 mm
   - DAN: Adviseer 30 minuten beregening.
   - ANDERS: Adviseer 0 minuten beregening.

## Proces (Saga)
1. `EvaluateIrrigation`: Triggered door een schema (bijv. 06:00).
2. `ProposeIrrigation`: Als beregening nodig is, maak een voorstel aan.
3. `NotifyUser`: Stuur een bericht (WhatsApp/Mock) naar de gebruiker.
4. `Wait`: Wacht een bepaalde tijd (bijv. tot 07:30) op annulering.
5. `Execute`: Start de beregening als er niet geannuleerd is.
