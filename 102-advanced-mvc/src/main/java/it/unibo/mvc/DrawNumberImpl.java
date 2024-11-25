package it.unibo.mvc;

import java.util.Random;

/**
 *
 */
public final class DrawNumberImpl implements DrawNumber {

    private int choice;
    private final int min;
    private final int max;
    private final int attempts;
    private int remainingAttempts;
    private final Random random = new Random();

    /**
     * @param min      min number of attempts
     * @param max      max number of attempts
     * @param attempts number of attempts before lose the game
     * @throws IllegalStateException if the configuration is not consistent
     */
    public DrawNumberImpl(final int min, final int max, final int attempts) {
        this.min = min;
        this.max = max;
        this.attempts = attempts;
        this.reset();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        this.remainingAttempts = this.attempts;
        this.choice = this.min + random.nextInt(this.max - this.min + 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DrawResult attempt(final int n) {
        if (this.remainingAttempts <= 0) {
            return DrawResult.YOU_LOST;
        }
        if (n < this.min || n > this.max) {
            throw new IllegalArgumentException("The number is outside boundaries");
        }
        remainingAttempts--;
        if (n > this.choice) {
            return DrawResult.YOURS_HIGH;
        }
        if (n < this.choice) {
            return DrawResult.YOURS_LOW;
        }
        return DrawResult.YOU_WON;
    }

}
