package com.spring.example.repository;

import com.spring.example.model.Employee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EmployeeRepository extends ElasticsearchRepository<Employee, String> {

    List<Employee> getEmployeesByDepartment(String department);
}
