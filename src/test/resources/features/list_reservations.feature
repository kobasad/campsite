Feature: list reservations
  As a campsite administrator
  I want to see existing reservations
  So that I know what reservations exist

  Scenario: list reservations when some reservations exist
    Given there are some reservations:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
      | 2020-12-20T12:00:00+11:00 | 2020-12-22T12:00:00+11:00 | jsmith@example.com  | John Smith      | 2020-12-01T11:00:00+11:00 |
      | 2020-12-24T12:00:00+11:00 | 2020-12-25T12:00:00+11:00 | jdoe@example.com    | John Doe        | 2020-12-01T11:00:00+11:00 |
      | 2020-12-25T12:00:00+11:00 | 2020-12-26T12:00:00+11:00 | another@example.com | Another Example | 2020-12-01T11:00:00+11:00 |

    When I list reservations

    Then the operation is successful
    And the following reservations are listed:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
      | 2020-12-20T12:00:00+11:00 | 2020-12-22T12:00:00+11:00 | jsmith@example.com  | John Smith      | 2020-12-01T11:00:00+11:00 |
      | 2020-12-24T12:00:00+11:00 | 2020-12-25T12:00:00+11:00 | jdoe@example.com    | John Doe        | 2020-12-01T11:00:00+11:00 |
      | 2020-12-25T12:00:00+11:00 | 2020-12-26T12:00:00+11:00 | another@example.com | Another Example | 2020-12-01T11:00:00+11:00 |

  Scenario: list reservations when no reservations exist
    Given there are no existing reservations

    When I list reservations

    Then the operation is successful
    And the following reservations are listed:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
