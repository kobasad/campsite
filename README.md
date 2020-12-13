# Introduction
This repository contains source code of a web application that is used to manage campsite reservations. The application runs as a REST web service exposing a number of API endpoints:
* Create a new reservation
* Update an existing reservation
* List existing reservations
* List availability slots for a given search period
* Delete an existing reservation

# Implementation details
The app is built using _Spring Boot_ and _Postgres_. _Liquibase_ is used to execute database schema migrations. Test framework uses _Cucumber_ and _Testcontainers_.

## Conflict resolution
In order to prevent creating overlapping reservations, a database constraint on a timestamp range is used so that no reservations can be stored with overlapping arrival and departure date ranges.

## Development process
Behavior Driven Development approach was used to develop this application:
1. Write a failing test scenario
2. Write enough code to make the test pass
3. Refactor existing code
4. Repeat

Test scenarios can be found in `src/test/resources/features` directory. Scenarios are written and executed using _Cucumber_ framework. In test environment, the application runs against a _Testcontainers Postgres_ database instance. Each scenario sets test data in the database before running API requests. Thus, a test instance of the application represents almost fully integrated application with `MockMvc` used instead of `DispatcherServlet` to handle HTTP requests.
Cucumber report listing all scenarios and status of their execution can be found in `target/cucumber-report/index.html`.

## Leftovers
* Almost no logging was implemented, which is a must-have for a production-ready application.
* API documentation is missing, Swagger could be used to generate it.

# Running the application
## Prerequisites
The following dependencies should be installed in order to run the application and send API requests:
* openjdk 11
* maven 3
* Docker
* curl
## Build docker image
1. Build application JAR:
    ```shell script
    mvn [clean] package
    ```
2. Build Docker image:
    ```shell script
    docker build -t campsite .
    ```
## Run the application and the database
```
docker-compose up
```
## Execute some API calls
1. Create some reservations:
    ```shell script
    curl -H 'Content-Type: application/json' -d '{"arrivalDate": "2020-12-24T12:00:00+11:00", "departureDate": "2020-12-26T12:00:00+11:00", "email": "jsmith@example.com", "fullName": "John Smith"}' localhost:8080/v1/reservations
    curl -H 'Content-Type: application/json' -d '{"arrivalDate": "2020-12-27T12:00:00+11:00", "departureDate": "2020-12-29T12:00:00+11:00", "email": "jdoe@example.com", "fullName": "John Doe"}' localhost:8080/v1/reservations
    ```
2.  List existing reservations:
    ```shell script
    curl localhost:8080/v1/reservations
    ```
3. List reservation availabilities:
    ```shell script
    curl -G localhost:8080/v1/availabilities --data-urlencode "start=2020-12-01T12:00:00+11:00"
    ```
4. Update an existing reservation
    ```shell script
    curl -X PUT -H 'Content-Type: application/json' localhost:8080/v1/reservations/1 -d '
    {
      "id": 1,
      "arrivalDate": "2020-12-24T01:00:00Z",
      "departureDate": "2020-12-27T01:00:00Z",
      "email": "jsmith@example.com",
      "fullName": "John Smith",
      "createdAt": "2020-12-13T21:35:37.926073Z"
    }'
    ```
5. Delete an existing reservation:
    ```shell script
    curl -X DELETE localhost:8080/v1/reservations/2
    ```
