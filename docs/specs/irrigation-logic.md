# Specification: Irrigation Logic

## Goal
Determine if a garden zone should be irrigated based on weather data.

## Decision Rules
The current logic (subject to change) is:
1. Fetch the weather forecast for today (rain and temperature).
2. Fetch the rain history of the past 7 days.
3. Fetch the irrigation history (to see if it was irrigated yesterday).
4. **Decision:**
   - IF the expected rain today < 2.0 mm AND the total rain of the past week < 15.0 mm:
     - IF the expected max temperature > 25.0 °C:
       - THEN: Advise 30 minutes of irrigation.
     - ELSE (temperature <= 25.0 °C):
       - IF NOT irrigated yesterday:
         - THEN: Advise 15 minutes of irrigation.
       - ELSE:
         - THEN: Advise 0 minutes of irrigation.
   - ELSE:
     - THEN: Advise 0 minutes of irrigation.

## Process (Saga)
1. `EvaluateIrrigation`: Triggered by a schedule (e.g., 06:00).
2. `ProposeIrrigation`: If irrigation is needed, create and save a proposal in the `advisory` module.
3. `NotifyUser`: `notification` module reacts to `IrrigationProposed` event.
4. `Wait`: Wait for a certain amount of time (e.g., until 07:30) for cancellation.
5. `Execute`: The `IrrigationAgent` fetches the advice and triggers the `irrigation` module to start.
