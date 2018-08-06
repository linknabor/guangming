package com.yumu.hexie.model.jingdong;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JDregionMappingRepository extends JpaRepository<JDregionMapping, Long>{
	
	@Query("from JDregionMapping where parentid = ?1 and regionid = ?2")
	public JDregionMapping getByRegionId(long parentid,long regionid);
}
