package com.skyblue.backend.tracing.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Data

@Table("whels")
public class Whels implements Serializable {
    @Id
    int vehicleNumberWheels;
    String position;
    String brand;


}
