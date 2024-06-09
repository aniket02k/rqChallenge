package com.example.rqchallenge.employees.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateEmployeeApiResponse {
    private String status;
    private CreateEmployee data;
    private String message;

}
