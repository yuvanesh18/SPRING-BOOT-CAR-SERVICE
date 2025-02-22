package com.project.carservice.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.carservice.model.Employee;
import com.project.carservice.model.MaterialsPrice;
import com.project.carservice.model.Vehicle;
import com.project.carservice.repo.MaterialsPriceRepo;
import com.project.carservice.service.EmployeeService;
import com.project.carservice.service.VehicleService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	VehicleService vehicleService;
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	MaterialsPriceRepo materialsPriceRepo;
	// vehicle dashboard controller
	
	@GetMapping("/vehiclelist")
	public ModelAndView gotovehiclemanagementpage()
	{
		ModelAndView mv = new ModelAndView("/admin/vehicledashboard");
		List<Vehicle> list = vehicleService.getVehiclelist();
		if(list==null) {
			list = new ArrayList<>();
		}
		mv.addObject("vehiclelist", list);
		int employeeCount = employeeService.getemployeeavailablityByCount("FREE");
		List<Employee> employeeList = employeeService.getFreeActiveEmployees();
		mv.addObject("employeeCount", employeeCount);
		mv.addObject("employeeList", employeeList);
		return mv;
	}
	
	@GetMapping("/filtervehicle")
	public ModelAndView gotovehiclemanagementpage_byfilter(@RequestParam("status") String status)
	{
		ModelAndView mv = new ModelAndView("/admin/vehicledashboard");
		List<Vehicle> list;
		if(status == null || status.isEmpty())
				list = vehicleService.getVehiclelist();
		else
				list = vehicleService.getVehiclelistbystatus(status);
		mv.addObject("vehiclelist", list);
		
		int employeeCount = employeeService.getemployeeavailablityByCount("FREE");
		List<Employee> employeeList = employeeService.getemployeelist("FREE");
		mv.addObject("employeeCount", employeeCount);
		mv.addObject("employeeList", employeeList);
		return mv;
	}

	// admin controller	
	@GetMapping("/admindashboard")
	public ModelAndView goto_admin_dashboard() {
		 ModelAndView mv = new ModelAndView("/admin/admindashboard");
		
		 Map<String,Integer> map = vehicleService.getVehicleStatusCount();
		 mv.addObject("statuscount", map);
		 
		 Map<String,Integer> map1 = employeeService.getemployeeavailablityCount();
		 mv.addObject("availablitycount", map1);
		 
		 List<Vehicle> pendingapprovals = vehicleService.getVehiclelistbystatus("SERVICED");
		 mv.addObject("pendingapprovals", pendingapprovals);
		 
		 int completedcount = vehicleService.getVehicleStatusCompletedCount("COMPLETED");
		 int employeeCount =employeeService.countByavailablity("FREE");

		 int approvalpendingCount = pendingapprovals.size();
		 int totalcount = vehicleService.getVehicleCount();
		 
		 mv.addObject("completedCount", completedcount);
		 mv.addObject("employeeCount", employeeCount);
		 mv.addObject("approvalpendingCount", approvalpendingCount);
		 mv.addObject("totalcount", totalcount);
		 
		 
		return mv;
	}

	
	@GetMapping("/passwordresetform")
	public ModelAndView resetpasswordform() {
		ModelAndView mv = new ModelAndView("passwordreset");
		return mv;
	}
	
	@PostMapping("/resetpassword")
	public ModelAndView resetpassword(@RequestParam String password1,@RequestParam String password2,HttpSession hs) {
		String email = (String) hs.getAttribute("email");

		Employee employee = employeeService.getemployeebyusername(email);
		
		ModelAndView mv = new ModelAndView();
		if(employee.getPassword().equals(password1)) {
			 mv = new ModelAndView("passwordreset");
			 mv.addObject("error", "NEW PASSWORD IS SAME AS OLD PASSWORD");
		}
		else if(!password1.equals(password2)) {
			 mv = new ModelAndView("redirect:/passwordresetform");
			 mv.addObject("error", "PASSWORDS  NOT MATCHED");
		}
		else {
			employee.setPassword(password2);
			employeeService.resetpassword(employee);
			mv.setViewName("redirect:/login");
		}

		return mv;
	}
	
	@PostMapping("/approvebom")
	public ModelAndView approvebom(@RequestParam int recordid) {
		
		Vehicle v = vehicleService.getVehicleById(recordid);
		vehicleService.updatevehiclestatus(v);
		
		Employee employee = v.getEmployee();
		employeeService.updateworkingstatus(employee);
		
		ModelAndView mv = new ModelAndView("redirect:/admin/admindashboard");
		return mv;
	}
	
	@GetMapping("/paymentpage")
	public ModelAndView paymentdashboard(ModelAndView mv,@RequestParam int id) {
	
		Vehicle v=vehicleService.getVehicleById(id);
		
		List<String[]> list1 = new ArrayList<>();
		
		list1.add(new String[] {v.getServicetype(),"1"});
		
		for(String[] s : list1) {
			System.out.println(s[0]+" "+s[1]);
		}
		
		StringBuilder sb= v.getBillOfMaterials();
		
		while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
		    sb.deleteCharAt(0);
		}

		// Remove trailing spaces
		while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
		    sb.deleteCharAt(sb.length() - 1);
		}
		
		if(!v.getBillOfMaterials().toString().isEmpty()) {
			System.out.println(v.getBillOfMaterials() +"HI");
			String[] materials = v.getBillOfMaterials().toString().split(",");
			for(String s : materials) {
				 String[] str = s.split(":");
				 list1.add(str);
				}
		}
		
		mv.addObject("id", id);
		mv.addObject("list1",list1);
		mv.addObject("cost", v.getActual_cost());
		mv.setViewName("/admin/paymentpage");
		return mv;
	}
	
	@PostMapping("/paymentsuccess")
	public ModelAndView paymentsuccess(ModelAndView mv,@RequestParam int id,@RequestParam String paymentmethod,@RequestParam String referenceNumber) {
		Vehicle v=vehicleService.getVehicleById(id);
		v.setPaymentStatus("Paid");
		v.setPaymentType(paymentmethod);
		v.setReferenceNumber(referenceNumber);
		v.setPaymentDate(LocalDateTime.now());
		vehicleService.addvehicle(v);
		mv.setViewName("redirect:/admin/admindashboard");
		return mv;
	}
	
	@GetMapping("/serviceHistory")
	public ModelAndView servicehistory(ModelAndView mv) {
		List<Vehicle> list = vehicleService.getVehiclelistbystatus("COMPLETED");
		mv.addObject("vehiclelist", list);
		mv.setViewName("/admin/serviceHistory");
		return mv;
	}
	
	@GetMapping("/materialsList")
	public ModelAndView materialsform(ModelAndView mv) {
		List<MaterialsPrice> list = materialsPriceRepo.findAll();
		mv.addObject("materials", list);
		mv.setViewName("/admin/materials");
		return mv;
	}
	
	@PostMapping("/insertMaterial")
	public ModelAndView insertmaterial(ModelAndView mv,@RequestParam String item,@RequestParam String price) {
		
		MaterialsPrice mp = new MaterialsPrice();
		mp.setItem(item);
		mp.setPrice(Double.parseDouble(price));
		materialsPriceRepo.save(mp);

		mv.setViewName("redirect:/admin/materialsList");
		return mv;
	}
	
	@PostMapping("/updatematerial")
	public ModelAndView updateproblem(ModelAndView mv,@RequestParam int id,@RequestParam String newPrice) {
		MaterialsPrice mp = materialsPriceRepo.findById(id).get();
		mp.setPrice(Double.parseDouble(newPrice));
		materialsPriceRepo.save(mp);
		mv.setViewName("redirect:/admin/materialsList");
		return mv;
	}
	
	@PostMapping("/deletematerial")
	public ModelAndView deleteproblem(ModelAndView mv,@RequestParam int id) {
		MaterialsPrice mp = materialsPriceRepo.findById(id).get();
		materialsPriceRepo.delete(mp);
		mv.setViewName("redirect:/admin/materialsList");
		return mv;
	}
	
	@PostMapping("/generatebill")
	public ModelAndView generatebill(@RequestParam int vehicleid,@RequestParam String selectedMaterials,@RequestParam String quantities){
		employeeService.generateBillOfMaterials(vehicleid,selectedMaterials,quantities);
		Vehicle v=vehicleService.getVehicleById(vehicleid);
		v.setStatus("SERVICED");
		vehicleService.addvehicle(v);
		ModelAndView mv = new ModelAndView("redirect:/employee/backtodashboard");
		return mv;
	}
	
	
	
	//Employee controller

	@GetMapping("/employeelist")
	public ModelAndView goto_employee_management() {
		
		List<Employee> list = employeeService.getemployeelist();
		ModelAndView mv = new ModelAndView("/admin/employeepage","employeelist",list);
		return mv;
	}
	
	@GetMapping("/filteremployeelist")
	public ModelAndView goto_employee_page_byfilter(@RequestParam("availablity") String availablity)
	{		
		List<Employee> list;
		if(availablity==null || availablity.isEmpty()) {
			list= employeeService.getemployeelist();
		}
		else {
			list = employeeService.getemployeelist(availablity);
		}
		ModelAndView mv = new ModelAndView("/admin/employeepage","employeelist", list);
		
		return mv;
	}
	
	@GetMapping("/terminateEmployee")
	public ModelAndView deleteEmployee(ModelAndView mv,@RequestParam int id,RedirectAttributes redirectAttributes) {
		boolean status = employeeService.deleteEmployee(id);
		if(status) {
			redirectAttributes.addFlashAttribute("message", "EMPLOYEE FIRED SUCCESSFULLY");
		}
		else {
			redirectAttributes.addFlashAttribute("error", "EMPLOYEE CANNOT BE FIRED, HE'S UNDER WORKING ON HIS ASSIGNED MISSION");
		}
		mv.setViewName("redirect:/admin/employeelist");
		return mv;
	}
	
	
	
	
	
	
}

