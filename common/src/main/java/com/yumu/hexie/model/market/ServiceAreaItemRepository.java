package com.yumu.hexie.model.market;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ServiceAreaItemRepository extends JpaRepository<ServiceAreaItem, Long> {
	
	List<ServiceAreaItem> findByProductId(Long productId);
	
	@Modifying
	@Query(value="update ServiceAreaItem set status = 0 where productId = ?1",nativeQuery = true)
	void upStatusEnd(String productNo);
	
	
	@Modifying
	@Query(value="update ServiceAreaItem set status = 1 where productId = ?1",nativeQuery = true)
	void upStatusStart(String productNo);
}
