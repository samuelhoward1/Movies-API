Movie Database API

A RESTful API built with Spring Boot and JPA for managing a movie database. Supports CRUD operations for movies, genres, and actors, with advanced filtering and search capabilities.
To run this program, ensure you have Java version 21 installed. Clone the repository, navigate to the project directory, and use the following command to start the application:mvn spring-boot:run.

Key Features:
Genre:
To POST a genre go to http://localhost:8080/api/genres and use the following request body.

{
"name": "Action"
}

If the genre already exists you will see an error message saying 'Genre with name 'Action' already exists.'
If the creation was successful you will see a 201 Created status code.

To GET all genres go to http://localhost:8080/api/genres.
To GET a particular genre go to http://localhost:8080/api/genres/{id}.

If the genre doesn't exist you will see an error message 'Genre not found with id {id}.'

To PATCH a genres' name go to http://localhost:8080/api/genres/1?newName=newGenreName.

If the update was successful you should see 200 ok status code.

To DELETE a genre go to http://localhost:8080/api/genres/{id}.

If the deletion was successful you will see a 204 no content status code.
If the genre has relationships you will get an error message like 'Cannot delete genre 'Action' because it has 2 associated movie(s).'

To force deletion with relationships use http://localhost:8080/api/genres/{id}?force=true.

Actor:
To POST an actor use the following request body.

{
"name": "Keanu Reeves",
"birthDate": "1964-09-02",
"movies": [
{
"id": 1
},
{
"id": 2
}
]
}

If the creation was successful you will see a 201 Created status code, you will recieve an error if an actor with that name already exists in the database.

To GET all actors go to http://localhost:8080/api/actors.
To GET actor by name go to http://localhost:8080/api/actors?name=Keanu+Reeves.
To GET a particular actor by id go to http://localhost:8080/api/actors/{id}.
To GET actors by movie http://localhost:8080/api/movies/{id}/actors.

If that actor doesn't exist you will get an error message.

To PATCH an actor use the following request body.

{
"name": "Keanu R",
"birthDate": "1999-09-19",
"movieIds": [3, 4]
}

To DELETE an actor go to http://localhost:8080/api/actors/{id}. 

The logic regarding deleting actors with relationships is analog to how it's done with genres.

Movie:
To POST a movie go to http://localhost:8080/api/movies and use the following request body.

{
"title": "Titanic",
"releaseYear": 2022,
"duration": 120,
"genres": [
{ "id": 1 }
],
"actors": [
{ "id": 1 }
]
}

To GET all movies go to http://localhost:8080/api/movies or /movies/{id} to GET a particular movie.
To GET movies by actor http://localhost:8080/api/movies?actorId={id}.
To GET movies by genre http://localhost:8080/api/genres/{id}/movies.
To GET movies by release year http://localhost:8080/api/movies?year=1999.
To GET movies by name http://localhost:8080/api/movies/search?title=Titanic.

If a movie doesn't exist with that id you will see an error message.

To PATCH a movie go to the following URL movies/{id} and use the same structure you would use for creating a movie.

To DELETE a movie go to movies/{id}, the same logic applies for deleting movies with relationships, so to force delete you must go to http://localhost:8080/api/movies/{id}?force=true.

NB! Feel free to play around with the field values of all entities to see the constraints.















