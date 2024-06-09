package com.cercli.controller.request;

import com.cercli.domain.Email;
import com.cercli.domain.Money;
import com.cercli.model.Location;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

/**
 * Request data to create an employee
 */
@Jacksonized
@Builder
@Getter
@ToString
public class EmployeeCreateRequest {
    private final String name;
    private final String position;
    private final Email email;
    private final Money salary;
    private final Location location;
}
