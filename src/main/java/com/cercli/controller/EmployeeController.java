package com.cercli.controller;

import com.cercli.controller.request.EmployeeCreateRequest;
import com.cercli.controller.request.EmployeeUpdateSalaryRequest;
import com.cercli.controller.response.EmployeeResponse;
import com.cercli.model.Employee;
import com.cercli.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.cercli.model.EmployeeState.ACTIVE;
import static java.util.UUID.randomUUID;

/**
 * Controller to process employees related requests
 */
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    /**
     * Create an employee
     */
    @PostMapping("")
    public ResponseEntity<EmployeeResponse> addEmployee(@RequestBody EmployeeCreateRequest request) {
        var employee = Employee.builder()
            .id(randomUUID())
            .state(ACTIVE)
            .name(request.getName())
            .position(request.getPosition())
            .email(request.getEmail())
            .salary(request.getSalary())
            .location(request.getLocation())
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
        employeeService.save(employee);
        return ResponseEntity.ok(EmployeeResponse.of(employee));
    }

    /**
     * Update salary of an employee
     */
    @PostMapping("/updateSalary")
    public ResponseEntity<EmployeeResponse> updateSalary(@RequestBody EmployeeUpdateSalaryRequest request) {
        var employee = employeeService.findById(request.getEmployeeId());
        if (employee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var updatedEmployee = employee.get().updateSalary(request.getSalary());
        employeeService.update(updatedEmployee);
        return ResponseEntity.ok(EmployeeResponse.of(updatedEmployee));
    }

    /**
     * Get an employee by its id
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> findById(@PathVariable UUID id) {
        var employee = employeeService.findById(id);
        if (employee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EmployeeResponse.of(employee.get()));
    }

    /**
     * Get all employees
     * ! Pagination should be added in case of significant amount of employees
     */
    @GetMapping("")
    public ResponseEntity<List<EmployeeResponse>> getAll() {
        var employees = employeeService.getAll()
            .stream()
            .map(EmployeeResponse::of)
            .toList();
        return ResponseEntity.ok(employees);
    }
}
