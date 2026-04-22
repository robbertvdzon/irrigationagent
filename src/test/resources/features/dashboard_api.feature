Feature: Dashboard API

  Scenario: Retrieve all dashboard data in one call
    Given the following forecasts:
      | days_ago | rain_mm |
      | -1       | 2.0     |
      | 0        | 1.5     |
    And the following rain history:
      | days_ago | rain_mm |
      | 1        | 10.0    |
      | 2        | 5.0     |
    And the following irrigation advices:
      | days_ago | minutes | status  |
      | 0        | 30      | PENDING |
      | 1        | 45      | EXECUTED|
    And the following irrigation events:
      | days_ago | minutes | status    |
      | 0        | 30      | COMPLETED |
    When I call the dashboard data API
    Then the response should match:
      | path                      | value     |
      | $.forecasts[0].rainExpectedMm | 2.0       |
      | $.forecasts[1].rainExpectedMm | 1.5       |
      | $.history[0].rainMm       | 10.0      |
      | $.history[1].rainMm       | 5.0       |
      | $.advice.durationMinutes  | 30        |
      | $.advices[0].durationMinutes | 30     |
      | $.advices[1].durationMinutes | 45     |
      | $.events[0].durationMinutes | 30      |
