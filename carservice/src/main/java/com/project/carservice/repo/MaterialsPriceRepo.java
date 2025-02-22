package com.project.carservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.carservice.model.MaterialsPrice;

@Repository
public interface MaterialsPriceRepo extends JpaRepository<MaterialsPrice, Integer> {

	MaterialsPrice findByItemIgnoreCase(String item);

}
