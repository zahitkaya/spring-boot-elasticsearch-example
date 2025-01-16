package com.spring.example.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.spring.example.model.Employee;
import com.spring.example.model.SearchRequest;
import com.spring.example.repository.EmployeeRepository;
import com.spring.example.util.ElasticSearchUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ElasticsearchClient elasticsearchClient;

    public EmployeeService(EmployeeRepository employeeRepository, ElasticsearchClient elasticsearchClient) {
        this.employeeRepository = employeeRepository;
        this.elasticsearchClient = elasticsearchClient;
    }

    public List<Employee> getEmployeesByDepartment(String department) {
        return employeeRepository.getEmployeesByDepartment(department);
    }

    public List<Employee> searchByFieldValue(SearchRequest searchRequest) throws IOException {
        Supplier<Query> querySupplier = ElasticSearchUtil.buildQueryForFieldAndValue(searchRequest.getName(), searchRequest.getValue());
        SearchResponse<Employee> response = elasticsearchClient
                .search(s -> s.index("employee").query(querySupplier.get()), Employee.class);
        return extractItemsFromResponse(response);
    }


    public List<Employee> searchByFieldBoolQuery(List<SearchRequest> searchRequest) throws IOException {
        Supplier<Query> querySupplier = ElasticSearchUtil.createBoolQuery(searchRequest);
        SearchResponse<Employee> response = elasticsearchClient
                .search(s -> s.index("employee").query(querySupplier.get()), Employee.class);
        return extractItemsFromResponse(response);
    }


    public List<Employee> extractItemsFromResponse(SearchResponse<Employee> response) {
        return response
                .hits()
                .hits()
                .stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

}
