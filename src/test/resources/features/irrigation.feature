Feature: Garden Irrigation Advisor

  Scenario: Beregenen bij droogte en weinig regen in historie
    Given de weersverwachting voorspelt 1.0 mm regen
    And de regen historie van afgelopen week is 10.0 mm
    When het dagelijkse advies wordt gegenereerd
    Then moet het advies 30 minuten zijn
    When het dagelijkse advies wordt uitgevoerd
    Then moet de irrigatie zijn uitgevoerd

  Scenario: Niet beregenen bij verwachte regen
    Given de weersverwachting voorspelt 5.0 mm regen
    And de regen historie van afgelopen week is 10.0 mm
    When het dagelijkse advies wordt gegenereerd
    Then moet het advies 0 minuten zijn
    When het dagelijkse advies wordt uitgevoerd
    Then moet de irrigatie niet zijn uitgevoerd

  Scenario: Niet beregenen bij veel regen in historie
    Given de weersverwachting voorspelt 1.0 mm regen
    And de regen historie van afgelopen week is 20.0 mm
    When het dagelijkse advies wordt gegenereerd
    Then moet het advies 0 minuten zijn
    When het dagelijkse advies wordt uitgevoerd
    Then moet de irrigatie niet zijn uitgevoerd
