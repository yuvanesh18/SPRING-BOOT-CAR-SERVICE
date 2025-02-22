package com.project.carservice.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable=false)
	@NotBlank(message="Name is mandatory")
	@Size(min=3,max=50,message="Name must between 3to50 characters")
	private String name;
	
	@Column(nullable=false)
	@NotBlank(message=" Contact is mandatory")
	@Pattern(regexp="\\d{10}",message="Contact must be a 10-digit number")
	private String contact;
	
	@Column(nullable = false)
	private LocalDate dob;
	
	@Column(nullable=false)
	@NotBlank(message=" address is mandatory")
	@Size(min=3,max=50,message="Name must between 3to150 characters")
	private String address;
	
	@Column(nullable=false,unique = true)
	@NotBlank(message="Username is required")
	@Email(message="Invalid email format")
	private String username;
	
	private String password;
	
	@Column
	private String availablity="FREE"; //FREE OR WORKING
	
	@Column(nullable = false)
	private String type="";  //admin or service advisor
	
	@Column(nullable = false)
	private String status="ACTIVE";
	
	@Column
	private int vehicleid = 0;
	
	@OneToMany(mappedBy = "employee")
	private List<Vehicle> servicedVehicles;
	
	//getter and setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAvailablity() {
		return availablity;
	}

	public void setAvailablity(String availablity) {
		this.availablity = availablity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getVehicleid() {
		return vehicleid;
	}

	public void setVehicleid(int vehicleid) {
		this.vehicleid = vehicleid;
	}

	public List<Vehicle> getServicedVehicles() {
		return servicedVehicles;
	}

	public void setServicedVehicles(List<Vehicle> servicedVehicles) {
		this.servicedVehicles = servicedVehicles;
	}
	
	@Override
	public String toString() {
		return id + "-" + name;
	}
	
	
}

