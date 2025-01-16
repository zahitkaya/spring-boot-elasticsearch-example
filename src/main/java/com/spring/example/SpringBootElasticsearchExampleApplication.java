package com.spring.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.spring.example.repository")
public class SpringBootElasticsearchExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootElasticsearchExampleApplication.class, args);
    }

}
