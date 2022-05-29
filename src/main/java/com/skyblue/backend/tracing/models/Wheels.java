package com.skyblue.backend.tracing.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skyblue.backend.security.repository.DataChange;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
@Table("wheels")
public class Wheels implements Serializable, DataChange {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    private Long id;
    Integer numberWheels;
    String position;
    String brand;
    String speedCode;
    Integer loadIndex;
    Double rimDiameter;
    Double cokeIndicator;
    @JsonIgnore
    Long vehicleId;

    @CreatedBy
    private String createBy;
    @CreatedDate
    private java.time.LocalDateTime createTime;
    @LastModifiedBy
    private String lastUpdateBy;
    @LastModifiedDate
    private java.time.LocalDateTime lastUpdateTime;

 
}
