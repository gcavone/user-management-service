package com.intesi.ums.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom constraint annotation for Italian Codice Fiscale validation.
 * Validates format AND checksum character using the official algorithm.
 */
@Documented
@Constraint(validatedBy = CodiceFiscaleValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCodiceFiscale {

    String message() default "Codice fiscale non valido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
