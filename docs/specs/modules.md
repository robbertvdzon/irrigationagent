# Specification: Modulith Structure

## Modules
The application is divided into the following Spring Modulith modules:

### 1. Weather Forecast (`weatherforecast`)
- Responsible for fetching and storing the weather forecast.
- Provides `WeatherForecastPort`.

### 2. Rain History (`rainhistory`)
- Responsible for fetching and storing the historical rainfall data.
- Provides `RainHistoryPort`.

### 3. Advisory (`advisory`)
- Responsible for the evaluation of weather data and rain history.
- Uses `WeatherForecastPort` and `RainHistoryPort`.
- Publishes `IrrigationProposed` events.

### 4. Irrigation (`irrigation`)
- Responsible for the execution of the physical irrigation (or the mock).
- Listens to `IrrigationProposed` and publishes `IrrigationStarted`/`IrrigationCompleted`.
- Manages the zone status.

### 5. Notification (`notification`)
- Responsible for external communication (e.g., WhatsApp).
- Listens to `IrrigationProposed` and publishes `NotificationSent`.

### 6. Dashboard (`dashboard`)
- Responsible for the web UI and providing data for the frontend.
- Uses various ports from other modules.

## Communication
No direct dependencies between modules where possible. Data flow between modules happens via Ports/Adapters or domain events.
