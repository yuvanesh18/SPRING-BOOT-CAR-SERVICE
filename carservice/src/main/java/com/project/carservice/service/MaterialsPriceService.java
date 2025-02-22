package com.project.carservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.carservice.model.MaterialsPrice;
import com.project.carservice.repo.MaterialsPriceRepo;

@Service
public class MaterialsPriceService {

	@Autowired
	MaterialsPriceRepo materialsPriceRepo;
	
	public MaterialsPrice getMaterialsPriceByitem(String item) {
		// TODO Auto-generated method stub
		return materialsPriceRepo.findByItemIgnoreCase(item);
	}

}

