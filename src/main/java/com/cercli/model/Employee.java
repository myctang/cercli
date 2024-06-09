package com.cercli.model;

import com.cercli.domain.Email;
import com.cercli.domain.Money;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

import static com.cercli.platform.Checks.checkThat;
import static java.util.Objects.requireNonNull;

/**
 * Employee model
 */
@Getter
@Builder(toBuilder = true)
@ToString
public class Employee {
    private final UUID id;
    private final EmployeeState state;
    private final String name;
    private final String position;
    private final Email email;
    private final Money salary;
    private final Location location;
    private final Instant createdAt;
    private final Instant updatedAt;

    public Employee(UUID id,
                    EmployeeState state,
                    String name,
                    String position,
                    Email email,
                    Money salary,
                    Location location,
                    Instant createdAt,
                    Instant updatedAt) {
        this.id = requireNonNull(id, "id");
        this.state = requireNonNull(state, "state");
        this.name = requireNonNull(name, "name");
        this.position = requireNonNull(position, "position");
        this.email = requireNonNull(email, "email");
        this.salary = requireNonNull(salary, "salary");
        this.location = requireNonNull(location, "location");
        this.createdAt = requireNonNull(createdAt, "createdAt");
        this.updatedAt = requireNonNull(updatedAt, "updatedAt");

        checkThat(salary.isPositive(), "Salary must be positive");
    }

    public Employee updateSalary(Money newSalary) {
        return toBuilder()
            .salary(newSalary)
            .updatedAt(Instant.now())
            .build();
    }
}
