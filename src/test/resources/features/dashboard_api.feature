Feature: Dashboard API

  Scenario: Retrieve all dashboard data in one call
    Given the following forecasts:
      | days_ago | rain_mm | max_temp |
      | -1       | 2.0     | 25.0     |
      | 0        | 1.5     | 22.0     |
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
    Then the following forecasts is recieved in the response:
      | days_ago | rain_mm | max_temp |
      | -1       | 2.0     | 25.0     |
      | 0        | 1.5     | 22.0     |
    And the following rain history is recieved in the response:
      | days_ago | rain_mm |
      | 1        | 10.0    |
      | 2        | 5.0     |
    And the following today advice is recieved in the response:
      | minutes | status  |
      | 30      | PENDING |
    And the following advices is recieved in the response:
      | days_ago | minutes | status  |
      | 0        | 30      | PENDING |
      | 1        | 45      | EXECUTED|
    And the following events is recieved in the response:
      | days_ago | minutes | status    |
      | 0        | 30      | COMPLETED |
