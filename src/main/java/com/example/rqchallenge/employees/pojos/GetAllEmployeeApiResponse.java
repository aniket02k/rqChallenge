package com.example.rqchallenge.employees.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GetAllEmployeeApiResponse {
    private String status;
    private List<Employee> data;
}
