Feature: Garden Irrigation Advisor

  Scenario: Irrigating during drought and low rain in history
    Given the weather forecast predicts 1.0 mm rain
    And the rain history of the past week is 10.0 mm
    When the daily advice is generated
    Then the advice should be 30 minutes
    When the daily advice is executed
    Then the irrigation should have been executed

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
