package com.project.carservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.carservice.model.Employee;
import com.project.carservice.model.Vehicle;
import com.project.carservice.repo.EmployeeRepo;
import com.project.carservice.repo.MaterialsPriceRepo;
import com.project.carservice.repo.VehicleRepo;

@Service
public class EmployeeService {

	@Autowired
	EmployeeRepo employeeRepo;
	
	@Autowired
	MaterialsPriceRepo materialsPriceRepo;
	
	@Autowired
	VehicleRepo vehicleRepo;
	
	public boolean checkcredentials(String email, String pass) {
		Employee e = employeeRepo.findByusername(email);
		if(e!=null && email.equals(e.getUsername()) && pass.equals(e.getPassword()) && !e.getStatus().equals("FIRED")) {
			return true;
		}
		return false;
	}
	
	public Employee getemployeebyId(int id) {
		return employeeRepo.findById(id).orElseThrow(()->new RuntimeException("Employee Not Found"));
	}
	
	public Employee getemployeebyusername(String email) {	
		return employeeRepo.findByusername(email);
	}
	
	public List<Employee> getFreeActiveEmployees() {
		return employeeRepo.findByAvailablityAndStatus("FREE","ACTIVE");
	}
	
	public List<Employee> getemployeelist() {
		return employeeRepo.findByTypeAndStatusNot("service advisor","FIRED");
	}
	
	public List<Employee> getemployeelist(String availablity) {
		return employeeRepo.findByavailablity(availablity);
	}
	
	public void addemployee(Employee e) {
		e.setPassword(e.getDob().toString());
		employeeRepo.save(e);
	}
	
	public void resetpassword(Employee e) {
			employeeRepo.save(e);
	}
	
	public void assignvehicle(Employee e, Vehicle v) {

		e.setVehicleid(v.getId());
		e.setAvailablity("WORKING");
		employeeRepo.save(e);
		
		v.setEmployee(e);
		v.setAssigned("ASSIGNED");
		v.setStatus("UNDER SERVICING");
		vehicleRepo.save(v);
		
	}

	public Map<String, Integer> getemployeeavailablityCount() {
		Map<String,Integer> map = new HashMap<>();
		map.put("FREE", employeeRepo.countByavailablity("FREE"));
		map.put("WORKING", employeeRepo.countByavailablity("WORKING"));
		return map;
	}

	public void updateworkingstatus(Employee employee) {
		employee.setAvailablity("FREE");
		employee.setVehicleid(0);
		employeeRepo.save(employee);
		
	}

	public boolean existsbyUsername(String username) {
		return employeeRepo.existsByUsername(username);
	}

	public int getemployeeCount() {
		return (int) employeeRepo.count();
	}

	public int getemployeeavailablityByCount(String string) {
		return employeeRepo.countByavailablity(string);
	}

	public boolean deleteEmployee(int id) {
		Employee employee = employeeRepo.findById(id).get();
		if(employee.getAvailablity().equalsIgnoreCase("FREE")) {
			employee.setStatus("FIRED");
			employee.setAvailablity("NO MORE");
			employeeRepo.save(employee);
			return true;
		}
		else {
			return false;
		}		
	}
	
	public void generateBillOfMaterials(int vehicleid, String serviceItems, String quantities) {
		
		Vehicle v = vehicleRepo.findById(vehicleid);

		if(!v.getStatus().equalsIgnoreCase("serviced") && !v.getStatus().equalsIgnoreCase("completed") ) {
			double cost = 0;
			StringBuilder bom = new StringBuilder();
			if(!serviceItems.isBlank() || !serviceItems.isEmpty()) {
				String[] itemsArray = serviceItems.split(",");
				String[] quantitiesArray = quantities.split(",");
				
				for(int i=0;i<itemsArray.length;i++) {
					if(i>0 && i<itemsArray.length) {
						bom.append(",");
					}
					bom.append(itemsArray[i]+":"+quantitiesArray[i]);
					 cost += materialsPriceRepo.findByItemIgnoreCase(itemsArray[i]).getPrice() * Integer.parseInt(quantitiesArray[i]);
					 
					 String itemwarrantycheck=materialsPriceRepo.findByItemIgnoreCase(itemsArray[i]).getItem();
					 if((itemwarrantycheck.equalsIgnoreCase("ENGINE REPLACEMENT") || itemwarrantycheck.equalsIgnoreCase("BATTERY REPLACEMENT") || itemwarrantycheck.equalsIgnoreCase("BRAKE DRUM") || itemwarrantycheck.equalsIgnoreCase("BRAKE DISC")) && v.getWarranty().equalsIgnoreCase("yes")) {
						 cost -= materialsPriceRepo.findByItemIgnoreCase(itemsArray[i]).getPrice() * Integer.parseInt(quantitiesArray[i]);
					 }
				}
			}
		
			String servicetype = v.getServicetype();
			if(servicetype.equals("OTHERS")) {
				servicetype = "LABOUR FEE";
			}
			
			double service_cost =   materialsPriceRepo.findByItemIgnoreCase(servicetype).getPrice();
	
			v.setActual_cost(cost+service_cost);
			v.setBillOfMaterials(bom);
			vehicleRepo.save(v);

		}
		
	}

	public int countByavailablity(String string) {
		// TODO Auto-generated method stub
		return employeeRepo.countByavailablity(string);	}

}
