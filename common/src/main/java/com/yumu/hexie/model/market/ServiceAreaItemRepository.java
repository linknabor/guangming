package com.yumu.hexie.model.market;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceAreaItemRepository extends JpaRepository<ServiceAreaItem, Long> {
	
	ServiceAreaItem findByProductId(Long productId);

}
