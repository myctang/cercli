package com.cercli.platform;

/**
 * Checks methods
 */
public class Checks {
    /**
     * Verifies that the result is true, else throws an IllegalStateException
     */
    public static void checkThat(boolean result, String message, Object ... args) {
        if (!result) {
            throw new IllegalStateException(String.format(message, args));
        }
    }
}
