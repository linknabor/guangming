package com.yumu.hexie.model.provider.ilohas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IlohasOrderRepository extends JpaRepository<IlohasOrder, Long> {
	
	IlohasOrder findByOrderNo(String orderNo);
	
	@Query(value="select * from ilohasOrder where status = ?1 and payDate >= ?2 and payDate <= ?3 ", nativeQuery = true)
	List<IlohasOrder> findByStatusAndPayDate(String orderStatus, String beginDate, String endDate);
	
	
}
