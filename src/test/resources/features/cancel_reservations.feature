Feature: cancel existing reservations
  As a camper
  I want to cancel reservations
  So that I can free up the campsite if I can not stay


  Scenario: cancel an existing reservation

    Given there are some reservations:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
      | 2020-12-20T12:00:00+11:00 | 2020-12-22T12:00:00+11:00 | jsmith@example.com  | John Smith      | 2020-12-01T11:00:00+11:00 |
      | 2020-12-24T12:00:00+11:00 | 2020-12-25T12:00:00+11:00 | jdoe@example.com    | John Doe        | 2020-12-01T11:00:00+11:00 |
      | 2020-12-25T12:00:00+11:00 | 2020-12-26T12:00:00+11:00 | another@example.com | Another Example | 2020-12-01T11:00:00+11:00 |

    When I cancel the reservation with 'email' equal to 'jdoe@example.com'

    Then the operation is successful
    And there is no reservation with 'email' equal to 'jdoe@example.com'


  Scenario: cancel a non-existing reservation

    Given there are some reservations:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
      | 2020-12-20T12:00:00+11:00 | 2020-12-22T12:00:00+11:00 | jsmith@example.com  | John Smith      | 2020-12-01T11:00:00+11:00 |
      | 2020-12-24T12:00:00+11:00 | 2020-12-25T12:00:00+11:00 | jdoe@example.com    | John Doe        | 2020-12-01T11:00:00+11:00 |
      | 2020-12-25T12:00:00+11:00 | 2020-12-26T12:00:00+11:00 | another@example.com | Another Example | 2020-12-01T11:00:00+11:00 |

    When I cancel reservation with ID 999

    Then the operation fails as not found
    And there is an error: "reservation not found"