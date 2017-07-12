package com.yumu.hexie.model.market;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SupermarketAssginRepository extends JpaRepository<SupermarketAssgin, Long> {
	
	@Query
	public List<SupermarketAssgin> findByServiceOrderId(long serviceOrderId);
	

}
