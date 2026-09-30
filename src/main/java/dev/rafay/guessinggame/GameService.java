package dev.rafay.guessinggame;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Core game logic: picks the secret number and produces hints.
 * With probability {@code lieChance} the hint is deliberately flipped.
 */
@Service
public class GameService {

    public enum Hint {
        TOO_HIGH("Too high!"),
        TOO_LOW("Too low!"),
        CORRECT("Correct!");

        private final String message;

        Hint(String message) {
            this.message = message;
        }

        public String message() {
            return message;
        }
    }

    /** The hint shown to the player and whether it was a lie. */
    public record Result(Hint shown, boolean lied) {
    }

    private final Random random;
    private final double lieChance;

    @Autowired
    public GameService(GameProperties properties) {
        this(new Random(), properties.lieChance());
    }

    // Package-private: lets tests inject a seeded Random.
    GameService(Random random, double lieChance) {
        this.random = random;
        this.lieChance = lieChance;
    }

    /** Picks a secret number in [min, max] inclusive. */
    public int pickSecret(int min, int max) {
        return random.nextInt(min, max + 1);
    }

    /**
     * Evaluates a guess. A correct guess is always reported honestly; a wrong
     * guess gets a flipped hint with probability {@code lieChance}.
     */
    public Result evaluate(int guess, int secret) {
        if (guess == secret) {
            return new Result(Hint.CORRECT, false);
        }
        Hint truth = guess > secret ? Hint.TOO_HIGH : Hint.TOO_LOW;
        if (random.nextDouble() < lieChance) {
            Hint lie = truth == Hint.TOO_HIGH ? Hint.TOO_LOW : Hint.TOO_HIGH;
            return new Result(lie, true);
        }
        return new Result(truth, false);
    }
}
