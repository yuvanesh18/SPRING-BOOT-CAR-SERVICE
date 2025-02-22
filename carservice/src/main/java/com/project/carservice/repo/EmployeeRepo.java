package com.project.carservice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.carservice.model.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Integer> {
	
	Employee findByusername(String email);
	
	List<Employee> findByavailablity(String availablity);
	
	int countByavailablity(String string);
	
//	List<Employee> findByType(String string);
	
	List<Employee> findByAvailablityAndStatus(String string, String string2);

	boolean existsByUsername(String username);

	List<Employee> findByTypeAndStatusNot(String string, String string2);

}
