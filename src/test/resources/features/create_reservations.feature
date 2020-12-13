Feature: create new reservations
  As a camper
  I want to create campsite reservations
  So that I do not conflict with other campers

  Background:
    Given it is '2020-12-01T11:00:00+11:00' now

  Scenario: create a reservation when no reservations exist
  A valid reservation is created when there are no existing reservations at all

    Given there are no existing reservations

    When I create a reservation:
      | arrival date   | 2020-12-24T12:00:00+11:00 |
      | departure date | 2020-12-26T12:00:00+11:00 |
      | email          | jsmith@example.com        |
      | full name      | John Smith                |

    Then the operation is successful
    And a reservation is created:
      | arrival date       | 2020-12-24T12:00:00+11:00 |
      | departure date     | 2020-12-26T12:00:00+11:00 |
      | email              | jsmith@example.com        |
      | full name          | John Smith                |
      | creation timestamp | 2020-12-01T11:00:00+11:00 |

  Scenario: create a reservation when some non-conflicting reservations exist
  A valid reservation is created when it does not conflict with existing reservations

    Given there are some reservations:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
      | 2020-12-23T12:00:00+11:00 | 2020-12-24T12:00:00+11:00 | jdoe@example.com    | John Doe        | 2020-12-01T11:00:00+11:00 |
      | 2020-12-26T12:00:00+11:00 | 2020-12-27T12:00:00+11:00 | another@example.com | Another Example | 2020-12-01T11:00:00+11:00 |

    When I create a reservation:
      | arrival date   | 2020-12-24T12:00:00+11:00 |
      | departure date | 2020-12-26T12:00:00+11:00 |
      | email          | jsmith@example.com        |
      | full name      | John Smith                |

    Then the operation is successful
    And a reservation is created:
      | arrival date       | 2020-12-24T12:00:00+11:00 |
      | departure date     | 2020-12-26T12:00:00+11:00 |
      | email              | jsmith@example.com        |
      | full name          | John Smith                |
      | creation timestamp | 2020-12-01T11:00:00+11:00 |

  Scenario: create a reservation when its start conflicts with an existing reservation
  Reservation fails when its start overlaps with another reservation

    Given there are some reservations:
      | arrival date              | departure date            | email            | full name | creation timestamp        |
      | 2020-12-23T12:00:00+11:00 | 2020-12-26T12:00:00+11:00 | jdoe@example.com | John Doe  | 2020-12-01T11:00:00+11:00 |

    When I create a reservation:
      | arrival date   | 2020-12-25T12:00:00+11:00 |
      | departure date | 2020-12-26T12:00:00+11:00 |
      | email          | jsmith@example.com        |
      | full name      | John Smith                |

    Then the operation fails as a conflict
    And there is an error: "reservation overlaps with an existing one"

  Scenario: create a reservation when its end conflicts with an existing reservation
  Reservation fails when its end overlaps with another reservation

    Given there are some reservations:
      | arrival date              | departure date            | email            | full name | creation timestamp        |
      | 2020-12-23T12:00:00+11:00 | 2020-12-26T12:00:00+11:00 | jdoe@example.com | John Doe  | 2020-12-01T11:00:00+11:00 |

    When I create a reservation:
      | arrival date   | 2020-12-22T12:00:00+11:00 |
      | departure date | 2020-12-24T12:00:00+11:00 |
      | email          | jsmith@example.com        |
      | full name      | John Smith                |

    Then the operation fails as a conflict
    And there is an error: "reservation overlaps with an existing one"

  Scenario: create a reservation when its start and end conflict with an existing reservation
  Reservation fails when it overlaps with another reservation

    Given there are some reservations:
      | arrival date              | departure date            | email            | full name | creation timestamp        |
      | 2020-12-23T12:00:00+11:00 | 2020-12-26T12:00:00+11:00 | jdoe@example.com | John Doe  | 2020-12-01T11:00:00+11:00 |

    When I create a reservation:
      | arrival date   | 2020-12-24T12:00:00+11:00 |
      | departure date | 2020-12-25T12:00:00+11:00 |
      | email          | jsmith@example.com        |
      | full name      | John Smith                |

    Then the operation fails as a conflict
    And there is an error: "reservation overlaps with an existing one"

  Scenario: create a reservation with invalid duration
  A valid reservation is not longer than 3 days

    Given there are no existing reservations

    When I create a reservation:
      | arrival date   | 2020-12-24T12:00:00+11:00 |
      | departure date | 2020-12-28T12:00:00+11:00 |
      | email          | jsmith@example.com        |
      | full name      | John Smith                |

    Then the operation fails as bad request
    And there is an error: "maximum duration is 3 days"


  Scenario: create a reservation which starts too early
  A valid reservation starts at least 1 day after creation date

    Given there are no existing reservations
    And it is '2020-12-01T13:00:00+11:00' now

    When I create a reservation:
      | arrival date   | 2020-12-02T12:00:00+11:00 |
      | departure date | 2020-12-03T12:00:00+11:00 |
      | email          | jsmith@example.com        |
      | full name      | John Smith                |

    Then the operation fails as bad request
    And there is an error: "reservation can be created at least 1 day before arrival time"

  Scenario: create a reservation which starts too late
  A valid reservation starts at most 1 month after creation date

    Given there are no existing reservations
    And it is '2020-12-01T13:00:00+11:00' now

    When I create a reservation:
      | arrival date   | 2021-01-02T12:00:00+11:00 |
      | departure date | 2021-01-03T12:00:00+11:00 |
      | email          | jsmith@example.com        |
      | full name      | John Smith                |

    Then the operation fails as bad request
    And there is an error: "reservation can be created at most 1 month before arrival time"

  Scenario: create a reservation with absent arrival date
  A valid reservation has arrival date

    Given there are no existing reservations
    And it is '2020-12-01T13:00:00+11:00' now

    When I create a reservation:
      | arrival date   |                           |
      | departure date | 2021-01-03T12:00:00+11:00 |
      | email          | jsmith@example.com        |
      | full name      | John Smith                |

    Then the operation fails as bad request
    And there is an error: "arrival date is mandatory"

  Scenario: create a reservation with absent departure date
  A valid reservation has departure date

    Given there are no existing reservations
    And it is '2020-12-01T13:00:00+11:00' now

    When I create a reservation:
      | arrival date   | 2021-01-02T12:00:00+11:00 |
      | departure date |                           |
      | email          | jsmith@example.com        |
      | full name      | John Smith                |

    Then the operation fails as bad request
    And there is an error: "departure date is mandatory"

  Scenario: create a reservation with absent email
  A valid reservation has email

    Given there are no existing reservations
    And it is '2020-12-01T13:00:00+11:00' now

    When I create a reservation:
      | arrival date   | 2021-01-02T12:00:00+11:00 |
      | departure date | 2021-01-03T12:00:00+11:00 |
      | email          |                           |
      | full name      | John Smith                |

    Then the operation fails as bad request
    And there is an error: "email is mandatory"

  Scenario: create a reservation with absent full name
  A valid reservation has full name

    Given there are no existing reservations
    And it is '2020-12-01T13:00:00+11:00' now

    When I create a reservation:
      | arrival date   | 2021-01-02T12:00:00+11:00 |
      | departure date | 2021-01-03T12:00:00+11:00 |
      | email          | jsmith@example.com        |
      | full name      |                           |

    Then the operation fails as bad request
    And there is an error: "full name is mandatory"

  Scenario: create a reservation with all mandatory fields empty
  A valid reservation has all mandatory fields

    Given there are no existing reservations
    And it is '2020-12-01T13:00:00+11:00' now

    When I create a reservation:
      | arrival date   |  |
      | departure date |  |
      | email          |  |
      | full name      |  |

    Then the operation fails as bad request
    And there is an error: "arrival date is mandatory"
    And there is an error: "departure date is mandatory"
    And there is an error: "email is mandatory"
    And there is an error: "full name is mandatory"