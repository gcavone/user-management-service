package com.intesi.ums.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CodiceFiscaleValidator")
class CodiceFiscaleValidatorTest {

    private final CodiceFiscaleValidator validator = new CodiceFiscaleValidator();

    @ParameterizedTest
    @ValueSource(strings = {
        "RSSMRA80A01H501U",  // Mario Rossi, Roma
        "VRDGNN80A01H501J",  // Giovanni Verdi, Roma
        "rssmra80a01h501u",  // lowercase — validator should uppercase internally
    })
    @DisplayName("accepts valid codici fiscali")
    void acceptsValid(String cf) {
        assertThat(validator.isValid(cf, null)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "RSSMRA80A01H501Z",  // wrong control char (should be U)
        "RSSMRA80A01H501",   // too short (15 chars)
        "RSSMRA80A01H501UU", // too long (17 chars)
        "12345678901234567", // all digits
        "",
        "INVALID",
    })
    @DisplayName("rejects invalid codici fiscali")
    void rejectsInvalid(String cf) {
        assertThat(validator.isValid(cf, null)).isFalse();
    }
}
