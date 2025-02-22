package com.project.carservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.project.carservice.model.Employee;
import com.project.carservice.model.MaterialsPrice;
import com.project.carservice.model.Vehicle;
import com.project.carservice.repo.MaterialsPriceRepo;
import com.project.carservice.repo.VehicleRepo;
import com.project.carservice.service.EmployeeService;
import com.project.carservice.service.MaterialsPriceService;
import com.project.carservice.service.VehicleService;

@Controller
@RequestMapping("/vehicle")
public class VehicleController {
	
	@Autowired
	EmployeeService employeeService;
	

	@Autowired
	VehicleService vehicleService;
	
	@Autowired
	MaterialsPriceRepo materialsPriceRepo;
	
	@Autowired
	MaterialsPriceService materialsPriceService;
	
	@Autowired
	VehicleRepo vehicleRepo;
	
	@GetMapping("/vehicleEntry")
	public ModelAndView addnewvehicle() {
		List<MaterialsPrice> pricelist = materialsPriceRepo.findAll();
		ModelAndView mv = new ModelAndView("/vehicle/addvehicle","vehicle", new Vehicle());
		mv.addObject("pricelist", pricelist);
		return mv;
	}
	
	@PostMapping("/registervehicle")
	public ModelAndView registervehicle(@Validated @ModelAttribute("vehicle") Vehicle vehicle,BindingResult result) {		
		
		ModelAndView mv = new ModelAndView();
		if(result.hasErrors()) {
			List<MaterialsPrice> pricelist = materialsPriceRepo.findAll();
			mv.addObject("pricelist", pricelist);
			mv.setViewName("/vehicle/addvehicle");
		}
		else {
			String servicetype = vehicle.getServicetype();
			if(servicetype.equals("OTHERS")) {
				servicetype = "LABOUR FEE";
			}
			
			double serviceprice = materialsPriceService.getMaterialsPriceByitem(servicetype).getPrice();
			double issue1 = materialsPriceService.getMaterialsPriceByitem(vehicle.getIssuefirst()).getPrice();
			double issue2 = materialsPriceService.getMaterialsPriceByitem(vehicle.getIssuesecond()).getPrice();
			double issue3 = materialsPriceService.getMaterialsPriceByitem(vehicle.getIssuethird()).getPrice();
			
			double cost = serviceprice+issue1+issue2+issue3;
			
			vehicle.setEstimated_cost(cost);
			vehicleService.addvehicle(vehicle);
			
			mv.setViewName("redirect:/admin/admindashboard");
		}
		
		return mv;
		
	}

	@GetMapping("/assignvehicle")
	public ModelAndView assignvehicle(@RequestParam("vehicleId") int vehicleId , @RequestParam("employeeId") int employeeId) {

		Employee sp = employeeService.getemployeebyId(employeeId);
		
		Vehicle v = vehicleService.getVehicleById(vehicleId);
		if(!v.getAssigned().equals("ASSIGNED") ) {
			employeeService.assignvehicle(sp,v);
		}
		else {
			throw new RuntimeException("UNDER SERVICING");
		}
		ModelAndView mv = new ModelAndView("redirect:/admin/vehiclelist");
		return mv;
	}
	
	@GetMapping("/printbom")
	public ModelAndView printbom(@RequestParam int id) {

		Vehicle v=vehicleService.getVehicleById(id);
		
		String[] materials = v.getBillOfMaterials().toString().split(",");
		List<String[]> list1 = new ArrayList<>();
		list1.add(new String[] {v.getServicetype(),"1"});
		for(String s : materials) {
		 String[] str = s.split(":");
		 list1.add(str);

		}
			
		ModelAndView mv = new ModelAndView("/vehicle/bill");
		mv.addObject("vehicle", v);
		mv.addObject("list1",list1);
		mv.addObject("cost", v.getActual_cost());
		return mv;
	}
	
}

