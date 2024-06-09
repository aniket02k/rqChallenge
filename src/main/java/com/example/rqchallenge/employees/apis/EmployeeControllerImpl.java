/* APIS tested
http://localhost:8080/employee/search?searchString=Yuri%20Berry

http://localhost:8080/employee/
http://localhost:8080/employee/1


http://localhost:8080/employee/highestSalary

http://localhost:8080/api/employee/topTenHighestEarningEmployeeNames

CREATE (POST)  ->  http://localhost:8080/employee/
DELETE (DELETE)-> http://localhost:8080/employee/1

 */

package com.example.rqchallenge.employees.apis;

import com.example.rqchallenge.employees.pojos.CreateEmployee;
import com.example.rqchallenge.employees.pojos.Employee;
import com.example.rqchallenge.employees.service.EmployeeService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employee")
@Data
public class EmployeeControllerImpl implements IEmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private RestTemplate restTemplate;
    private static final String API_URL = "https://dummy.restapiexample.com/api/v1/";
    private static final String STATUS_FAILED = "Failed";



    private static final Logger logger = LoggerFactory.getLogger(EmployeeControllerImpl.class);


    @Override
    @GetMapping()
    public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {
        List<Employee> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@RequestParam String searchString) {
        return employeeService.getEmployeesByNameSearch(searchString);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(String id) {
        return employeeService.getEmployeeById(id);
    }

    @Override
    @GetMapping("/highestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        try {
            List<Employee> employees = employeeService.getAllEmployees();

            if (employees.isEmpty()) {

                return ResponseEntity.notFound().build();
            }

            int highestSalary = employees.stream()
                    .mapToInt(Employee::getEmployee_salary)
                    .max()
                    .orElseThrow(); // Throws NoSuchElementException if employees list is empty

            return ResponseEntity.ok(highestSalary);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @Override
    @GetMapping("/topTenHighestEarningEmployeeNames")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        try {
            List<Employee> employees = employeeService.getAllEmployees();

            if (employees.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return  ResponseEntity.ok(employees.stream()
                    .sorted(Comparator.comparingDouble(Employee::getEmployee_salary).reversed())
                    .limit(10)
                    .map(Employee::getEmployee_name)
                    .collect(Collectors.toList()));


        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping()
    public ResponseEntity<String> createEmployee(@RequestBody Map<String, Object> employeeInput) {
        String id= (String) employeeInput.get("id");
        String name = (String) employeeInput.get("name");
        String salary = (String) employeeInput.get("salary");
        String age = (String) employeeInput.get("age");

        // Validate inputs
        if (name == null || salary == null || age == null) {
            return ResponseEntity.badRequest().body("Name, salary, and age are required.");
        }

        // Convert salary and age to appropriate types
        int salaryInt;
        int ageInt;
        int idInt;
        try {
            idInt= Integer.parseInt(id);
            salaryInt = Integer.parseInt(salary);
            ageInt = Integer.parseInt(age);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid salary or age format.");
        }

        // Create employee
        CreateEmployee employee = new CreateEmployee();
        employee.setId(idInt);
        employee.setName(name);
        employee.setSalary(salaryInt);
        employee.setAge(ageInt);

        // Make POST request to the dummy API
        RestTemplate restTemplate = new RestTemplate();
        String url = API_URL + "create";
        ResponseEntity<String> response = restTemplate.postForEntity(url, employee, String.class);

        // Check response status
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Employee created successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create employee.");
        }
    }




    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        try {
            // Make DELETE request to the dummy API
            RestTemplate restTemplate = new RestTemplate();
            String url = API_URL + "delete/" + id;
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);

            // Check response status
            if (response.getStatusCode() == HttpStatus.OK) {
                // Assuming the API returns the name of the deleted employee
                String deletedEmployeeName = response.getBody();
                return ResponseEntity.ok().body(deletedEmployeeName);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete employee.");
            }
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete employee.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete employee: " + e.getMessage());
        }
    }


}

