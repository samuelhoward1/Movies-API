Postman reqbody to post movie

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

To create genre

{
"name": "Thriller"
}

Postman url to patch movie

http://localhost:8080/api/movies/48

Postman URL to patch genre name

http://localhost:8080/api/genres/4?newName=Documentaryy

To get all movies

http://localhost:8080/api/movies

To get movies by genre

http://localhost:8080/api/genres/2/movies

To get movies by actor

http://localhost:8080/api/movies?actor=1

To get movies by release year

http://localhost:8080/api/movies?year=1999

To get actors by movie

http://localhost:8080/api/movies/36/actors

To create actor 

{
"name": "Leonardo DiCaprio",
"birthDate": "1964-09-02",
"movies": [
{
"id": 23
},
{
"id": 25
}
]
}






