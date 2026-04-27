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
- Manages irrigation advice lifecycle (saving, providing overview).
- Uses `WeatherForecastPort`, `RainHistoryPort`, and `IrrigationPort` (for history).
- Publishes `IrrigationProposed` events.

### 4. Irrigation (`irrigation`)
- Responsible for the execution of the physical irrigation (or the mock).
- Manages the history of executed irrigation events.
- Does NOT depend on other modules.

### 5. Agent (`agent`)
- Responsible for the scheduled triggering of advice generation and irrigation execution.
- Uses `AdvisoryPort` and `IrrigationPort`.
- Runs `generateDailyAdvice` at 06:00 and `executeDailyAdvice` at 07:30 (configurable via application.yml).

### 6. Dashboard (`dashboard`)
- Responsible for the web UI and providing data for the frontend.
- Uses various ports from other modules.

## Communication
No direct dependencies between modules where possible. Data flow between modules happens via Ports/Adapters or domain events.
