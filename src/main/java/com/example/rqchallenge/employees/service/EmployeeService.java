package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.pojos.Employee;
import com.example.rqchallenge.employees.pojos.GetAllEmployeeApiResponse;
import com.example.rqchallenge.employees.pojos.GetEmployeeApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final String API_URL = "https://dummy.restapiexample.com/api/v1/";
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public EmployeeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public List<Employee> getAllEmployees() {
        try {
            ResponseEntity<GetAllEmployeeApiResponse> response = restTemplate.getForEntity(API_URL + "employees", GetAllEmployeeApiResponse.class);
            if (response.getBody() != null && "success".equals(response.getBody().getStatus())) {
                List<Employee> employees = response.getBody().getData();
                //employees.forEach(employee -> logger.info(employee.toString()));
                return employees;
            } else {
                logger.error("Failed to retrieve employees: API returned an unsuccessful status");
                return Collections.emptyList();
            }
        } catch (RestClientException e) {
            logger.error(e.getMessage());
            return Collections.emptyList();
        }

    }

    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        try {
            List<Employee> employees = getAllEmployees();
            List<Employee> filteredEmployees = employees.stream()
                    .filter(employee -> employee.getEmployee_name().toLowerCase().contains(searchString.toLowerCase()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filteredEmployees);
        } catch (Exception e) {
            logger.error("Error while searching employees by name", e);
            return ResponseEntity.status(500).body(Collections.emptyList());
        }
    }

    public ResponseEntity<Employee> getEmployeeById(String id) {
        String apiUrl = API_URL + "employee/" + id;
        //logger.info("API URL: {}", apiUrl);
        try {
            GetEmployeeApiResponse employee = restTemplate.getForObject(apiUrl, GetEmployeeApiResponse.class);
            if (employee != null) {
                return ResponseEntity.ok(employee.getData());
            } else {
                logger.warn("Empty response received for employee ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Employee not found for ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error fetching employee data for ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


}


