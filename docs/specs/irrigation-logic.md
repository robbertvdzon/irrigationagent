# Specification: Irrigation Logic

## Goal
Determine if a garden zone should be irrigated based on weather data.

## Decision Rules
The current logic (subject to change) is:
1. Fetch the weather forecast for today.
2. Fetch the rain history of the past 7 days.
3. **Decision:**
   - IF the expected rain today < 2.0 mm
   - AND the total rain of the past week < 15.0 mm
   - THEN: Advise 30 minutes of irrigation.
   - ELSE: Advise 0 minutes of irrigation.

## Process (Saga)
1. `EvaluateIrrigation`: Triggered by a schedule (e.g., 06:00).
2. `ProposeIrrigation`: If irrigation is needed, create a proposal.
3. `NotifyUser`: Send a message (WhatsApp/Mock) to the user.
4. `Wait`: Wait for a certain amount of time (e.g., until 07:30) for cancellation.
5. `Execute`: Start irrigation if not cancelled.
