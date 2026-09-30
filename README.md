# Number Guessing Game: The "Reverse Psychology" AI (Spring Boot)

A console number guessing game built with **Spring Boot 3 / Java 17**. The computer picks a secret number and gives
"Too high" / "Too low" hints, but **about 10% of the time it lies**. You have to work out when the system is
malfunctioning.

Task 4 of the Arithmatrix Virtual Internship Program (AVIP) 2026, Software Developer track.

## Features

- Random secret number within a configurable range (`game.min` / `game.max`)
- Hints for each guess plus an attempt counter
- **Reverse psychology twist:** each wrong guess has a 10% chance (`game.lie-chance`) of getting a flipped hint.
  A correct guess is always reported honestly.
- Final summary: the correct number, attempts taken, and which guesses the computer lied on
- Input validation: non-numeric input and out-of-range guesses are rejected without costing an attempt
- Difficulty levels: `easy` (1-10), `medium` (1-100), `hard` (1-1000), or a custom range
- Type `q` to give up (the number is revealed)
- Unit tests for the hint and lie logic

## Requirements

- Java 17+
- Maven 3.9+

## Build and run

```bash
mvn clean package
java -jar target/number-guessing-game-1.0.0.jar
```

Or run straight from source:

```bash
mvn spring-boot:run
```

### Configuration examples

```bash
# Difficulty preset
java -jar target/number-guessing-game-1.0.0.jar --game.difficulty=hard

# Custom range
java -jar target/number-guessing-game-1.0.0.jar --game.min=1 --game.max=500

# Change the lie probability (0.0 = honest, 1.0 = always lies)
java -jar target/number-guessing-game-1.0.0.jar --game.lie-chance=0.25

# With Maven
mvn spring-boot:run -Dspring-boot.run.arguments="--game.difficulty=easy"
```

| Property           | Default | Description                                   |
|--------------------|---------|-----------------------------------------------|
| `game.min`         | `1`     | Lowest number                                 |
| `game.max`         | `100`   | Highest number                                |
| `game.lie-chance`  | `0.10`  | Probability that a wrong-guess hint is a lie  |
| `game.difficulty`  | (empty) | `easy`, `medium` or `hard`; overrides min/max |

Defaults live in `src/main/resources/application.properties`.

## Example playthrough

Format of a typical session (your run will differ, since the number is random):

```
=== Number Guessing Game: Reverse Psychology Edition ===
I'm thinking of a number between 1 and 100.
Warning: I might malfunction and lie about 10% of the time. Stay sharp!
Type 'q' to give up.

Guess #1 (1-100): 50
Too high!
Guess #2 (1-100): abc
That's not a whole number. Try again.
Guess #2 (1-100): 250
Out of range! Pick a number between 1 and 100.
Guess #2 (1-100): 25
Too low!
Guess #3 (1-100): 37
Too high!
Guess #4 (1-100): 31
Correct!

=== Summary ===
The number was : 31
Attempts taken : 4
Lies told      : 1 (on guess [2])
```

See `docs/` for a real transcript and screenshots/GIF of gameplay.

## Project structure

```
src/main/java/dev/rafay/guessinggame/
    GuessingGameApplication.java   entry point
    GameProperties.java            configuration binding + difficulty presets
    GameService.java               secret number + hint / lie logic
    GameRunner.java                interactive console loop
src/test/java/dev/rafay/guessinggame/
    GameServiceTest.java
```

## Run the tests

```bash
mvn test
```
