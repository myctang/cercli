package com.cercli.model;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Location wrapper
 */
public record Location(
    Integer countryId,
    Integer zoneId
) {
    public Location {
        requireNonNull(countryId, "countryId");
    }

    public Optional<Integer> zoneIdOptional() {
        return Optional.ofNullable(zoneId);
    }
}
