package com.cercli.controller.response;

import com.cercli.model.Holiday;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Response data of a holiday
 */
@Builder
@Getter
@ToString
public class HolidayResponse {
    private final LocalDate date;

    public static HolidayResponse of(Holiday holiday) {
        return HolidayResponse.builder()
            .date(holiday.getDate())
            .build();
    }
}
