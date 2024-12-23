# Movie Database API

### A RESTful API built with Spring Boot and JPA for managing a movie database.

## Overview
The Movie Database API is a RESTful application built with Spring Boot and JPA for managing movies, genres, and actors.

It uses entities for the database model, repositories for data access, and services for business logic.

Controllers define endpoints for CRUD operations, filtering, and search.

The API supports many-to-many relationships between movies, genres, and actors, with SQLite as the database.


## Installation
### Clone repository to local machine:
`git clone https://gitea.kood.tech/samuelhoward/kmdb.git`

### Make sure to have Maven installed:
`mvn -v`

### Make sure to have Java 21:
`java -v`

### Navigate to movies-api and run:
`mvn clean install`

`mvn spring-boot:run`

### Install and open Postman, click on the import button and upload the Movie-Database-API.json collection and start testing.

## Usage

| **Entity** | **Action**       | **Endpoint**                                       | **Method** | **Details**                                                                                  |
|------------|------------------|---------------------------------------------------|------------|----------------------------------------------------------------------------------------------|
| Genre      | Create           | `/api/genres`                                     | POST       | Request Body: `{ "name": "Action" }`. Error if genre already exists.                        |
|            | Retrieve All     | `/api/genres`                                     | GET        | Retrieves all genres.                                                                        |
|            | Retrieve One     | `/api/genres/{id}`                                | GET        | Error if genre not found.                                                                   |
|            | Update           | `/api/genres/{id}?newName=newGenreName`           | PATCH      | Updates genre name.                                                                          |
|            | Delete           | `/api/genres/{id}`                                | DELETE     | Error if relationships exist. Force delete with `?force=true`.                              |
| Actor      | Create           | `/api/actors`                                     | POST       | Request Body: `{ "name": "Keanu Reeves", "birthDate": "1964-09-02", "movies": [{ "id": 1 }] }`. |
|            | Retrieve All     | `/api/actors`                                     | GET        | Retrieves all actors.                                                                       |
|            | Retrieve by Name | `/api/actors?name=Keanu+Reeves`                   | GET        | Filters actors by name.                                                                     |
|            | Retrieve by ID   | `/api/actors/{id}`                                | GET        | Retrieves a specific actor by ID.                                                           |
|            | Retrieve by Movie| `/api/movies/{id}/actors`                         | GET        | Retrieves actors starring in a specific movie.                                              |
|            | Update           | `/api/actors/{id}`                                | PATCH      | Request Body: `{ "name": "Keanu R", "birthDate": "1999-09-19", "movieIds": [3, 4] }`.       |
|            | Delete           | `/api/actors/{id}`                                | DELETE     | Error if relationships exist. Force delete with `?force=true`.                              |
| Movie      | Create           | `/api/movies`                                     | POST       | Request Body: `{ "title": "Titanic", "releaseYear": 2022, "duration": 120, "genres": [{ "id": 1 }], "actors": [{ "id": 1 }] }`. |
|            | Retrieve All     | `/api/movies`                                     | GET        | Retrieves all movies.                                                                       |
|            | Retrieve One     | `/api/movies/{id}`                                | GET        | Retrieves a specific movie by ID.                                                           |
|            | Retrieve by Actor| `/api/movies?actorId={id}`                        | GET        | Retrieves movies by actor ID.                                                               |
|            | Retrieve by Genre| `/api/genres/{id}/movies`                         | GET        | Retrieves movies by genre ID.                                                               |
|            | Retrieve by Year | `/api/movies?year={releaseYear}`                  | GET        | Retrieves movies by release year.                                                           |
|            | Search by Title  | `/api/movies/search?title={title}`                | GET        | Searches movies by title (case-insensitive).                                                |
|            | Update           | `/api/movies/{id}`                                | PATCH      | Same request body structure as `POST`.                                                     |
|            | Delete           | `/api/movies/{id}`                                | DELETE     | Error if relationships exist. Force delete with `?force=true`.                              |

## PS! For pagination add page and size to query parameters.


















