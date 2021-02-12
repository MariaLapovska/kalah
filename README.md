# Kalah game

This project is a simple implementation of [Kalah game](https://en.wikipedia.org/wiki/Kalah). 
It is a RESTful Web Service written in Java. 

**IMPORTANT NOTE:** This project uses [H2 database](https://www.h2database.com/html/main.html) in in-memory mode, 
so all the game progress will be stored only during the application runtime.

## Kalah Rules

Each of the two players has six pits in front of him/her. To the right of the six pits, each player has a larger pit, his
Kalah or house.

At the start of the game, six stones are put in each pit.

The player who begins picks up all the stones in any of their own pits, and sows the stones on to the right, one in
each of the following pits, including his own Kalah. No stones are put in the opponent's' Kalah. If the players last
stone lands in his own Kalah, he gets another turn. This can be repeated any number of times before it's the other
player's turn.

When the last stone lands in an own empty pit, the player captures this stone and all stones in the opposite pit (the
other players' pit) and puts them in his own Kalah.
The game is over as soon as one of the sides run out of stones. The player who still has stones in his/her pits keeps
them and puts them in his/hers Kalah. The winner of the game is the player who has the most stones in his Kalah.

## Prerequisites

- [JDK version 11](https://jdk.java.net/11/) or higher
- [Maven version 3.6.3](https://maven.apache.org/download.cgi) or higher

## Run

To build the project execute this command while in [kalah](../kalah) folder:

```bash
mvn clean install -DskipTests
```

Then start the application with:

```bash
java -jar target/kalah-0.0.1-SNAPSHOT.jar
```

By default, the application will start on the `8080` port.

Alternatively, you can run the project in docker container using [Dockerfile](../kalah/Dockerfile) with the following commands:

```
docker build -t kalah .
docker run run -p 8080:8080 kalah
```

## API docs

Swagger UI is available by this link - [Swagger UI](http://localhost:8080/swagger-ui/#)

## Parameters

You can customize the following parameters while running the project:

- **PORT** - port that app will be running on, _8080 by default_
- **PROTOCOL** - protocol used by the app, is used for game link generation, _http by default_
- **HOST** - host used by the app, is used for game link generation, _localhost by default_
- **GAME_PATH** - path to the `games` endpoint, is used for game link generation, _games by default_
- **STONES_NUM** - number of stones in each pit, **should be between 3 and 6**, _6 by default_
- **BOARD_SIZE** - size of the Kalah board, **should be an even number**, _14 by default_
