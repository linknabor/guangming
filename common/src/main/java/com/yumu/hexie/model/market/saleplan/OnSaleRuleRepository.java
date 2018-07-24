package com.yumu.hexie.model.market.saleplan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
@Transactional
public interface OnSaleRuleRepository extends JpaRepository<OnSaleRule, Long> {
	
	List<OnSaleRule> findAllByProductId(long productId);
	
	<T> List<T> findByProductItemIdAndStatus(long productItemId, int status);
	@Modifying
	@Query(value="update OnSaleRule set status = 0 where productId = ?1",nativeQuery = true)
	void upStatusEnd(String productNo);
	
	
	@Modifying
	@Query(value="update OnSaleRule set status = 1 where productId = ?1",nativeQuery = true)
	void upStatusStart(String productNo);
	
	@Modifying
	@Query(value="UPDATE OnSaleRule o SET o.price = :price,o.oriPrice = :jdPrice WHERE o.productId = :productNo",nativeQuery = true)
	public void upProductPrice(@Param("productNo")String productNo,@Param("jdPrice")String jdPrice,@Param("price")String price);
	
}
