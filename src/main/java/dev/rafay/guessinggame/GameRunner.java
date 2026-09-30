package dev.rafay.guessinggame;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Interactive console loop. Runs once at startup. */
@Component
public class GameRunner implements CommandLineRunner {

    private static final int LIMIT = 1_000_000_000;

    private final GameProperties properties;
    private final GameService gameService;

    public GameRunner(GameProperties properties, GameService gameService) {
        this.properties = properties;
        this.gameService = gameService;
    }

    @Override
    public void run(String... args) {
        int min;
        int max;
        try {
            min = properties.rangeMin();
            max = properties.rangeMax();
        } catch (IllegalArgumentException e) {
            System.err.println("Configuration error: " + e.getMessage());
            return;
        }
        if (min >= max || Math.abs(min) > LIMIT || Math.abs(max) > LIMIT) {
            System.err.println("Configuration error: game.min must be lower than game.max "
                    + "(and both within +/-" + LIMIT + ").");
            return;
        }
        if (properties.lieChance() < 0 || properties.lieChance() > 1) {
            System.err.println("Configuration error: game.lie-chance must be between 0 and 1.");
            return;
        }

        int secret = gameService.pickSecret(min, max);
        int attempts = 0;
        List<Integer> lieAttempts = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Number Guessing Game: Reverse Psychology Edition ===");
        System.out.printf("I'm thinking of a number between %d and %d.%n", min, max);
        System.out.printf("Warning: I might malfunction and lie about %d%% of the time. Stay sharp!%n",
                Math.round(properties.lieChance() * 100));
        System.out.println("Type 'q' to give up.\n");

        while (true) {
            System.out.printf("Guess #%d (%d-%d): ", attempts + 1, min, max);
            if (!scanner.hasNextLine()) {
                System.out.println("\nInput closed. Goodbye!");
                return;
            }
            String line = scanner.nextLine().trim();

            if (line.equalsIgnoreCase("q") || line.equalsIgnoreCase("quit")) {
                System.out.printf("You gave up after %d attempt(s). The number was %d.%n", attempts, secret);
                return;
            }

            int guess;
            try {
                guess = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("That's not a whole number. Try again.");
                continue;
            }
            if (guess < min || guess > max) {
                System.out.printf("Out of range! Pick a number between %d and %d.%n", min, max);
                continue;
            }

            attempts++;
            GameService.Result result = gameService.evaluate(guess, secret);
            System.out.println(result.shown().message());
            if (result.lied()) {
                lieAttempts.add(attempts);
            }
            if (result.shown() == GameService.Hint.CORRECT) {
                break;
            }
        }

        System.out.println("\n=== Summary ===");
        System.out.println("The number was : " + secret);
        System.out.println("Attempts taken : " + attempts);
        if (lieAttempts.isEmpty()) {
            System.out.println("Lies told      : 0 (the system behaved this time)");
        } else {
            System.out.println("Lies told      : " + lieAttempts.size() + " (on guess " + lieAttempts + ")");
        }
    }
}
