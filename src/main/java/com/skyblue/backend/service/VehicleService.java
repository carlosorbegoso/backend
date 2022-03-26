package com.skyblue.backend.service;

import com.skyblue.backend.models.Vehicle;
import com.skyblue.backend.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class VehicleService implements VehicleRepository {
	@PersistenceContext
	private EntityManager entityManager;
	@Override
	public List<Vehicle> findByVehicleId(int id) {
		String query = "FROM Vehicle WHERE VehicleId = :id";
		var results = entityManager.createQuery(query,Vehicle.class)
				.setParameter("id",id)
				.getResultList();
		return null;
	}

	@Override
	public List<Vehicle> findByVehicleCensus(String census) {
		return null;
	}

	@Override
	public List<Vehicle> findByVehicleLicensePlate(String licencePlate) {
		return null;
	}

	@Override
	public List<Vehicle> listVehicleOnPages(String status, int page, int size) {
		return null;
	}

	@Override
	public List<Vehicle> vehicleCount() {
		return null;
	}

	@Override
	public Vehicle createAndUpdateVehicle(Vehicle vehicle) {
		return null;
	}
}
