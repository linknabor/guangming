package com.yumu.hexie.model.jingdong.regionsyn;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface RegionSynLimtRepository extends JpaRepository<RegionSynLimt, Long>{
	
	@Query(value = "select * from RegionSynLimt GROUP BY productno",nativeQuery = true)
	List<RegionSynLimt> findByproductNoAll();
	
	@Query(value = "select * from RegionSynLimt GROUP BY address",nativeQuery = true)
	List<RegionSynLimt> findByAddressAll();
	
	@Query(value = "select * from RegionSynLimt where address = ?1",nativeQuery = true)
	List<RegionSynLimt> findByAddress(String address);
}
