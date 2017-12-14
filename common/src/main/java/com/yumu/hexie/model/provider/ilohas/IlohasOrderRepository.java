package com.yumu.hexie.model.provider.ilohas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IlohasOrderRepository extends JpaRepository<IlohasOrder, Long> {
	
	IlohasOrder findByOrderNo(String orderNo);
	
	List<IlohasOrder> findByUpdated(Boolean updated);
	
	
}
