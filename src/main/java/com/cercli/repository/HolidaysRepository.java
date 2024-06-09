package com.cercli.repository;

import com.cercli.model.Holiday;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.cercli.public_.Tables.HOLIDAYS;
import static java.time.ZoneOffset.UTC;

/**
 * Repository to manage holidays in the db
 */
@Service
@RequiredArgsConstructor
public class HolidaysRepository {
    private static final RecordMapper<Record, Holiday> RECORD_MAPPER = record -> Holiday.builder()
        .id(record.get(HOLIDAYS.ID))
        .date(record.get(HOLIDAYS.DATE))
        .countryId(record.get(HOLIDAYS.COUNTRY_ID))
        .zoneId(record.get(HOLIDAYS.ZONE_ID))
        .createdAt(record.get(HOLIDAYS.CREATED_AT).toInstant(UTC))
        .updatedAt(record.get(HOLIDAYS.UPDATED_AT).toInstant(UTC))
        .build();

    private final DSLContext dslContext;

    /**
     * Store a holiday in the db
     */
    public void store(Holiday holiday) {
        dslContext.insertInto(HOLIDAYS)
            .set(HOLIDAYS.ID, holiday.getId())
            .set(HOLIDAYS.DATE, holiday.getDate())
            .set(HOLIDAYS.COUNTRY_ID, holiday.getCountryId())
            .set(HOLIDAYS.ZONE_ID, holiday.getZoneId().orElse(null))
            .set(HOLIDAYS.CREATED_AT, holiday.getCreatedAt().atZone(UTC).toLocalDateTime())
            .set(HOLIDAYS.UPDATED_AT, holiday.getUpdatedAt().atZone(UTC).toLocalDateTime())
            .execute();
    }

    /**
     * Find holidays for a date
     */
    public List<Holiday> findFor(LocalDate date) {
        return dslContext.selectFrom(HOLIDAYS)
            .where(HOLIDAYS.DATE.eq(date))
            .fetch(RECORD_MAPPER);
    }

    /**
     * Find holidays between dates for country id and zone id
     */
    public List<Holiday> findFor(LocalDate from,
                                 LocalDate to,
                                 Integer countryId,
                                 @Nullable Integer zoneId) {
        return dslContext.selectFrom(HOLIDAYS)
            .where(HOLIDAYS.DATE.ge(from)
                .and(HOLIDAYS.DATE.lt(to))
                .and(HOLIDAYS.COUNTRY_ID.eq(countryId))
                .and(HOLIDAYS.ZONE_ID.eq(zoneId).or(HOLIDAYS.ZONE_ID.isNull())))
            .fetch(RECORD_MAPPER);
    }
}
