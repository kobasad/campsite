Feature: : list existing reservation availabilities
  As a camper
  I want to list existing reservation availabilities
  So that I can pick suitable dates for my reservation


  Scenario: list availabilities with default end date when no reservations exist
  If no reservations exist, then the whole period from start date to end date
  is available. Default end date is 1 month from start date.

    Given there are no existing reservations

    When I list availabilities with these criteria:
      | start | 2020-12-01T12:00:00+11:00 |
      | end   |                           |

    Then the operation is successful
    And the following availabilities are listed:
      | start date                | end date                  |
      | 2020-12-01T12:00:00+11:00 | 2021-01-01T12:00:00+11:00 |


  Scenario: list availabilities with specific end date when no reservations exist
  If no reservations exist, then the whole period from start date to end date
  is available.

    Given there are no existing reservations

    When I list availabilities with these criteria:
      | start | 2020-12-01T12:00:00+11:00  |
      | end   | 2020-12-15T12:00:00+11:00  |

    Then the operation is successful
    And the following availabilities are listed:
      | start date                | end date                  |
      | 2020-12-01T12:00:00+11:00 | 2020-12-15T12:00:00+11:00 |


  Scenario: list availabilities with default end date when some reservations exist
  If some reservations exist, then periods between reservations should be returned.
  Default end date is 1 month from start date.

    Given there are some reservations:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
      | 2020-12-03T12:00:00+11:00 | 2020-12-05T12:00:00+11:00 | jsmith@example.com  | John Smith      | 2020-12-01T11:00:00+11:00 |
      | 2020-12-09T12:00:00+11:00 | 2020-12-12T12:00:00+11:00 | jdoe@example.com    | John Doe        | 2020-12-01T11:00:00+11:00 |
      | 2020-12-12T12:00:00+11:00 | 2020-12-14T12:00:00+11:00 | another@example.com | Another Example | 2020-12-01T11:00:00+11:00 |

    When I list availabilities with these criteria:
      | start | 2020-12-01T12:00:00+11:00 |
      | end   |                           |

    Then the operation is successful
    And the following availabilities are listed:
      | start date                | end date                  |
      | 2020-12-01T12:00:00+11:00 | 2020-12-03T12:00:00+11:00 |
      | 2020-12-05T12:00:00+11:00 | 2020-12-09T12:00:00+11:00 |
      | 2020-12-14T12:00:00+11:00 | 2021-01-01T12:00:00+11:00 |

  Scenario: list availabilities with specific end date when some reservations exist

    Given there are some reservations:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
      | 2020-12-03T12:00:00+11:00 | 2020-12-04T12:00:00+11:00 | jsmith@example.com  | John Smith      | 2020-12-01T11:00:00+11:00 |
      | 2020-12-08T12:00:00+11:00 | 2020-12-11T12:00:00+11:00 | jdoe@example.com    | John Doe        | 2020-12-01T11:00:00+11:00 |
      | 2020-12-12T12:00:00+11:00 | 2020-12-14T12:00:00+11:00 | another@example.com | Another Example | 2020-12-01T11:00:00+11:00 |

    When I list availabilities with these criteria:
      | start | 2020-12-01T12:00:00+11:00 |
      | end   | 2020-12-17T12:00:00+11:00 |

    Then the operation is successful
    And the following availabilities are listed:
      | start date                | end date                  |
      | 2020-12-01T12:00:00+11:00 | 2020-12-03T12:00:00+11:00 |
      | 2020-12-04T12:00:00+11:00 | 2020-12-08T12:00:00+11:00 |
      | 2020-12-11T12:00:00+11:00 | 2020-12-12T12:00:00+11:00 |
      | 2020-12-14T12:00:00+11:00 | 2020-12-17T12:00:00+11:00 |

  Scenario: list availabilities with a reservation crossing on the left and right ends

    Given there are some reservations:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
      | 2020-12-01T12:00:00+11:00 | 2020-12-04T12:00:00+11:00 | jsmith@example.com  | John Smith      | 2020-12-01T11:00:00+11:00 |
      | 2020-12-14T12:00:00+11:00 | 2020-12-17T12:00:00+11:00 | jdoe@example.com    | John Doe        | 2020-12-01T11:00:00+11:00 |
      | 2020-12-24T12:00:00+11:00 | 2020-12-31T12:00:00+11:00 | another@example.com | Another Example | 2020-12-01T11:00:00+11:00 |

    When I list availabilities with these criteria:
      | start | 2020-12-02T12:00:00+11:00 |
      | end   | 2020-12-20T12:00:00+11:00 |

    Then the operation is successful
    And the following availabilities are listed:
      | start date                | end date                  |
      | 2020-12-04T12:00:00+11:00 | 2020-12-14T12:00:00+11:00 |
      | 2020-12-17T12:00:00+11:00 | 2020-12-20T12:00:00+11:00 |

  Scenario: list availabilities with a reservation starting and ending 1 day before search period (just in case, you know)
  If some reservations exist, then periods between reservations should be returned.
  Default end date is 1 month from start date.

    Given there are some reservations:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
      | 2020-12-01T12:00:00+11:00 | 2020-12-02T12:00:00+11:00 | jsmith@example.com  | John Smith      | 2020-12-01T11:00:00+11:00 |
      | 2020-12-03T12:00:00+11:00 | 2020-12-05T12:00:00+11:00 | jdoe@example.com    | John Doe        | 2020-12-01T11:00:00+11:00 |
      | 2020-12-08T12:00:00+11:00 | 2020-12-11T12:00:00+11:00 | another@example.com | Another Example | 2020-12-01T11:00:00+11:00 |

    When I list availabilities with these criteria:
      | start | 2020-12-03T12:00:00+11:00 |
      | end   | 2020-12-10T12:00:00+11:00 |

    Then the operation is successful
    And the following availabilities are listed:
      | start date                | end date                  |
      | 2020-12-05T12:00:00+11:00 | 2020-12-08T12:00:00+11:00 |

  Scenario: list availabilities with an invalid start date

    Given there are some reservations:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
      | 2020-12-01T12:00:00+11:00 | 2020-12-02T12:00:00+11:00 | jsmith@example.com  | John Smith      | 2020-12-01T11:00:00+11:00 |
      | 2020-12-03T12:00:00+11:00 | 2020-12-05T12:00:00+11:00 | jdoe@example.com    | John Doe        | 2020-12-01T11:00:00+11:00 |
      | 2020-12-08T12:00:00+11:00 | 2020-12-11T12:00:00+11:00 | another@example.com | Another Example | 2020-12-01T11:00:00+11:00 |

    When I list availabilities with these criteria:
      | start | 2020-13-32T12:00:00+11:00 |
      | end   |                           |

    Then the operation fails as bad request
    And there is an error: "invalid start"

  Scenario: list availabilities with an invalid end date

    Given there are some reservations:
      | arrival date              | departure date            | email               | full name       | creation timestamp        |
      | 2020-12-01T12:00:00+11:00 | 2020-12-02T12:00:00+11:00 | jsmith@example.com  | John Smith      | 2020-12-01T11:00:00+11:00 |
      | 2020-12-03T12:00:00+11:00 | 2020-12-05T12:00:00+11:00 | jdoe@example.com    | John Doe        | 2020-12-01T11:00:00+11:00 |
      | 2020-12-08T12:00:00+11:00 | 2020-12-11T12:00:00+11:00 | another@example.com | Another Example | 2020-12-01T11:00:00+11:00 |

    When I list availabilities with these criteria:
      | start | 2020-12-01T12:00:00+11:00 |
      | end   | 2020-13-32T12:00:00+11:00 |

    Then the operation fails as bad request
    And there is an error: "invalid end"
