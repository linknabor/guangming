package com.yumu.hexie.model.commonsupport.info;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductclassificationRepository extends JpaRepository<Productclassification, Long> {
	@Query("from Productclassification where parentid = 1 and status = 1 order by sortNo")
	List<Productclassification> getParentProductCfi();
	
	@Query("from Productclassification where parentid = ?1 and status = 1 order by sortNo")
	List<Productclassification> getByParentIDProductCfi(int parentid);

}
