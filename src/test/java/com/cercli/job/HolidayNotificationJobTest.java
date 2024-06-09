package com.cercli.job;

import com.cercli.domain.Email;
import com.cercli.domain.Money;
import com.cercli.model.Employee;
import com.cercli.model.Holiday;
import com.cercli.model.Location;
import com.cercli.service.EmployeeService;
import com.cercli.service.HolidayService;
import com.cercli.service.NotificationService;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static com.cercli.domain.Currency.USD;
import static com.cercli.model.EmployeeState.ACTIVE;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.*;

public class HolidayNotificationJobTest {
    private static final Random RANDOM = new Random();

    private final HolidayService holidayService = mock(HolidayService.class);
    private final EmployeeService employeeService = mock(EmployeeService.class);
    private final NotificationService notificationService = mock(NotificationService.class);
    private final HolidayNotificationJob job = new HolidayNotificationJob(holidayService, employeeService,
        notificationService);

    @Test
    void sends_notifications() {
        // given
        var holidayOne = randomHoliday();
        var holidayTwo = randomHoliday();
        when(holidayService.findFor(LocalDate.now().plusWeeks(1)))
            .thenReturn(List.of(holidayOne, holidayTwo));

        var employeeOne = randomEmployee();
        var employeeTwo = randomEmployee();
        when(employeeService.findFor(holidayOne.getCountryId(), holidayOne.getZoneId().orElse(null)))
            .thenReturn(List.of(employeeOne, employeeTwo));

        var employeeThree = randomEmployee();
        when(employeeService.findFor(holidayTwo.getCountryId(), holidayTwo.getZoneId().orElse(null)))
            .thenReturn(List.of(employeeThree));

        // when
        job.run();

        // then
        verify(notificationService).send(employeeOne, holidayOne);
        verify(notificationService).send(employeeTwo, holidayOne);
        verify(notificationService).send(employeeThree, holidayTwo);
        verifyNoMoreInteractions(notificationService);
    }

    @Test
    void sends_nothing_when_no_holidays_returned() {
        // given
        when(holidayService.findFor(LocalDate.now().plusWeeks(1)))
            .thenReturn(List.of());

        // when
        job.run();

        // then
        verifyNoMoreInteractions(notificationService);
    }

    @Test
    void sends_nothing_when_no_employees_for_holiday() {
        // given
        var holidayOne = randomHoliday();
        when(holidayService.findFor(LocalDate.now().plusWeeks(1)))
            .thenReturn(List.of(holidayOne));

        when(employeeService.findFor(holidayOne.getCountryId(), holidayOne.getZoneId().orElse(null)))
            .thenReturn(List.of());

        // when
        job.run();

        // then
        verifyNoMoreInteractions(notificationService);
    }

    private Holiday randomHoliday() {
        return Holiday.builder()
            .id(randomUUID())
            .date(LocalDate.now().plusDays(RANDOM.nextInt(7)))
            .countryId(RANDOM.nextInt(1_000_000))
            .zoneId(RANDOM.nextInt(1_000_000))
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }

    private Employee randomEmployee() {
        return Employee.builder()
            .id(randomUUID())
            .state(ACTIVE)
            .name("John Black")
            .position("Manager")
            .email(Email.of("john@google.com"))
            .salary(Money.of(1_00, USD))
            .location(new Location(1, 1))
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }
}
