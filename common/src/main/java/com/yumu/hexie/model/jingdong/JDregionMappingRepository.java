package com.yumu.hexie.model.jingdong;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JDregionMappingRepository extends JpaRepository<JDregionMapping, Long>{
	
	@Query("from JDregionMapping where parentid = ?1 and regionid = ?2")
	public JDregionMapping getByRegionId(long parentid,long regionid);
	
	@Query(value = "SELECT * FROM JDregionMapping j WHERE j.name = ?1",nativeQuery = true)
	public JDregionMapping getByName(String name);
	
	@Query(value = "SELECT * FROM JDregionMapping m WHERE m.parentname = ?1",nativeQuery = true)
	public List<JDregionMapping> getByParentName(String name);
}
