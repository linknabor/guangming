package com.yumu.hexie.model.distribution.region;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RegionRepository extends JpaRepository<Region, Long> {
    public List<Region> findAllByRegionType(int regionType);
    public List<Region> findAllByRegionType(int regionType,Pageable page);
	
	public List<Region> findAllByRegionTypeAndParentId(int regionType,long parentId);
	public List<Region> findAllByParentId(long parentId);
	
	public List<Region> findAllByParentIdAndName(long countyId,String xiaoquName);
    public List<Region> findByAmapId(long amapId);
    
    @Query(value="SELECT * FROM region WHERE regionType <4",nativeQuery=true)
    public List<Region> getAll();//查询全部 国 省市 区 不查第四级
}
