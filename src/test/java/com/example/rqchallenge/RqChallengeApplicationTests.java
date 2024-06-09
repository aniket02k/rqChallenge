package com.example.rqchallenge;

import com.example.rqchallenge.employees.apis.EmployeeControllerImpl;
import com.example.rqchallenge.employees.pojos.Employee;
import com.example.rqchallenge.employees.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.Assertions;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)

class RqChallengeApplicationTests {
    private static final String API_URL = "https://dummy.restapiexample.com/api/v1/";

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeControllerImpl employeeController;

    @Test
    public void testGetAllEmployees_Success() throws IOException {
        // Mock employeeService behavior
        List<Employee> mockEmployees = new ArrayList<>();
        mockEmployees.add(new Employee(1, "John", 30000, 25));
        when(employeeService.getAllEmployees()).thenReturn(mockEmployees);

        // Call the controller method
        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();

        // Assertions
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(mockEmployees, response.getBody());
    }
    @Test
    public void testGetEmployeesByNameSearch_Success() {
        String searchString = "John";
        List<Employee> mockEmployees = new ArrayList<>();
        mockEmployees.add(new Employee(1, "John", 30000, 25));
        when(employeeService.getEmployeesByNameSearch(searchString)).thenReturn(new ResponseEntity<>(mockEmployees, HttpStatus.OK));

        ResponseEntity<List<Employee>> response = employeeController.getEmployeesByNameSearch(searchString);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(mockEmployees, response.getBody());
    }

    @Test
    public void testGetHighestSalaryOfEmployees_Success() {
        List<Employee> mockEmployees = new ArrayList<>();
        mockEmployees.add(new Employee(1, "John", 30000, 25));
        mockEmployees.add(new Employee(2, "Jane", 40000, 30));
        when(employeeService.getAllEmployees()).thenReturn(mockEmployees);

        ResponseEntity<Integer> response = employeeController.getHighestSalaryOfEmployees();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(40000, response.getBody().intValue());
    }

    @Test
    public void testGetHighestSalaryOfEmployees_EmptyList() {
        when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());

        ResponseEntity<Integer> response = employeeController.getHighestSalaryOfEmployees();

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteEmployeeById_Success() {
        String id = "123";

        // Mock RestTemplate behavior
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

        // Inject mock and call the method
        employeeController.setRestTemplate(restTemplate);
        ResponseEntity<String> response = employeeController.deleteEmployeeById(id);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCreateEmployee_Success() {
        // Mock data
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "John Doe");
        employeeInput.put("salary", "50000");
        employeeInput.put("age", "30");
        employeeInput.put("id","1111");

        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

        // Inject mock and call the method
        employeeController.setRestTemplate(restTemplate);
        ResponseEntity<String> response = employeeController.createEmployee(employeeInput);

        // Assertions
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void contextLoads() {
    }

}
