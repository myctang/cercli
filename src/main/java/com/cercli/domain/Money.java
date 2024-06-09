package com.cercli.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import static java.util.Objects.requireNonNull;

/**
 * Money domain class
 * Minor fraction should be defined for each currency (e.g. 2 for USD, 3 for JPY)
 */
public record Money(
    long minorAmount,
    Currency currency
) {
    public Money {
        requireNonNull(currency, "currency");
    }

    public static Money of(long minorAmount, Currency currency) {
        return new Money(minorAmount, currency);
    }

    @JsonIgnore
    public boolean isPositive() {
        return minorAmount > 0;
    }
}
