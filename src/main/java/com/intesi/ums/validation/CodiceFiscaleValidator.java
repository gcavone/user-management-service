package com.intesi.ums.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Validates an Italian Codice Fiscale using:
 * 1. Format check (regex)
 * 2. Control character (16th char) checksum verification per the official algorithm
 *    defined by DPR 605/1973 and subsequent MEF specifications.
 */
public class CodiceFiscaleValidator implements ConstraintValidator<ValidCodiceFiscale, String> {

    private static final Pattern CF_PATTERN =
        Pattern.compile("^[A-Z]{6}[0-9LMNPQRSTUV]{2}[ABCDEHLMPRST]{1}[0-9LMNPQRSTUV]{2}[A-Z]{1}[0-9LMNPQRSTUV]{3}[A-Z]{1}$");

    // Values for even-position characters (0-indexed positions 1,3,5,7,9,11,13)
    private static final Map<Character, Integer> EVEN_VALUES = Map.ofEntries(
        Map.entry('0', 0), Map.entry('1', 1), Map.entry('2', 2), Map.entry('3', 3),
        Map.entry('4', 4), Map.entry('5', 5), Map.entry('6', 6), Map.entry('7', 7),
        Map.entry('8', 8), Map.entry('9', 9), Map.entry('A', 0), Map.entry('B', 1),
        Map.entry('C', 2), Map.entry('D', 3), Map.entry('E', 4), Map.entry('F', 5),
        Map.entry('G', 6), Map.entry('H', 7), Map.entry('I', 8), Map.entry('J', 9),
        Map.entry('K', 10), Map.entry('L', 11), Map.entry('M', 12), Map.entry('N', 13),
        Map.entry('O', 14), Map.entry('P', 15), Map.entry('Q', 16), Map.entry('R', 17),
        Map.entry('S', 18), Map.entry('T', 19), Map.entry('U', 20), Map.entry('V', 21),
        Map.entry('W', 22), Map.entry('X', 23), Map.entry('Y', 24), Map.entry('Z', 25)
    );

    // Values for odd-position characters (0-indexed positions 0,2,4,6,8,10,12,14)
    private static final Map<Character, Integer> ODD_VALUES = Map.ofEntries(
        Map.entry('0', 1), Map.entry('1', 0), Map.entry('2', 5), Map.entry('3', 7),
        Map.entry('4', 9), Map.entry('5', 13), Map.entry('6', 15), Map.entry('7', 17),
        Map.entry('8', 19), Map.entry('9', 21), Map.entry('A', 1), Map.entry('B', 0),
        Map.entry('C', 5), Map.entry('D', 7), Map.entry('E', 9), Map.entry('F', 13),
        Map.entry('G', 15), Map.entry('H', 17), Map.entry('I', 19), Map.entry('J', 21),
        Map.entry('K', 2), Map.entry('L', 4), Map.entry('M', 18), Map.entry('N', 20),
        Map.entry('O', 11), Map.entry('P', 3), Map.entry('Q', 6), Map.entry('R', 8),
        Map.entry('S', 12), Map.entry('T', 14), Map.entry('U', 16), Map.entry('V', 10),
        Map.entry('W', 22), Map.entry('X', 25), Map.entry('Y', 24), Map.entry('Z', 23)
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }

        String cf = value.toUpperCase().trim();

        if (!CF_PATTERN.matcher(cf).matches()) {
            return false;
        }

        return isChecksumValid(cf);
    }

    /**
     * Verifies the 16th character (control char) using the standard MEF algorithm.
     */
    private boolean isChecksumValid(String cf) {
        int sum = 0;
        for (int i = 0; i < 15; i++) {
            char c = cf.charAt(i);
            // positions are 1-indexed in the spec; odd positions use ODD_VALUES
            if (i % 2 == 0) {
                sum += ODD_VALUES.getOrDefault(c, 0);
            } else {
                sum += EVEN_VALUES.getOrDefault(c, 0);
            }
        }
        char expectedControl = (char) ('A' + (sum % 26));
        return cf.charAt(15) == expectedControl;
    }
}
