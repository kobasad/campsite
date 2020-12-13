Feature: update existing reservations
  As a camper
  I want to modify existing reservations
  So that I am able to correct them

  Background:
    Given it is '2020-12-01T11:00:00+11:00' now


  Scenario: change start date when no conflicting reservations exist

    Given there are some reservations:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
      | 2020-12-20T12:00:00+11:00 | 2020-12-22T12:00:00+11:00 | jsmith@example.com  | John Smith      | 2020-12-01T11:00:00+11:00 |
      | 2020-12-24T12:00:00+11:00 | 2020-12-25T12:00:00+11:00 | jdoe@example.com    | John Doe        | 2020-12-01T11:00:00+11:00 |
      | 2020-12-25T12:00:00+11:00 | 2020-12-26T12:00:00+11:00 | another@example.com | Another Example | 2020-12-01T11:00:00+11:00 |

    When I update the reservation with 'email' equal to 'jdoe@example.com':
      | arrival date   | 2020-12-22T12:00:00+11:00 |
      | departure date | 2020-12-24T12:00:00+11:00 |
      | email          | jdoe@example.com          |
      | full name      | John Doe                  |

    Then the operation is successful
    And a reservation is updated:
      | arrival date       | 2020-12-22T12:00:00+11:00 |
      | departure date     | 2020-12-24T12:00:00+11:00 |
      | email              | jdoe@example.com          |
      | full name          | John Doe                  |
      | creation timestamp | 2020-12-01T11:00:00+11:00 |

  Scenario: change end date when no conflicting reservations exist

    Given there are some reservations:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
      | 2020-12-20T12:00:00+11:00 | 2020-12-22T12:00:00+11:00 | jsmith@example.com  | John Smith      | 2020-12-01T11:00:00+11:00 |
      | 2020-12-22T12:00:00+11:00 | 2020-12-23T12:00:00+11:00 | jdoe@example.com    | John Doe        | 2020-12-01T11:00:00+11:00 |
      | 2020-12-25T12:00:00+11:00 | 2020-12-26T12:00:00+11:00 | another@example.com | Another Example | 2020-12-01T11:00:00+11:00 |

    When I update the reservation with 'email' equal to 'jdoe@example.com':
      | arrival date   | 2020-12-22T12:00:00+11:00 |
      | departure date | 2020-12-25T12:00:00+11:00 |
      | email          | jdoe@example.com          |
      | full name      | John Doe                  |

    Then the operation is successful
    And a reservation is updated:
      | arrival date       | 2020-12-22T12:00:00+11:00 |
      | departure date     | 2020-12-25T12:00:00+11:00 |
      | email              | jdoe@example.com          |
      | full name          | John Doe                  |
      | creation timestamp | 2020-12-01T11:00:00+11:00 |

  Scenario: change start and end dates when no conflicting reservations exist

    Given there are some reservations:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
      | 2020-12-20T12:00:00+11:00 | 2020-12-22T12:00:00+11:00 | jsmith@example.com  | John Smith      | 2020-12-01T11:00:00+11:00 |
      | 2020-12-23T12:00:00+11:00 | 2020-12-24T12:00:00+11:00 | jdoe@example.com    | John Doe        | 2020-12-01T11:00:00+11:00 |
      | 2020-12-25T12:00:00+11:00 | 2020-12-26T12:00:00+11:00 | another@example.com | Another Example | 2020-12-01T11:00:00+11:00 |

    When I update the reservation with 'email' equal to 'jdoe@example.com':
      | arrival date   | 2020-12-22T12:00:00+11:00 |
      | departure date | 2020-12-25T12:00:00+11:00 |
      | email          | jdoe@example.com          |
      | full name      | John Doe                  |

    Then the operation is successful
    And a reservation is updated:
      | arrival date       | 2020-12-22T12:00:00+11:00 |
      | departure date     | 2020-12-25T12:00:00+11:00 |
      | email              | jdoe@example.com          |
      | full name          | John Doe                  |
      | creation timestamp | 2020-12-01T11:00:00+11:00 |

  Scenario: change start date when a conflicting reservation exists

    Given there are some reservations:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
      | 2020-12-20T12:00:00+11:00 | 2020-12-22T12:00:00+11:00 | jsmith@example.com  | John Smith      | 2020-12-01T11:00:00+11:00 |
      | 2020-12-23T12:00:00+11:00 | 2020-12-24T12:00:00+11:00 | jdoe@example.com    | John Doe        | 2020-12-01T11:00:00+11:00 |

    When I update the reservation with 'email' equal to 'jdoe@example.com':
      | arrival date   | 2020-12-21T12:00:00+11:00 |
      | departure date | 2020-12-24T12:00:00+11:00 |
      | email          | jdoe@example.com          |
      | full name      | John Doe                  |

    Then the operation fails as a conflict
    And there is an error: "reservation overlaps with an existing one"

  Scenario: change end date when a conflicting reservation exists

    Given there are some reservations:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
      | 2020-12-24T12:00:00+11:00 | 2020-12-25T12:00:00+11:00 | jdoe@example.com    | John Doe        | 2020-12-01T11:00:00+11:00 |
      | 2020-12-25T12:00:00+11:00 | 2020-12-26T12:00:00+11:00 | another@example.com | Another Example | 2020-12-01T11:00:00+11:00 |

    When I update the reservation with 'email' equal to 'jdoe@example.com':
      | arrival date   | 2020-12-24T12:00:00+11:00 |
      | departure date | 2020-12-26T12:00:00+11:00 |
      | email          | jdoe@example.com          |
      | full name      | John Doe                  |

    Then the operation fails as a conflict
    And there is an error: "reservation overlaps with an existing one"

  Scenario: change an existing reservation: set empty arrival date

    Given there are some reservations:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
      | 2020-12-24T12:00:00+11:00 | 2020-12-25T12:00:00+11:00 | jdoe@example.com    | John Doe        | 2020-12-01T11:00:00+11:00 |

    When I update the reservation with 'email' equal to 'jdoe@example.com':
      | arrival date   |                           |
      | departure date | 2020-12-26T12:00:00+11:00 |
      | email          | jdoe@example.com          |
      | full name      | John Doe                  |

    Then the operation fails as bad request
    And there is an error: "arrival date is mandatory"


  Scenario: change an existing reservation: set empty departure date

    Given there are some reservations:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
      | 2020-12-24T12:00:00+11:00 | 2020-12-25T12:00:00+11:00 | jdoe@example.com    | John Doe        | 2020-12-01T11:00:00+11:00 |

    When I update the reservation with 'email' equal to 'jdoe@example.com':
      | arrival date   | 2020-12-24T12:00:00+11:00 |
      | departure date |                           |
      | email          | jdoe@example.com          |
      | full name      | John Doe                  |

    Then the operation fails as bad request
    And there is an error: "departure date is mandatory"

  Scenario: change an existing reservation: set empty email

    Given there are some reservations:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
      | 2020-12-24T12:00:00+11:00 | 2020-12-25T12:00:00+11:00 | jdoe@example.com    | John Doe        | 2020-12-01T11:00:00+11:00 |

    When I update the reservation with 'email' equal to 'jdoe@example.com':
      | arrival date   | 2020-12-24T12:00:00+11:00 |
      | departure date | 2020-12-25T12:00:00+11:00 |
      | email          |                           |
      | full name      | John Doe                  |

    Then the operation fails as bad request
    And there is an error: "email is mandatory"

  Scenario: change an existing reservation: set empty full name

    Given there are some reservations:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
      | 2020-12-24T12:00:00+11:00 | 2020-12-25T12:00:00+11:00 | jdoe@example.com    | John Doe        | 2020-12-01T11:00:00+11:00 |

    When I update the reservation with 'email' equal to 'jdoe@example.com':
      | arrival date   | 2020-12-24T12:00:00+11:00 |
      | departure date | 2020-12-25T12:00:00+11:00 |
      | email          | jdoe@example.com          |
      | full name      |                           |

    Then the operation fails as bad request
    And there is an error: "full name is mandatory"

  Scenario: change an existing reservation: set all fields empty

    Given there are some reservations:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
      | 2020-12-24T12:00:00+11:00 | 2020-12-25T12:00:00+11:00 | jdoe@example.com    | John Doe        | 2020-12-01T11:00:00+11:00 |

    When I update the reservation with 'email' equal to 'jdoe@example.com':
      | arrival date   |                           |
      | departure date |                           |
      | email          |                           |
      | full name      |                           |

    Then the operation fails as bad request
    And there is an error: "arrival date is mandatory"
    And there is an error: "departure date is mandatory"
    And there is an error: "email is mandatory"
    And there is an error: "full name is mandatory"


  Scenario: change an non-existing reservation

    Given there are no existing reservations

    When I update the reservation with ID equal to 1:
      | arrival date   | 2020-12-24T12:00:00+11:00 |
      | departure date | 2020-12-26T12:00:00+11:00 |
      | email          | jdoe@example.com          |
      | full name      | John Doe                  |

    Then the operation fails as not found
    And there is an error: "reservation not found"