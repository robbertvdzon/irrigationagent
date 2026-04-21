# Specificatie: Modulith Structuur

## Modules
De applicatie is verdeeld in de volgende Spring Modulith modules:

### 1. Advisory (`advisory`)
- Verantwoordelijk voor de evaluatie van weersgegevens.
- Publiceert `WeatherEvaluated` en `IrrigationProposed` events.

### 2. Irrigation (`irrigation`)
- Verantwoordelijk voor de uitvoering van de fysieke beregening (of de mock).
- Luistert naar `IrrigationStarted` en publiceert `IrrigationCompleted`.
- Beheert de status van de zone.

### 3. Notification (`notification`)
- Verantwoordelijk voor externe communicatie (bijv. WhatsApp).
- Luistert naar `IrrigationProposed` en publiceert `NotificationSent`.

## Communicatie
Geen directe afhankelijkheden tussen modules waar mogelijk. Alle interactie verloopt via domein-events die worden afgehandeld door de Saga/Process Manager.
