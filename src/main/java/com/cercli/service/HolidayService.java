package com.cercli.service;

import com.cercli.model.Holiday;
import com.cercli.repository.HolidaysRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.util.List;

/**
 * Service to manage holidays
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HolidayService {
    private final HolidaysRepository holidaysRepository;
    private final TransactionTemplate transactionTemplate;

    public void store(Holiday holiday) {
        log.info("store(): id={}", holiday.getId());
        transactionTemplate.executeWithoutResult(status -> holidaysRepository.store(holiday));
    }

    public List<Holiday> findFor(LocalDate date) {
        log.info("findFor(): date={}", date);
        return holidaysRepository.findFor(date);
    }

    public List<Holiday> findFor(LocalDate from,
                                 LocalDate to,
                                 Integer countryId,
                                 @Nullable Integer zoneId) {
        log.info("findFor(): from={} to={} countryId={}", from, to, countryId);
        return holidaysRepository.findFor(from, to, countryId, zoneId);
    }
}
