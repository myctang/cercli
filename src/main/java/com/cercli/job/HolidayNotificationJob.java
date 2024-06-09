package com.cercli.job;

import com.cercli.model.Employee;
import com.cercli.model.Holiday;
import com.cercli.service.EmployeeService;
import com.cercli.service.HolidayService;
import com.cercli.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Job to send notification about upcoming holidays to employees
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HolidayNotificationJob {
    private final HolidayService holidayService;
    private final EmployeeService employeeService;
    private final NotificationService notificationService;

    // Better to send the notifications using a dedicated model Notification to track the state, notification time, etc.
    // It also allows to retry failed notifications for a specific user, and does not force the job to run once a day
    // since it will be possible to check whether we already sent a notification about this holiday to this user
    // To implement it we would need 2 jobs (queues), first to generate Notification models based on Holidays and Employees
    // and second to process Notifications (send them to the employees)

    @Scheduled(cron = "0 0 0 * * *")
    public void run() {
        log.info("run()");

        var targetDate = LocalDate.now().plusWeeks(1);
        var holidays = holidayService.findFor(targetDate);
        holidays.forEach(holiday -> {
            try {
                process(holiday);
            } catch (Exception e) {
                log.error("Could not send notifications for {}", holiday);
            }
        });
    }

    private void process(Holiday holiday) {
        var employees = employeeService.findFor(holiday.getCountryId(), holiday.getZoneId().orElse(null));
        employees.forEach(employee -> {
            try {
                send(employee, holiday);
            } catch (Exception e) {
                log.error("Could not send notifications for {}, {}", employee, holiday);
            }
        });
    }

    private void send(Employee employee,
                      Holiday holiday) {
        notificationService.send(employee, holiday);
    }
}
