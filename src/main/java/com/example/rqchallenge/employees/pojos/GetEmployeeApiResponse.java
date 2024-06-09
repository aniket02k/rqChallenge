package com.example.rqchallenge.employees.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetEmployeeApiResponse {
    private String status;
    private Employee data;
}
