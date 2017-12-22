/**
 * 
 */
package com.yumu.hexie.model.provider.ilohas;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author davidhardson
 *
 */
public interface IlohasProductRepository extends JpaRepository<IlohasProduct, Long> {
	
	IlohasProduct findByCode(String code);
	
}
