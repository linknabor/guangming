package com.yumu.hexie.model.commonsupport.info;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
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
	@Query(value="SELECT pitem.* FROM productitem pitem  "
+"INNER JOIN product p ON p.`productItemId` = pitem.`id` "
+"INNER JOIN onsaleareaitem areaitem ON p.`id` = areaitem.`productId`  "
+"WHERE ((areaitem.regionType=0)  "
+"OR (areaitem.regionType=1 AND areaitem.regionId=?1)  "
+"OR (areaitem.regionType=2 AND areaitem.regionId=?2)  "
+"OR (areaitem.regionType=3 AND areaitem.regionId=?3)  "
+"OR (areaitem.regionType=4 AND areaitem.regionId=?4)) "
+"AND (p.`endDate` > NOW() AND p.`startDate` < NOW()) "
+"AND p.`status` = 1 "
+"AND p.`totalCount`>0	 "
+"AND pitem.`status` = 1 "
+"AND (pitem.`endDate` > NOW() AND pitem.`startDate` < NOW()) "
+"AND pitem.`name` LIKE %?5% "
+"GROUP BY pitem.`name` "
+"LIMIT ?6,6 ",nativeQuery = true)
	List<ProductItem> getByNameProduct(long provinceId, long cityId, long countyId, long xiaoquId,String name,int pageNow);

	List<Product> findByProductItemAndStatus(ProductItem productItem, int status);
	
	@Query(value="SELECT pitem.* FROM productitem pitem "
			 +"INNER JOIN product p ON p.`productItemId` = pitem.`id` "
			 +"INNER JOIN onsaleareaitem areaitem ON p.`id` = areaitem.`productId` "
			 +"WHERE ((areaitem.regionType=0)  "
			 +"OR (areaitem.regionType=1 AND areaitem.regionId=?1)  "
			 +"OR (areaitem.regionType=2 AND areaitem.regionId=?2)  "
			 +"OR (areaitem.regionType=3 AND areaitem.regionId=?3)  "
			 +"OR (areaitem.regionType=4 AND areaitem.regionId=?4)) "	
			 +"and (p.`endDate` > NOW() AND p.`startDate` < NOW())"
			 +"AND p.`status` = 1 "
			 +"AND p.`totalCount`>0	 "
			 +"AND (pitem.`endDate` > NOW() AND pitem.`startDate` < NOW())	"		 
			 +"AND pitem.`status` = 1 "			 
			 +"AND pitem.`productclassificationid` = ?5 "
			 +"GROUP BY pitem.`name` "
			 +"LIMIT ?6,6 ",nativeQuery = true)
	List<ProductItem> getByProductCfiId(long provinceId, long cityId, long countyId, long xiaoquId,int productcfiid,int pageNow);

}
