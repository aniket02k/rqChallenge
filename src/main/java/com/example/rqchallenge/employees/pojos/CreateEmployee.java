package com.example.rqchallenge.employees.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployee {
    private int id;
    private String name;
    private int salary;
    private int age;
    private String profile_image;

}
