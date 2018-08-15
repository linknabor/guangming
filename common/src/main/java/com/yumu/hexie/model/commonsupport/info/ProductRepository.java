package com.yumu.hexie.model.commonsupport.info;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	
	@Modifying
	@Query(value="update Product p set p.status = 1 where p.productNo = ?1",nativeQuery = true)
	public void invalidByProductNo(String productNo);
	
	@Modifying
	@Query(value="update Product p set p.status = 0 where p.productNo = ?1",nativeQuery = true)
	public void invalidByProductNoEnd(String productNo);
	
	@Modifying
	@Query(value="update Product p set p.oriPrice = :jdPrice,p.singlePrice = :price,p.miniPrice= :price where p.productNo = :productNo",nativeQuery = true)
	public void upProductPrice(@Param("productNo")String productNo,@Param("jdPrice")String jdPrice,@Param("price")String price);
	
	@Query(value = "select * from Product where productType =?1 and status = 1",nativeQuery = true)
	List<Product> findByProductType(String ProductType);
	
	@Query(value = "select * from Product where merchantId =?1 and status = 1",nativeQuery = true)
	List<Product> findByMerchantId(String MerchantId);
	
	@Query(value = "SELECT p.* FROM Product p JOIN merchant m ON p.merchantId = m.id WHERE m.name = '京东' AND (productNo IS NULL OR productNo = '')",nativeQuery = true)
	List<Product> findByJDProductNoIsNull();
	
	@Query(value = "SELECT p.* FROM Product p JOIN merchant m ON p.merchantId = m.id WHERE p.productNo IS NOT NULL AND m.name = '京东'",nativeQuery = true)
	List<Product> findByJDProductNoIsNotNull();
	
//	@Query("select ifnull(max(substring(productNo,5)),0) as max_no from Product "
//			+ "where productNo >= ?1 and productNo <= ?2 ")
	@Query("select max(substring(productNo,5)) as max_no from Product where productNo >= ?1 and productNo <= ?2 ")
	String getMaxProductNo(String start, String end);
	
	Product findByMerchanProductNo(String merchantProductNo);
	
//	@Query("from Product where name like %?1% and status = 1 and endDate > NOW() AND startDate < NOW() AND totalCount > 0 ")
//	@Query(value="SELECT * FROM product p "
//			+"INNER JOIN productitem pitem ON p.`productItemId` = pitem.`id` "
//			+"INNER JOIN onsalerule rule ON p.`id` = rule.`productId` "
//			+"INNER JOIN onsaleareaitem areaitem ON p.`id` = areaitem.`productId` "
//			+"WHERE p.`totalCount` > 0 "
//			+"AND p.`name` LIKE %?1% "
//			+"AND areaitem.`regionId` = ?2 "
//			+"AND (p.`endDate` > NOW() AND p.`startDate` < NOW()) "
//			+"AND p.`status` = 1 "
//			+"LIMIT ?3,2 ",nativeQuery = true)


	List<Product> findByProductItemAndStatus(ProductItem productItem, int status);
	

	

	List<Product> findByProductNo(String productNo);
}
