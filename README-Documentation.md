

# Event Booking API

Overview
This API allows customers to create, find and reserve tickets for events, view and manage their reservations, and be notified before the event kickoff


### Getting Started

To get started with the Booking API Project, follow these steps:


1. Clone the repository from GitHub:

https://oauth:glpat-2r4Q5zpjfpstAZCfPrFy@gitlab.com/musala_soft/DEV_EVENT_BOOKING-72df017f-52a3-e300-bf23-b205c39a2d3c.git

2. Open the project in your preferred IDE (e.g., IntelliJ IDEA, Eclipse).

3. Make sure you have Java 17 installed.


##### Architecture

The Booking API Project follows a three-layered architecture:

Controller Layer: Handles incoming HTTP requests and delegates them to the appropriate service.

Service Layer: Implements business logic and interacts with the repository layer.

Repository Layer: Interfaces with the database using Spring Data JPA.


##### Usage

API Endpoints
POST /users: Create user
POST /auth: Login user
POST /events: Create new event
POST /events/{eventId}/tickets: Reserve tickets for an event.
POST /events/tickets/cancel: Cancel a reservation.
GET /events/tickets/{ticketId}: View a reservation by ID.


1. POST /users
   Content-Type: application/json

##### Sample Request:

`{
"name" : "username password",
"email" : "username.password@gmail.com",
"password" : "password"
}`

##### Sample Response:
`{
    "userProfile":
    {
        "name":"username password",
        "email":"username.password@gmail.com",
        "token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3YWxlSmFtZXNAZ21haWwuY29tIiwiaWF0IjoxNzA3MDYxMDYwLCJleHAiOjE3MDcwNjQ2NjB9.9YnnhHYqrj1Gpp9AUtOcXzjYGCWjdle467JCXJT5tUw"
    }
}
`


2.  POST /auth
   Content-Type: application/json

##### Sample Request:

`{
"email" : "username.password@gmail.com",
"password" : "password"
}`

##### Sample Response:
`{
    "userProfile":
        {
            "name":"username password",
            "email":"username.password@gmail.com",
            "token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3YWxlSmFtZXNAZ21haWwuY29tIiwiaWF0IjoxNzA3MDYxMDYwLCJleHAiOjE3MDcwNjQ2NjB9.9YnnhHYqrj1Gpp9AUtOcXzjYGCWjdle467JCXJT5tUw"
        }
}
`

3. POST /events
   Content-Type: application/json


##### Sample Request:


`{
"name" : "livespot Expo",
"date" : "2024-02-0",
"availableAttendeesCount" : 1000,
"description" : "December party",  
"category" : "Concert"     
}`

##### Sample Response:
`{
    "eventId" : 1
}`


4. POST /events/{eventId}/tickets
   Content-Type: application/json

##### SampleSample Request:

`
{
    "attendeesCount" : 50
}`

##### Sample Response:
`{
    "ticketId":3
}`


5. POST /events/tickets/cancel
   Content-Type: application/json

##### Sample Request:

`{
"ticketId":3
}`

##### Sample Response:


6.GET /events/tickets/{ticketId}
   Content-Type: application/json

##### Sample Request:

`{
"ticketId":1
}`


##### Sample Response:

`{
"id":1,
"eventId":1,
"attendeesCount":50
}`


7. GET /events
   Content-Type: application/json


##### Sample Request:
`{
"startDate" : "2023-01-01",
"endDate" : "2024-04-19",
"category" : "Concert"
}`

##### Sample Response:

`[
    {
        "id":1,
        "name":"EventBrite",
        "date":"2024-01-04",
        "availableAttendeesCount":100,
        "description":"for kids ",
        "category":"Concert"
    },
    {
        "id":2,
        "name":"livespot Expo",
        "date":"2024-02-04",
        "availableAttendeesCount":1000,
        "description":"December party",
        "category":"Concert"
    }
]`


##### Troubleshooting

Issue: Unable to connect to the database.
Solution: Check database configuration in application.properties and ensure the database server is running.
