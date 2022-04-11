package com.skyblue.backend.tracing.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With

@Table("vehicle")
public class Vehicle  implements Serializable {
	@Id
	private Integer vehicleId;
	private String vehicleCensus;
	private String vehicleLicensePlate;
	private String vehicleClass;
	private String vehicleModel;
	private int    vehicleOwnerId;
	private String vehiclePropertyCard;
	private String vehiclePolice;
	private String vehicleYerProduction;
	private String vehicleEngineNumber;
	private double vehicleWeight;
	private double vehicleGrossWeight;
	private String vehicleBrand;
	private int    vehicleNumberSeats;
	private int    vehicleNumberPassengers;
	private String vehicleFuelType;
	private String vehicleBodywork;
	private String vehicleColors;
	private int    vehicleNumberCylinders;
	//@OneToMany(cascade = CascadeType.ALL, mappedBy = "Whels")
	//private List<Whels> vehicleWheels;
	private double vehicleLength;
	private double vehicleWidth;
	private double vehicleHeight;
	private double vehicleUsefulLoad;
	private int    vehicleAxes;
	private String vehicleRegistrationDate;
	private String vehicleCondition;
	private String RegisterUser; 
}