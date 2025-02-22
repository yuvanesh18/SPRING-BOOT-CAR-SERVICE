package com.project.carservice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.carservice.model.Vehicle;

@Repository
public interface VehicleRepo extends JpaRepository<Vehicle, Integer> {
	
	Vehicle findById(int id);

	List<Vehicle> findByStatus(String status);
	
	int countByStatus(String string);

	List<Vehicle> findByRegistrationNumber(String registrationNumber);

	List<Vehicle> findByStatusNot(String string);

	

	

	
}
