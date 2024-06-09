package com.cercli.controller;

import com.cercli.controller.response.HolidayResponse;
import com.cercli.service.EmployeeService;
import com.cercli.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

/**
 * Controller to process holidays related requests
 */
@RestController
@RequestMapping("/api/holidays")
@RequiredArgsConstructor
public class HolidayController {
    private final HolidayService holidayService;
    private final EmployeeService employeeService;

    /**
     * Get holidays of an employee for the upcoming 7 days
     */
    @GetMapping("/employee/{id}")
    public ResponseEntity<List<HolidayResponse>> getForEmployee(@PathVariable UUID id) {
        var employee = employeeService.findById(id);
        if (employee.isEmpty()) {
            return notFound().build();
        }

        var from = LocalDate.now();
        var to = from.plusWeeks(1);
        var location = employee.get().getLocation();
        var holidays = holidayService.findFor(from, to, location.countryId(),
            location.zoneIdOptional().orElse(null));
        return ok(holidays.stream().map(HolidayResponse::of).toList());
    }
}
