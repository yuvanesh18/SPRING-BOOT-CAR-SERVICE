package com.project.carservice.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.carservice.model.Vehicle;
import com.project.carservice.service.EmployeeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	EmployeeService employeeService;
	
	@GetMapping("/")
	public ModelAndView method1(ModelAndView mv) {
		mv.setViewName("index");
		return mv;
	}
	
	@GetMapping("/login")
	public ModelAndView login(ModelAndView mv) {
		mv.setViewName("login");
		return mv;
	}
	
	@PostMapping("/logout")
	public ModelAndView logout(HttpServletRequest req) {	
		HttpSession hs = req.getSession(false);
		if(hs!=null) {
			hs.invalidate();
		}
		ModelAndView  mv = new ModelAndView("redirect:/");
		return mv;
	}
	
	

	@PostMapping("/checkdashboard")
	public ModelAndView goto_admin_user_page(HttpServletRequest req,HttpSession hs, RedirectAttributes redirectAttributes) {
		ModelAndView mv = null;
		String email=req.getParameter("email");
		String pass = req.getParameter("password");
		hs.setAttribute("email", email);
		hs.setAttribute("pass", pass);
		
		if(employeeService.checkcredentials(email,pass)) {
			if(employeeService.getemployeebyusername(email).getType().equalsIgnoreCase("admin")) {
				 mv = new ModelAndView("redirect:/admin/admindashboard");
			}
			else if(employeeService.getemployeebyusername(email).getType().equalsIgnoreCase("service advisor")) {
				redirectAttributes.addFlashAttribute("vehiclehistory", new ArrayList<Vehicle>());
				 mv = new ModelAndView("redirect:/employee/employeedashboard");
			}
			else {
				 mv = new ModelAndView("login");
				 mv.addObject("error", "YOU ARE NOT ALLOWED TO ACCESS THIS WEBSITE");
			 }
		}
		else {
			 mv = new ModelAndView("login");
			 mv.addObject("error", "CREDENTIALS IS NOT VALID");
		}

		return mv;			
	}
}
	

