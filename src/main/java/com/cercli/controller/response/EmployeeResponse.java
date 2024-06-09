package com.cercli.controller.response;

import com.cercli.domain.Email;
import com.cercli.domain.Money;
import com.cercli.model.Employee;
import com.cercli.model.EmployeeState;
import com.cercli.model.Location;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

/**
 * Response data of a user
 */
@Builder
@ToString
@Getter
public class EmployeeResponse {
    private final UUID id;
    private final EmployeeState state;
    private final String name;
    private final String position;
    private final Email email;
    private final Money salary;
    private final Location location;
    // Clients must handle the required timezone on their side, since it's not a responsibility of the server to know
    // which timezone the client expects
    private final Instant createdAt;
    private final Instant updatedAt;

    public static EmployeeResponse of(Employee employee) {
        return EmployeeResponse.builder()
            .id(employee.getId())
            .state(employee.getState())
            .name(employee.getName())
            .position(employee.getPosition())
            .email(employee.getEmail())
            .salary(employee.getSalary())
            .location(employee.getLocation())
            .createdAt(employee.getCreatedAt())
            .updatedAt(employee.getUpdatedAt())
            .build();
    }
}
