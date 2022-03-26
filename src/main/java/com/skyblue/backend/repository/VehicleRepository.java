package com.skyblue.backend.repository;

import com.skyblue.backend.models.Vehicle;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository {
	List<Vehicle> findByVehicleId(int id);
	List<Vehicle> findByVehicleCensus(String census);
	List<Vehicle>findByVehicleLicensePlate(String licencePlate);
	List<Vehicle>listVehicleOnPages(String status,int page,int size);
	List<Vehicle> vehicleCount();
	Vehicle createAndUpdateVehicle(Vehicle vehicle);

}
