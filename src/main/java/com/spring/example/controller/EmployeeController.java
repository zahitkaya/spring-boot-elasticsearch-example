package com.spring.example.controller;

import com.spring.example.model.Employee;
import com.spring.example.model.SearchRequest;
import com.spring.example.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class EmployeeController {

    final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(value = "/employees")
    public ResponseEntity<List<Employee>> getEmployees(@RequestParam String department) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartment(department));
    }

    @GetMapping("/employees/field")
    public ResponseEntity<List<Employee>> getEmployeeByField(@RequestBody SearchRequest searchRequest) throws IOException {
        return ResponseEntity.ok(employeeService.searchByFieldValue(searchRequest));
    }

    @GetMapping("/employees/bool")
    public ResponseEntity<List<Employee>> getEmployeeByFieldBool(@RequestBody List<SearchRequest> searchRequest) throws IOException {
        return ResponseEntity.ok(employeeService.searchByFieldBoolQuery(searchRequest));
    }
}
