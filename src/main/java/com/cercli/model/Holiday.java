package com.cercli.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * Holiday model
 */
@Getter
@ToString
@Builder
public class Holiday {
    private final UUID id;
    private final LocalDate date;
    private final Integer countryId;
    @Nullable
    private final Integer zoneId;
    private final Instant createdAt;
    private final Instant updatedAt;

    public Optional<Integer> getZoneId() {
        return Optional.ofNullable(zoneId);
    }
}
