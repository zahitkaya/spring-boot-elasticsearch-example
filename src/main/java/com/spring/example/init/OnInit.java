package com.spring.example.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.example.model.Employee;
import com.spring.example.repository.EmployeeRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class OnInit implements ApplicationRunner {

    private final EmployeeRepository employeeRepository;

    public OnInit(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (employeeRepository.count() > 0) {
            return;
        }

        ObjectMapper mapper = new ObjectMapper();

// read JSON file and map/convert to java POJO
        try {
            List<Employee> someClassObject = mapper.readValue(new File("src/main/resources/static/example.json"), List.class);
            employeeRepository.saveAll(someClassObject);
            System.out.printf("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
