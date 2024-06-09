package com.cercli.service;

import com.cercli.model.Employee;
import com.cercli.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service to manage employees
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final TransactionTemplate transactionTemplate;

    public void save(Employee employee) {
        log.info("save(): id={}", employee.getId());
        transactionTemplate.executeWithoutResult(status -> employeeRepository.save(employee));
    }

    public void update(Employee employee) {
        log.info("update(): id={}", employee.getId());
        transactionTemplate.executeWithoutResult(status -> employeeRepository.update(employee));
    }

    public Optional<Employee> findById(UUID id) {
        log.info("findById(): id={}", id);
        return employeeRepository.findById(id);
    }

    public List<Employee> findFor(Integer countryId,
                                  @Nullable Integer zoneId) {
        log.info("findFor(): countryId={}, zoneId={}", countryId, zoneId);
        return employeeRepository.findFor(countryId, zoneId);
    }

    public List<Employee> getAll() {
        log.info("getAll()");
        return employeeRepository.getAll();
    }
}
