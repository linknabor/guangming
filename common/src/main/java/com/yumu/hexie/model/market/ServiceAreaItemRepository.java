package com.yumu.hexie.model.market;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceAreaItemRepository extends JpaRepository<ServiceAreaItem, Long> {
	
	List<ServiceAreaItem> findByProductId(Long productId);

}
