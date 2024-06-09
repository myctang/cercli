package com.cercli.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.regex.Pattern;

import static com.cercli.platform.Checks.checkThat;
import static java.util.Objects.requireNonNull;

/**
 * Email wrapper
 * Pattern is for demonstration only, should be changed to more precise
 */
public record Email(
    String value
) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    public Email {
        requireNonNull(value);
        checkThat(EMAIL_PATTERN.matcher(value).matches(), "Invalid email format");
    }

    @JsonCreator
    public static Email of(String email) {
        return new Email(email);
    }

    @JsonValue
    public String asString() {
        return value;
    }
}
