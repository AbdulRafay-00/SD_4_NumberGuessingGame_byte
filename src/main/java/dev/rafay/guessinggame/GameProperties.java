package dev.rafay.guessinggame;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configurable settings, bound from application.properties or command-line
 * arguments such as {@code --game.difficulty=hard} or {@code --game.max=500}.
 */
@ConfigurationProperties(prefix = "game")
public record GameProperties(
        @DefaultValue("1") int min,
        @DefaultValue("100") int max,
        @DefaultValue("0.10") double lieChance,
        @DefaultValue("") String difficulty) {

    /** Lowest number in play, taking the difficulty preset into account. */
    public int rangeMin() {
        return switch (normalizedDifficulty()) {
            case "easy", "medium", "hard" -> 1;
            case "" -> min;
            default -> throw unknownDifficulty();
        };
    }

    /** Highest number in play, taking the difficulty preset into account. */
    public int rangeMax() {
        return switch (normalizedDifficulty()) {
            case "easy" -> 10;
            case "medium" -> 100;
            case "hard" -> 1000;
            case "" -> max;
            default -> throw unknownDifficulty();
        };
    }

    private String normalizedDifficulty() {
        return difficulty == null ? "" : difficulty.trim().toLowerCase();
    }

    private IllegalArgumentException unknownDifficulty() {
        return new IllegalArgumentException(
                "Unknown difficulty '" + difficulty + "'. Use easy, medium or hard.");
    }
}
