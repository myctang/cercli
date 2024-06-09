package com.cercli.repository;

import com.cercli.domain.Currency;
import com.cercli.domain.Email;
import com.cercli.domain.Money;
import com.cercli.model.Employee;
import com.cercli.model.EmployeeState;
import com.cercli.model.Location;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.cercli.public_.Tables.EMPLOYEES;
import static java.time.ZoneOffset.UTC;
import static org.jooq.impl.DSL.noCondition;

/**
 * Repository to manage employees in the db
 */
@Service
@RequiredArgsConstructor
public class EmployeeRepository {
    private static final RecordMapper<Record, Employee> EMPLOYEE_ROW_MAPPER = record -> Employee.builder()
        .id(record.get(EMPLOYEES.ID))
        .state(EmployeeState.valueOf(record.get(EMPLOYEES.STATE)))
        .name(record.get(EMPLOYEES.NAME))
        .position(record.get(EMPLOYEES.POSITION))
        .email(Email.of(record.get(EMPLOYEES.EMAIL)))
        .salary(Money.of(record.get(EMPLOYEES.SALARY_AMOUNT),
            Currency.valueOf(record.get(EMPLOYEES.SALARY_CURRENCY))))
        .location(new Location(record.get(EMPLOYEES.COUNTRY_ID), record.get(EMPLOYEES.ZONE_ID)))
        .createdAt(record.get(EMPLOYEES.CREATED_AT).toInstant(UTC))
        .updatedAt(record.get(EMPLOYEES.UPDATED_AT).toInstant(UTC))
        .build();

    private final DSLContext dslContext;

    /**
     * Save an employee in the db
     */
    public void save(Employee employee) {
        dslContext.insertInto(EMPLOYEES)
            .set(EMPLOYEES.ID, employee.getId())
            .set(EMPLOYEES.STATE, employee.getState().name())
            .set(EMPLOYEES.NAME, employee.getName())
            .set(EMPLOYEES.POSITION, employee.getPosition())
            .set(EMPLOYEES.EMAIL, employee.getEmail().asString())
            .set(EMPLOYEES.SALARY_AMOUNT, employee.getSalary().minorAmount())
            .set(EMPLOYEES.SALARY_CURRENCY, employee.getSalary().currency().name())
            .set(EMPLOYEES.COUNTRY_ID, employee.getLocation().countryId())
            .set(EMPLOYEES.ZONE_ID, employee.getLocation().zoneIdOptional().orElse(null))
            .set(EMPLOYEES.CREATED_AT, employee.getCreatedAt().atZone(UTC).toLocalDateTime())
            .set(EMPLOYEES.UPDATED_AT, employee.getUpdatedAt().atZone(UTC).toLocalDateTime())
            .execute();
    }

    /**
     * Update an employee in the db
     */
    public void update(Employee employee) {
        dslContext.update(EMPLOYEES)
            .set(EMPLOYEES.STATE, employee.getState().name())
            .set(EMPLOYEES.NAME, employee.getName())
            .set(EMPLOYEES.POSITION, employee.getPosition())
            .set(EMPLOYEES.EMAIL, employee.getEmail().asString())
            .set(EMPLOYEES.SALARY_AMOUNT, employee.getSalary().minorAmount())
            .set(EMPLOYEES.SALARY_CURRENCY, employee.getSalary().currency().name())
            .set(EMPLOYEES.COUNTRY_ID, employee.getLocation().countryId())
            .set(EMPLOYEES.ZONE_ID, employee.getLocation().zoneIdOptional().orElse(null))
            .set(EMPLOYEES.UPDATED_AT, employee.getUpdatedAt().atZone(UTC).toLocalDateTime())
            .where(EMPLOYEES.ID.eq(employee.getId()))
            .execute();
    }

    /**
     * Find an employee in the db by an id
     */
    public Optional<Employee> findById(UUID id) {
        return dslContext.selectFrom(EMPLOYEES)
            .where(EMPLOYEES.ID.eq(id))
            .fetchOptional(EMPLOYEE_ROW_MAPPER);
    }

    /**
     * Find an employee in the db by country id and zone id
     */
    public List<Employee> findFor(Integer countryId,
                                  @Nullable Integer zoneId) {
        return dslContext.selectFrom(EMPLOYEES)
            .where(EMPLOYEES.COUNTRY_ID.eq(countryId)
                .and(zoneId == null ? noCondition() : EMPLOYEES.ZONE_ID.eq(zoneId)))
            .fetch(EMPLOYEE_ROW_MAPPER);
    }

    /**
     * Get all employees from the db
     */
    public List<Employee> getAll() {
        return dslContext.selectFrom(EMPLOYEES)
            .fetch(EMPLOYEE_ROW_MAPPER);
    }
}
