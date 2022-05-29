package com.skyblue.backend.tracing.models;

import com.skyblue.backend.security.repository.DataChange;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
@Table("vehicle")
public class Vehicle  implements Serializable, DataChange {
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	private String code;
	private String licensePlate;
	private String type;
	private String model;
	private String propertyCard;
	private String policy;
	private String engineSeries;
	private Double netWeight;
	private Double grossWeight;
	private String brand;
	private Integer numberSeats;
	private Integer numberPassengers;
	private String fuelType;
	private String bodywork;
	private String colors;
	private Integer numberCylinders;
	private Double length;
	private Double width;
	private Double height;
	private Double usefulLoad;
	private Integer axes;
	private Double mileage;
	private String condition;//active, inactive, deleted
	@CreatedBy
	private String createBy;
	@CreatedDate
	private java.time.LocalDateTime createTime;
	@LastModifiedBy
	private String lastUpdateBy;
	@LastModifiedDate
	private java.time.LocalDateTime lastUpdateTime;
	@Transient
	private List<Wheels> wheels;

	//private Integer ownerId;
}