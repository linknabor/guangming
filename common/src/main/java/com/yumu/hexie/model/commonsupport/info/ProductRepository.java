package com.yumu.hexie.model.commonsupport.info;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
//	@Query("select ifnull(max(substring(productNo,5)),0) as max_no from Product "
//			+ "where productNo >= ?1 and productNo <= ?2 ")
	@Query("select max(substring(productNo,5)) as max_no from Product where productNo >= ?1 and productNo <= ?2 ")
	String getMaxProductNo(String start, String end);
	
	Product findByMerchanProductNo(String merchantProductNo);
}
