package dev.rafay.guessinggame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

class GameServiceTest {

    @Test
    void neverLiesWhenChanceIsZero() {
        GameService service = new GameService(new Random(1), 0.0);
        for (int i = 0; i < 1000; i++) {
            assertFalse(service.evaluate(80, 50).lied());
            assertEquals(GameService.Hint.TOO_HIGH, service.evaluate(80, 50).shown());
            assertEquals(GameService.Hint.TOO_LOW, service.evaluate(20, 50).shown());
        }
    }

    @Test
    void alwaysFlipsWhenChanceIsOne() {
        GameService service = new GameService(new Random(1), 1.0);
        GameService.Result high = service.evaluate(80, 50);
        assertTrue(high.lied());
        assertEquals(GameService.Hint.TOO_LOW, high.shown());
        GameService.Result low = service.evaluate(20, 50);
        assertTrue(low.lied());
        assertEquals(GameService.Hint.TOO_HIGH, low.shown());
    }

    @Test
    void correctGuessIsNeverALie() {
        GameService service = new GameService(new Random(1), 1.0);
        GameService.Result result = service.evaluate(50, 50);
        assertFalse(result.lied());
        assertEquals(GameService.Hint.CORRECT, result.shown());
    }

    @Test
    void lieRateIsRoughlyTenPercent() {
        GameService service = new GameService(new Random(42), 0.10);
        int trials = 100_000;
        int lies = 0;
        for (int i = 0; i < trials; i++) {
            if (service.evaluate(80, 50).lied()) {
                lies++;
            }
        }
        double rate = (double) lies / trials;
        assertTrue(rate > 0.09 && rate < 0.11, "lie rate was " + rate);
    }

    @Test
    void secretStaysWithinRange() {
        GameService service = new GameService(new Random(7), 0.1);
        for (int i = 0; i < 10_000; i++) {
            int secret = service.pickSecret(5, 15);
            assertTrue(secret >= 5 && secret <= 15);
        }
    }

    @Test
    void difficultyPresetsOverrideRange() {
        GameProperties hard = new GameProperties(1, 100, 0.1, "HARD");
        assertEquals(1, hard.rangeMin());
        assertEquals(1000, hard.rangeMax());
        GameProperties custom = new GameProperties(10, 20, 0.1, "");
        assertEquals(10, custom.rangeMin());
        assertEquals(20, custom.rangeMax());
    }
}
