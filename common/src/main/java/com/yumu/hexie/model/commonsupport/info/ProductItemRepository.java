package com.yumu.hexie.model.commonsupport.info;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.yumu.hexie.model.ModelConstant;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {

	public List<ProductItem> findByStatus(int status);
	
	@Query(value = "select distinct pi.* from ProductItem pi join OnSaleRule rule on pi.id = rule.productItemId "
			+ "join OnSaleAreaItem m on m.productItemId = pi.id and m.productItemId = rule.productItemId "
			+ "where rule.productType = ?1 and pi.status = ?2 " 
			+ "and m.status=" + ModelConstant.DISTRIBUTION_STATUS_ON  
			+ " and ((m.regionType=0) "
			+ "or (m.regionType=1 and m.regionId=?3) "
			+ "or (m.regionType=2 and m.regionId=?4) "
			+ "or (m.regionType=3 and m.regionId=?5) "
			+ "or (m.regionType=4 and m.regionId=?6)) "
			+ "and m.ruleCloseTime > ?7 "
			+ "and m.productItemId > 0 "
			+ "limit ?8, 100 "
//			+ "ORDER BY ?#{#pageable} "
			,
//			countQuery = "select count(distinct pi.*) from ProductItem pi join OnSaleRule rule on pi.id = rule.productItemId "
//					+ "join OnSaleAreaItem m on m.prodcutItemId = pi.id and m.productItemId = rule.productItemId "
//					+ "where rule.productType = ?1 and pi.status = ?2" 
//					+ "and m.status="+ModelConstant.DISTRIBUTION_STATUS_ON+" and ((m.regionType=0) "
//					+ "or (m.regionType=1 and m.regionId=?3) "
//				+ "or (m.regionType=2 and m.regionId=?4) "
//				+ "or (m.regionType=3 and m.regionId=?5) "
//				+ "or (m.regionType=4 and m.regionId=?6)) "
//				+ "and m.ruleCloseTime > ?7 "
//				+ "and m.productItemId > 0 ",
			nativeQuery = true)
	public List<ProductItem> queryProductItemsByType(int productType, int status,
			long provinceId, long cityId, long countyId, long xiaoquId, long current, int page);
	
	
	@Query(value = "select distinct pi.* from ProductItem pi join OnSaleRule rule on pi.id = rule.productItemId "
			+ "join OnSaleAreaItem m on m.productItemId = pi.id and m.productItemId = rule.productItemId "
			+ "where rule.productType = ?1 and pi.status = ?2 " 
			+ "and m.status=" + ModelConstant.DISTRIBUTION_STATUS_ON  
			+ " and ((m.regionType=0) "
			+ "or (m.regionType=1 and m.regionId=?3) "
			+ "or (m.regionType=2 and m.regionId=?4) "
			+ "or (m.regionType=3 and m.regionId=?5) "
			+ "or (m.regionType=4 and m.regionId=?6)) "
			+ "and m.ruleCloseTime > ?7 "
			+ "and m.productItemId > 0 "
			+ "order by saledNum desc limit ?8, 100 ", 
			nativeQuery = true)
	public List<ProductItem> queryHotProductItems(int productType, int status, 
			long provinceId, long cityId, long countyId, long xiaoquId, long current, int page);
	
	
}
