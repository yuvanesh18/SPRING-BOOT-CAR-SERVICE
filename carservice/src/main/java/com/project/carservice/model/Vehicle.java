package com.project.carservice.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Vehicle {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

@Column(nullable=false)
@NotBlank(message="Name is mandatory")
@Size(min=3,max=50,message="Name must between 3to50 characters")
private String customerName;

@Column(nullable=false)
@NotBlank(message=" Contact is mandatory")
@Pattern(regexp="\\d{10}",message="Contact must be a 10-digit number")
private String customerContact;

@Column(nullable = false)
@NotBlank(message="Registration Number is mandatory")
@Pattern(regexp="^[A-Z]{2}\\d{2}[A-Z]{1,2}\\d{4}$", message="Invalid Registration Number")
private String registrationNumber;

@Column(nullable=false)
@NotBlank(message=" Vehicle model is mandatory")
@Size(min=3,max=50,message="Vechile Model Name must between 3to50 characters")
private String model;

@Column
private String status="DUE";// due or under servicing or serviced

@Column(nullable=false)
private String warranty;//yes or no

@Column(nullable=false)
private LocalDate entryDate  = LocalDate.now();

private LocalDate ReleasedDate;

private String assigned = "UNASSIGNED";

@NotBlank(message="Service type is required")
private String servicetype;

private String issuedescription;
private String issuefirst;
private String issuesecond;
private String issuethird;

private double estimated_cost=0;
private double actual_cost=0;

private String paymentStatus="Not Paid";
private String paymentType;
private String referenceNumber;
private LocalDateTime paymentDate;

private StringBuilder billOfMaterials;

@ManyToOne
@JoinColumn(name="employee")
private Employee employee;

//getters and  setters

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public String getCustomerName() {
	return customerName;
}

public void setCustomerName(String customerName) {
	this.customerName = customerName;
}

public String getCustomerContact() {
	return customerContact;
}

public void setCustomerContact(String customerContact) {
	this.customerContact = customerContact;
}

public String getRegistrationNumber() {
	return registrationNumber;
}

public void setRegistrationNumber(String registrationNumber) {
	this.registrationNumber = registrationNumber;
}

public String getModel() {
	return model;
}

public void setModel(String model) {
	this.model = model;
}

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}

public String getWarranty() {
	return warranty;
}

public void setWarranty(String warranty) {
	this.warranty = warranty;
}

public LocalDate getEntryDate() {
	return entryDate;
}

public void setEntryDate(LocalDate entryDate) {
	this.entryDate = entryDate;
}

public LocalDate getReleasedDate() {
	return ReleasedDate;
}

public void setReleasedDate(LocalDate releasedDate) {
	ReleasedDate = releasedDate;
}

public String getAssigned() {
	return assigned;
}

public void setAssigned(String assigned) {
	this.assigned = assigned;
}

public String getServicetype() {
	return servicetype;
}

public void setServicetype(String servicetype) {
	this.servicetype = servicetype;
}

public String getIssuedescription() {
	return issuedescription;
}

public void setIssuedescription(String issuedescription) {
	this.issuedescription = issuedescription;
}

public String getIssuefirst() {
	return issuefirst;
}

public void setIssuefirst(String issuefirst) {
	this.issuefirst = issuefirst;
}

public String getIssuesecond() {
	return issuesecond;
}

public void setIssuesecond(String issuesecond) {
	this.issuesecond = issuesecond;
}

public String getIssuethird() {
	return issuethird;
}

public void setIssuethird(String issuethird) {
	this.issuethird = issuethird;
}

public double getEstimated_cost() {
	return estimated_cost;
}

public void setEstimated_cost(double estimated_cost) {
	this.estimated_cost = estimated_cost;
}

public double getActual_cost() {
	return actual_cost;
}

public void setActual_cost(double actual_cost) {
	this.actual_cost = actual_cost;
}

public String getPaymentStatus() {
	return paymentStatus;
}

public void setPaymentStatus(String paymentStatus) {
	this.paymentStatus = paymentStatus;
}

public String getPaymentType() {
	return paymentType;
}

public void setPaymentType(String paymentType) {
	this.paymentType = paymentType;
}

public String getReferenceNumber() {
	return referenceNumber;
}

public void setReferenceNumber(String referenceNumber) {
	this.referenceNumber = referenceNumber;
}

public LocalDateTime getPaymentDate() {
	return paymentDate;
}

public void setPaymentDate(LocalDateTime paymentDate) {
	this.paymentDate = paymentDate;
}

public StringBuilder getBillOfMaterials() {
	return billOfMaterials;
}

public void setBillOfMaterials(StringBuilder billOfMaterials) {
	this.billOfMaterials = billOfMaterials;
}

public Employee getEmployee() {
	return employee;
}

public void setEmployee(Employee employee) {
	this.employee = employee;
}

@Override
public String toString() {
	return "Vehicle [id=" + id + ", customerName=" + customerName + ", customerContact=" + customerContact
			+ ", registrationNumber=" + registrationNumber + ", model=" + model + ", status=" + status + ", warranty="
			+ warranty + "]";
}



}
