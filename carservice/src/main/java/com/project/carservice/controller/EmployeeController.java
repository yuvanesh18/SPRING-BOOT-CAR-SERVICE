package com.project.carservice.controller;

import java.time.LocalDate;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.carservice.model.Employee;
import com.project.carservice.model.MaterialsPrice;
import com.project.carservice.model.Vehicle;
import com.project.carservice.repo.MaterialsPriceRepo;
import com.project.carservice.repo.VehicleRepo;
import com.project.carservice.service.EmployeeService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	MaterialsPriceRepo materialsPriceRepo;
	
	@Autowired
	VehicleRepo vehicleRepo;
	
	@GetMapping("/addemployeeform")
	public ModelAndView addnewvehicle() {
		LocalDate maxDOB = LocalDate.now().minusYears(18);
		ModelAndView mv = new ModelAndView("/employee/addemployee","employee", new Employee());
		mv.addObject("maxDOB", maxDOB);
		return mv;
	}
	
	@PostMapping("/registeremployee")
	public ModelAndView registervehicle(@Validated @ModelAttribute("employee") Employee employee , BindingResult result) {
		ModelAndView mv = new ModelAndView();
		
		boolean checkExistingusername =   employeeService.existsbyUsername(employee.getUsername());
		if(checkExistingusername) {
			result.rejectValue("username","error.employee","Email Already Exists.   Try new Email");
		}
		
		if(result.hasErrors()) {
			LocalDate maxDOB = LocalDate.now().minusYears(18);
			mv.addObject("maxDOB", maxDOB);
			mv.setViewName("/employee/addemployee");
		}
		else {
			employeeService.addemployee(employee);
			mv.setViewName("redirect:/admin/admindashboard");
		}
		
		return mv;
	}
	
	@GetMapping("/employeedashboard")
	public ModelAndView goto_employee_dashboard(HttpSession hs, @ModelAttribute("vehiclehistory") List<Vehicle> vh) {
		
		ModelAndView mv = new ModelAndView("/employee/employeedashboard");
		String email = (String) hs.getAttribute("email");
		
		Employee employee = employeeService.getemployeebyusername(email);
		hs.setAttribute("id", employee.getId());
		mv.addObject("name", employee.getName());
		
	    List<Vehicle> list = employee.getServicedVehicles();
	    
	    List<MaterialsPrice> pricelist = materialsPriceRepo.findAll();
	    
	    mv.addObject("servicehistory", list);
	    mv.addObject("employee", employee);
	    mv.addObject("materials", pricelist);
	    mv.addObject("vehiclehistory", vh);
	    
		return mv;
	}
	
	@GetMapping("/backtodashboard")
	public ModelAndView backtodashboard(RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView("redirect:/employee/employeedashboard");
		redirectAttributes.addFlashAttribute("vehiclehistory", new ArrayList<Vehicle>());
		return mv;
	}
	
	
	@GetMapping("/checkhistory")
	public ModelAndView gethistory(@RequestParam String registrationNumber,RedirectAttributes redirectAttributes) {
		List<Vehicle> vh = vehicleRepo.findByRegistrationNumber(registrationNumber);
		if(vh==null) {
				vh=new ArrayList<>();
		}
		System.out.println(vh);
		redirectAttributes.addFlashAttribute("vehiclehistory", vh);
		ModelAndView mv = new ModelAndView("redirect:/employee/employeedashboard");
		return mv;
	}
	

}


