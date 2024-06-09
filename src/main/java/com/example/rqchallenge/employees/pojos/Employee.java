package com.example.rqchallenge.employees.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private int id;
    private String employee_name;
    private int employee_salary;
    private int employee_age;
}
