# Specification: Modulith Structure

## Modules
The application is divided into the following Spring Modulith modules:

### 1. Advisory (`advisory`)
- Responsible for the evaluation of weather data.
- Publishes `WeatherEvaluated` and `IrrigationProposed` events.

### 2. Irrigation (`irrigation`)
- Responsible for the execution of the physical irrigation (or the mock).
- Listens to `IrrigationProposed` and publishes `IrrigationStarted`/`IrrigationCompleted`.
- Manages the zone status.

### 3. Notification (`notification`)
- Responsible for external communication (e.g., WhatsApp).
- Listens to `IrrigationProposed` and publishes `NotificationSent`.

## Communication
No direct dependencies between modules where possible. All interaction happens via domain events handled by the Saga/Process Manager.
