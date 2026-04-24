Feature: Garden Irrigation Advisor

  Scenario: Irrigating during drought, low rain and high temperature
    Given the weather forecast predicts 1.0 mm rain and 30.0 degrees
    And the rain history of the past week is 10.0 mm
    When the daily advice is generated
    Then the advice should be 30 minutes
    When the daily advice is executed
    Then the irrigation should have been executed

  Scenario: Irrigating during drought, low rain and mild temperature (not irrigated yesterday)
    Given the weather forecast predicts 1.0 mm rain and 20.0 degrees
    And the rain history of the past week is 10.0 mm
    When the daily advice is generated
    Then the advice should be 15 minutes
    When the daily advice is executed
    Then the irrigation should have been executed

  Scenario: Do not irrigate during drought, low rain and mild temperature when irrigated yesterday
    Given the weather forecast predicts 1.0 mm rain and 20.0 degrees
    And the rain history of the past week is 10.0 mm
    And irrigation was executed yesterday
    When the daily advice is generated
    Then the advice should be 0 minutes
    When the daily advice is executed
    Then the irrigation should not have been executed today

  Scenario: Do not irrigate when rain is expected
    Given the weather forecast predicts 5.0 mm rain
    And the rain history of the past week is 10.0 mm
    When the daily advice is generated
    Then the advice should be 0 minutes
    When the daily advice is executed
    Then the irrigation should not have been executed

  Scenario: Do not irrigate when there was high rain in history
    Given the weather forecast predicts 1.0 mm rain
    And the rain history of the past week is 20.0 mm
    When the daily advice is generated
    Then the advice should be 0 minutes
    When the daily advice is executed
    Then the irrigation should not have been executed
