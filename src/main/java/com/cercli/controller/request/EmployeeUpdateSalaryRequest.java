package com.cercli.controller.request;

import com.cercli.domain.Money;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

/**
 * Request data to update the salary of an employee
 */
@Builder
@Getter
@ToString
@Jacksonized
public class EmployeeUpdateSalaryRequest {
    private final UUID employeeId;
    private final Money salary;
}
