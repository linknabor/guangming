package com.yumu.hexie.model.distribution.region;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
	//根据商品类型来判断
	@Query("from Merchant p where p.productType = ?1")
	public Merchant findMerchantByProductType(int productType);
	
	//根据商户名称来查找商户
	@Query(value="select * from Merchant p where p.name like %?1% " ,nativeQuery=true)
	public Merchant findMechantByNameLike(String merchantName);
}
