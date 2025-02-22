package com.project.carservice.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.carservice.model.Vehicle;
import com.project.carservice.repo.VehicleRepo;

@Service
public class VehicleService {
	
	@Autowired
	VehicleRepo vehicleRepo;

	public Vehicle getVehicleById(int id) {
		return vehicleRepo.findById(id);
	}
	
	public List<Vehicle> getVehiclelist() {
		return vehicleRepo.findByStatusNot("COMPLETED");
	}
	
	public List<Vehicle> getVehiclelistbystatus(String status) {
		return vehicleRepo.findByStatus(status);
	}
	
	public void addvehicle(Vehicle vehicle) {
		 vehicleRepo.save(vehicle);
	}

	public Map<String, Integer> getVehicleStatusCount() {
		Map<String,Integer> map = new HashMap<>();
		map.put("DUE", vehicleRepo.countByStatus("DUE"));
		map.put("UNDER SERVICING", vehicleRepo.countByStatus("UNDER SERVICING"));
		map.put("SERVICED", vehicleRepo.countByStatus("SERVICED"));
		return map;
	}

	public void updatevehiclestatus(Vehicle v) {
		v.setStatus("COMPLETED");
		v.setReleasedDate(LocalDate.now());
		vehicleRepo.save(v);	
	}

	public int getVehicleCount() {
		return (int) vehicleRepo.count();
	}

	public int getVehicleStatusCompletedCount(String string) {
		return vehicleRepo.countByStatus(string);
	}

	



	
}

