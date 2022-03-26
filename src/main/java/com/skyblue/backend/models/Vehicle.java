package com.skyblue.backend.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
@Data
@Entity
@JsonInclude
@Table(name = "vehicle")
public class Vehicle {
	@Id
	@NotNull
	private int vehicleId;
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
	private int    vehicleNumberWheels;
	private double vehicleLength;
	private double vehicleWidth;
	private double vehicleHeight;
	private double vehicleUsefulLoad;
	private int    vehicleAxes;
	private String vehicleRegistrationDate;
	private String vehicleCondition;
	private String RegisterUser;
}
